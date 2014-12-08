/**
 * Created by malinovsky on 12/5/14.
 */
class ClusterService {
/**
 *
 * @param addresses
 * @param k Clusters count
 */
    List<Cluster> initializeClusters(List<Address> addresses, int k) {
        List<Address> clusterAddresses = new ArrayList<>()
        List<Cluster> result = new ArrayList<>()
        (1..k).each {
            int n = (int) Math.random() * addresses.size() - 1
            Address a = addresses.get(n)
            while (clusterAddresses.contains(a)) {
                n = (int) Math.random() * addresses.size() - 1
                a = addresses.get(n)
            }
            clusterAddresses.add(a)
            result.add(new Cluster(curLat: a.lat, curLng: a.lng))
        }
        result
    }

    List<Cluster> generateClusters(List<Address> addresses, int k) {
        List<Cluster> initialClusters = initializeClusters(addresses, k)
        assignAddressesToClusters(initialClusters, addresses)
        while (!isEnd(initialClusters)) {
            recalculateCenters(initialClusters)
            assignAddressesToClusters(initialClusters, addresses)
        }
        initialClusters
    }

    boolean isEnd(List<Cluster> clusters) {
        boolean result = true
        clusters.each {
            if (it.curLat != it.prevLat || it.curLng != it.prevLng) {
                result = false
            }
        }
        return result
    }

    void recalculateCenters(List<Cluster> clusters) {
        clusters.each { cluster ->
            double newLat = getLatSum(cluster.addresses) / cluster.addresses.size()
            double newLng = getLngSum(cluster.addresses) / cluster.addresses.size()
            cluster.prevLat = cluster.curLat
            cluster.curLat = newLat
            cluster.prevLng = cluster.curLng
            cluster.curLng = newLng
            cluster.addresses = []
        }
    }

    double getLatSum(List<Address> addr) {
        double sum = 0
        addr.each { it ->
            sum += it.lat
        }
        sum
    }

    double getLngSum(List<Address> addr) {
        double sum = 0
        addr.each { it ->
            sum += it.lng
        }
        sum
    }

    void assignAddressesToClusters(List<Cluster> clusters, List<Address> addresses) {
        addresses.each { address ->
            Cluster min = getOptCluster(address, clusters)
            min.addresses.add(address)
        }
    }

    double getDistanceBetweenCoord(double origLat, double origLng, double destLat, double destLng) {
        return Math.sqrt(Math.pow(destLat - origLat, 2) + Math.pow(destLng - origLng, 2))
    }

    Cluster getOptCluster(Address a, List<Cluster> clusters) {
        Cluster minClust = clusters.get(0)
        double minDist = getDistanceBetweenCoord(a.lat, a.lng, minClust.curLat, minClust.curLng)
        for (int i = 1; i < clusters.size(); i++) {
            double dist = getDistanceBetweenCoord(a.lat, a.lng, clusters.get(i).curLat, clusters.get(i).curLng)
            if (dist < minDist) {
                minDist = dist
                minClust = clusters.get(i)
            }
        }
        minClust
    }
}
