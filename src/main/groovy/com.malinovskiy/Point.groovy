/**
 * Created by malinovsky on 11/26/2014.
 */
class Point {
    String address

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Point point = (Point) o

        if (address != point.address) return false

        return true
    }

    int hashCode() {
        return (address != null ? address.hashCode() : 0)
    }

    @Override
    public String toString() {
        return "Point{" +
                "address='" + address + '\'' +
                '}';
    }
}
