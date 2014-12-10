import org.junit.Test

/**
 * Created by Александр on 10.11.2014.
 */

class GenericAlgorithmTest {
    public int speed = 30
    GenericAlgorithm gs = new GenericAlgorithm()
    ClusterService cs = new ClusterService()

    @Test
    public void testGoThroughTheString() {
        List<Route> population = new ArrayList<>()
        List<Address> addresses = gs.readAddresses(new File("addresses.txt"), new File("coordinates.csv"))
        long cur = System.nanoTime()
        gs.generatePopulation(population, new ArrayList<Address>(), addresses)
        long end = System.nanoTime()
        println("Execution time ${(end - cur) / 1000000000}")
        /*population.each {
            println(it.distance)
        }*/
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
            distFile << "\n"
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
                times << "${BigDecimal.valueOf(dist / speed * 60).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue()},"
            }
            times << "\n"
        }
    }

    @Test
    public void testCreateClusters() {
        gs.calculate()
    }
}
