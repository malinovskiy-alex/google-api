import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * Created by Александр on 10.11.2014.
 */
class GoogleService {
    public static final String GOOGLE_API = "http://maps.googleapis.com"

    void goThroughString(String begin, String end) {
        if (end.length() <= 1)
            System.out.println(begin + end);
        else
            for (int i = 0; i < end.length(); i++) {
                String newString = end.substring(0, i) + end.substring(i + 1);
                goThroughString(begin + end.charAt(i), newString);
            }
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
