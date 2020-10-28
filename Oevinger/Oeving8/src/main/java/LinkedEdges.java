import java.util.ArrayList;

public class LinkedEdges {
    private Edge[] edges;

    public LinkedEdges(int noEdges) {
        edges = new Edge[noEdges];
    }

    public Edge[] getEdges() {
        return edges;
    }
<<<<<<< HEAD:Oevinger/Oeving8/src/main/java/LinkedEdges.java
=======
    public boolean hasEdges(int index){
        return edges[index] != null;
    }
>>>>>>> 1c18d0a4f77bf7bf7c1ddad513d5541816e01b0f:Oevinger/Oeving8/src/LinkedEdges.java
    /**
     * legger til en node bakerst i den lenka lista
     */
    public void pushBack(int index, Edge edge){
        Edge current = edges[index];
        if(current == null){
            edges[index] = edge;
            return;
        }
        while (current.getNext() != null){
            current = current.getNext();
        }
        current.setNext(edge);
    }

    public Edge getHead(int index){
        return edges[index];
    }
}
