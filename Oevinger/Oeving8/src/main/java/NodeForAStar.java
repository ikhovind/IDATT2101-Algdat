public class NodeForAStar extends Node implements Comparable {
    double timeWithAirDistance;

    public NodeForAStar(int index, double lat, double longditude){
        super(index, lat, longditude);
    }

    @Override
    public int compareTo(Object o) {
        return (int)(this.timeWithAirDistance - ((NodeForAStar)o).timeWithAirDistance);
    }
}
