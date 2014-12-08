/**
 * Created by malinovsky on 12/8/14.
 */
class Cluster {
    double prevLat
    double prevLng
    double curLat
    double curLng
    List<Address> addresses = new ArrayList<>()

    @Override
    public String toString() {
        return "Cluster{" +
                "prevLat=" + prevLat +
                ", prevLng=" + prevLng +
                ", curLat=" + curLat +
                ", curLng=" + curLng +
                ", addresses=" + addresses +
                '}';
    }
}
