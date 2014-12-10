import static java.lang.Math.*

/**
 * Created by Александр on 10.11.2014.
 */
class GenericAlgorithm {
    private static final double EARTH_RADIUS = 6371; // Радиус Земли
    // public static final String GOOGLE_API = "http://maps.googleapis.com"
    public static final int POPULATION_SIZE = 1000;
    public static final double ELITE_RATE = 0.1;
    public static final double SURVIVE_RATE = 0.5;
    //public static final double MUTATION_RATE = 0.2;
    private static final int MAX_ITER = 1000;


    void generatePopulation(List<Route> result, List<Address> start, List<Address> end) {
        if (end.size() <= 1) {
            start.addAll(end)
            result.add(new Route(distance: getDistByPoints(start), addresses: start))
        } else {
            if (result.size() < 1000) {
                for (int i = 0; i < end.size(); i++) {
                    List<Address> newArray = new ArrayList<>(end.size() - 1);
                    List<Address> begin = new ArrayList<>(end.size() + 1);
                    begin.addAll(start)
                    begin.add(end.get(i))
                    newArray.addAll(end)
                    newArray.remove(end.get(i))
                    generatePopulation(result, begin, newArray);
                }
            }
        }
    }

    List<Route> mate(List<Route> population) {
        int esize = (int) (POPULATION_SIZE * ELITE_RATE);
        List<Route> children = new ArrayList<Route>();

        selectElite(population, children, esize);

        for (int i = esize; i < POPULATION_SIZE; i++) {
            int i1 = (int) (Math.random() * POPULATION_SIZE * SURVIVE_RATE);
            int i2 = (int) (Math.random() * POPULATION_SIZE * SURVIVE_RATE);

            Route child = generateChild(population.get(i1), population.get(i2))
            mutate(child);
            children.add(child);
        }
        return children;
    }


    Route generateChild(Route r1, Route r2) {
        List<Address> addresses = new ArrayList<>(r1.addresses.size())
        r1.addresses.eachWithIndex { addr, index ->
            if (r2.addresses.contains(addr)) {
                addresses.add(addr)
            } else if (index % 2 == 0) {
                addresses.add(addr)
            } else if (index % 2 != 0) {
                addresses.add(r2.addresses.get(index))
            } else {
                addresses.add(addr)
            }
        }
        return new Route(distance: getDistByPoints(addresses), addresses: addresses)
    }

    public void calculate() {
        List<Route> population = new ArrayList<Route>();
        List<Address> addresses = readAddresses(new File("addresses.txt"), new File("coordinates.csv"))
        generatePopulation(population, new ArrayList<Address>(), addresses);

        for (int i = 0; i < MAX_ITER; i++) {
            Collections.sort(population);
            println(i + " > " + population.get(0));
            population = mate(population);
        }

    }

    void mutate(Route input) {
        List<Address> addrs = input.addresses
        int ipos = (int) (Math.random() * addrs.size());
        Address temp = addrs.get(ipos)
        addrs.remove(ipos)
        addrs.add(temp)
    }


    private void selectElite(List<Route> population, List<Route> children, int esize) {
        for (int i = 0; i < esize; i++) {
            children.add(population.get(i));
        }
    }

    double getDistByPoints(List<Address> result) {
        double resultDist = 0;
        for (int i = 0; i < result.size() - 1; i++) {
            resultDist += getDistanceByLatAndLng(result.get(i).lat, result.get(i).lng, result.get(i + 1).lat, result.get(i + 1).lng)
        }
        return resultDist
    }

    /* def generateDistance(String origin, String destination) {
         def http = new HTTPBuilder(GOOGLE_API)
         double result = 0
         http.request(GET, JSON) {
             uri.path = '/maps/api/distancematrix/json'
             uri.query = [sensor: false, origins: origin, destinations: destination, mode: 'driving']

             // response handler for a success response code:
             response.success = { resp, json ->
                 assert resp.statusLine.statusCode == 200
                 String dist = json.rows.elements['distance'].text[0][0]
                 //result = Double.parseDouble(dist.replaceAll("( km)|( m)", ""))
                 return dist
             }

             // handler for any failure status code:
             response.failure = { resp ->
                 println("Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}")
             }
             return result
         }
     }*/

    public List<Address> readAddresses(File addresses, File coordinates) {
        List<Address> result = new ArrayList<>()
        addresses.eachLine { line, number ->
            if (number < 20) {
                String[] comps = line.split(",")
                result.add(new Address(name: comps[0] + comps[1]))
            }
        }
        coordinates.eachLine { line, number ->
            if (number < 20) {
                String[] comps = line.split(",")
                result.get(number - 1).lat = Double.valueOf(comps[0])
                result.get(number - 1).lng = Double.valueOf(comps[1])
            }
        }
        result
    }

    double getDistanceByLatAndLng(double originLat, double originLng, double destLat, double destLng) {
        final double dlng = deg2rad(originLng - destLng);
        final double dlat = deg2rad(originLat - destLat);
        final double a = sin(dlat / 2) * sin(dlat / 2) + cos(deg2rad(destLat)) * cos(deg2rad(originLat)) * sin(dlng / 2) * sin(dlng / 2);
        return BigDecimal.valueOf(2 * atan2(sqrt(a), sqrt(1 - a)) * EARTH_RADIUS).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    private static double deg2rad(final double degree) {
        return degree * (Math.PI / 180);
    }

}
