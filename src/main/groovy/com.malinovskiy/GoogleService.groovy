import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

/**
 * Created by Александр on 10.11.2014.
 */
class GoogleService {
    public static final String GOOGLE_API = "http://maps.googleapis.com"

    void goThroughString(int[] start, int[] end) {
        if (end.length <= 1)
            showArray(start, end)
        else
            for (int i = 0; i < end.length; i++) {
                int[] newArray = new int[end.length - 1];
                int[] begin = new int[start.length + 1];
                System.arraycopy(start, 0, begin, 0, start.length);
                begin[begin.length - 1] = end[i]
                System.arraycopy(end, 0, newArray, 0, i)
                System.arraycopy(end, i + 1, newArray, i, newArray.length - i)
                //String newString = end.substring(0, i) + end.substring(i + 1);
                goThroughString(begin, newArray);
            }
    }

    void showArray(int[] start, int[] end) {
        int[] result = new int[start.length + end.length]
        System.arraycopy(start, 0, result, 0, start.length)
        System.arraycopy(end, 0, result, start.length, end.length)
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]+",")
        }
        System.out.println()
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
