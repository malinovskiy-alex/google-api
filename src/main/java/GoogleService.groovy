import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static java.lang.Math.*

/**
 * Created by Александр on 10.11.2014.
 */
class GoogleService {
    private static final double EARTH_RADIUS = 6371; // Радиус Земли
    public static final String GOOGLE_API = "http://maps.googleapis.com"

    void goThroughString(Route result, List<Address> start, List<Address> end) {
        if (end.size() <= 1) {
            start.addAll(end)
            if (!result.addresses) {
                result.addresses.addAll(start)
                result.distance = getDistByPoints(start)
            } else {
                double curDist = getDistByPoints(start)
                if (result.distance > curDist) {
                    result.addresses.clear()
                    result.addresses.addAll(start)
                    result.distance = curDist
                }
            }
        } else {
            for (int i = 0; i < end.size(); i++) {
                List<Address> newArray = new ArrayList<>(end.size() - 1);
                List<Address> begin = new ArrayList<>(end.size() + 1);
                begin.addAll(start)
                begin.add(end.get(i))
                newArray.addAll(end)
                if (result.distance == 0 || result.distance > getDistByPoints(begin)) {
                    newArray.remove(end.get(i))
                    goThroughString(result, begin, newArray);
                }
            }
        }
    }


    double getDistByPoints(List<Address> result) {
        double resultDist = 0;
        for (int i = 0; i < result.size() - 1; i++) {
            resultDist += getDistanceByLatAndLng(result.get(i).lat, result.get(i).lng, result.get(i + 1).lat, result.get(i + 1).lng)
        }
        return resultDist
    }

    def generateDistance(String origin, String destination) {
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
    }

    public List<Address> readAddresses(File addresses, File coordinates) {
        List<Address> result = new ArrayList<>()
        addresses.eachLine { line, number ->
            if (number < 15) {
                String[] comps = line.split(",")
                result.add(new Address(name: comps[0] + comps[1]))
            }
        }
        coordinates.eachLine { line, number ->
            if (number < 10) {
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
