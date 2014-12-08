/**
 * Created by �?лек�?андр on 08.12.2014.
 */
class Address {
    String name
    double lat
    double lng

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Address address = (Address) o

        if (Double.compare(address.lat, lat) != 0) return false
        if (Double.compare(address.lng, lng) != 0) return false
        if (name != address.name) return false

        return true
    }

    int hashCode() {
        int result
        long temp
        result = (name != null ? name.hashCode() : 0)
        temp = lat != +0.0d ? Double.doubleToLongBits(lat) : 0L
        result = 31 * result + (int) (temp ^ (temp >>> 32))
        temp = lng != +0.0d ? Double.doubleToLongBits(lng) : 0L
        result = 31 * result + (int) (temp ^ (temp >>> 32))
        return result
    }
}
