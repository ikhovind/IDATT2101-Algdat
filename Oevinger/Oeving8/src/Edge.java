import java.util.Iterator;
import java.util.function.Consumer;
import org.w3c.dom.ls.LSOutput;

class Edge implements Iterable<Edge> {
    private int from;
    private int to;
    private int weight;
    private Edge next;

    public Edge(int from, int to, int weight, Edge next1) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        next = next1;
    }

    public int getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    public Edge getNext() {
        return next;
    }

    public void setNext(Edge next) {
        this.next = next;
    }

    @Override
    public Iterator<Edge> iterator() {
        return new EdgeIterator(this);
    }


    class EdgeIterator implements Iterator<Edge>{
        Edge current;
        public EdgeIterator(Edge edge) {
            current = edge;
        }
        @Override
        public boolean hasNext() {
            return current.getNext() != null;
        }

        @Override
        public Edge next() {
            Edge temp = current;
            current = current.getNext();
            return temp;
        }

    }
}
