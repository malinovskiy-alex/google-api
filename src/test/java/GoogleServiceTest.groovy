import org.junit.Test

import java.text.Bidi

/**
 * Created by Александр on 10.11.2014.
 */

class GoogleServiceTest {
    public int speed = 30
    GoogleService gs = new GoogleService()

    @org.junit.Test
    public void generateDistance() {
        assert gs.generateDistance("gvardeytsev shironintsev street 73", "gvardeytsev shironintsev street 73") == 9.9
    }

    @Test
    public void testGoThroughTheString() {
        def arr = Arrays.asList(
                new Point(address: "Московський просп., 122, Харків, Харківська область"),
                new Point(address: "Московський просп., 197, Харків, Харківська область"),
                new Point(address: "Куп'янська вул., 5, Харків, Харківська область"),
                new Point(address: "Садово-Набережна вул., 6, Харків, Харківська область"),
                new Point(address: "2А, пл. Повстання, 2А, Харків, Харківська область")
                /*new Point(address: "a"),
                new Point(address: "b"),
                new Point(address: "c"),
                new Point(address: "d"),
                new Point(address: "e")*/
        )
        Minimum target = new Minimum()
        long cur = System.nanoTime()
        gs.goThroughString(target, new ArrayList<Point>(), arr)
        long end = System.nanoTime()
        println("Execution time ${(end - cur) / 1000000000}")
        print(gs.getDistByPoints(target.points))
    }


    @Test
    public void testReading() {
        def coordFile = new File("coordinates.csv")
        def distFile = new File("distances.csv")
        coordFile.text.eachLine { origin ->
            String[] coordinates = origin.split(",")
            coordFile.text.eachLine { dest ->
                //if (origin && dest) {
                    String[] destin = dest.split(",")
                    distFile << gs.getDistanceByLatAndLng(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]), Double.parseDouble(destin[0]), Double.parseDouble(destin[1])) * 2 + ","
               // }
            }
            distFile<<"\n"
        }
    }

    @Test
    public void countDist() {
        def distances = new File("distances.csv")
        def times = new File("times.csv")
        distances.text.eachLine { origin ->
            String[] originCoordinates = origin.split(",")
            originCoordinates.each {
                double dist = Double.parseDouble(it)
                times << "${BigDecimal.valueOf(dist/speed*60).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue()},"
            }
            times << "\n"
        }
    }
}
