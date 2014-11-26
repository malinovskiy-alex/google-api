import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * Created by Александр on 10.11.2014.
 */
class GoogleService {
    public static final String GOOGLE_API = "http://maps.googleapis.com"

    void goThroughString(Minimum result, List<Point> start, List<Point> end) {
        if (end.size() <= 1) {
            start.addAll(end)
            if (!result.points) {
                result.points.addAll(start)
                result.distance = getDistByPoints(start)
            } else {
                double curDist = getDistByPoints(start)
                if (result.distance > curDist) {
                    result.points.clear()
                    result.points.addAll(start)
                    result.distance = curDist
                }
            }

        } else
            for (int i = 0; i < end.size(); i++) {
                List<Point> newArray = new ArrayList<>(end.size() - 1);
                List<Point> begin = new ArrayList<>(end.size() + 1);
                begin.addAll(start)
                begin.add(end.get(i))
                newArray.addAll(end)
                newArray.remove(end.get(i))
                goThroughString(result, begin, newArray);
            }
    }

    double getDistByPoints(List<Point> result) {
        double resultDist = 0;
        for (int i = 0; i < result.size() - 1; i++) {
            resultDist += generateDistance(result[i].address, result[i + 1].address)
        }
        return resultDist
    }

    double generateDistance(String origin, String destination) {
        def http = new HTTPBuilder(GOOGLE_API)
        double result = 0
        http.request(GET, JSON) {
            uri.path = '/maps/api/distancematrix/json'
            uri.query = [sensor: false, origins: origin, destinations: destination, mode: 'driving']

            // response handler for a success response code:
            response.success = { resp, json ->
                assert resp.statusLine.statusCode == 200
                String dist = json.rows.elements['distance'].text[0][0]
                result = Double.parseDouble(dist.replaceAll(" km", ""))
            }

            // handler for any failure status code:
            response.failure = { resp ->
                println("Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}")
            }
            return result
        }
    }
}
