import org.junit.Test

/**
 * Created by Александр on 10.11.2014.
 */

class GoogleServiceTest {
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
}
