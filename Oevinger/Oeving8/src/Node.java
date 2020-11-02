public class Node implements Comparable{
    int pastNode;
    int index;
    int distTo = Integer.MAX_VALUE/2;
    double lat;
    double longitude;
    int type;
    boolean found = false;
    int distToTime = Integer.MAX_VALUE/2;

    public Node(int index, double lat, double longitude) {
        this.index = index;
        this.lat = lat;
        this.longitude = longitude;
    }

    @Override
    public int compareTo(Object o) {
       return this.distTo - ((Node) o).distTo;
    }
}
