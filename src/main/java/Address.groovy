/**
 * Created by Александр on 08.12.2014.
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
}
