/**
 * Created by Александр on 27.11.2014.
 */
class Route implements Comparable<Route> {
    double distance
    List<Address> addresses = []

    @Override
    int compareTo(Route o) {
        if (distance > o.distance)
            return 1
        if (distance < o.distance)
            return -1
        if (distance == distance)
            return 0
    }

    @Override
    public String toString() {
        return "Route{" +
                "distance=" + distance +
                ", addresses=" + addresses +
                '}';
    }
}
