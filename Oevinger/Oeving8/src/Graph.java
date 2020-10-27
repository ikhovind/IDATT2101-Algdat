import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Graph {
    private LinkedEdges edges;
    private int noNodes;
    private int noEdges;
    private Node[] nodes;
    public Graph(File edgeFile, File nodeFile) throws IOException {
        System.out.println("Innlesing av fil starter...");
        BufferedReader nodeReader = new BufferedReader(new FileReader(nodeFile.getPath()));
        noNodes = Integer.parseInt(nodeReader.readLine().trim());

        nodes = new Node[noNodes];
        edges = new LinkedEdges(noNodes);

        BufferedReader edgeReader = new BufferedReader(new FileReader(edgeFile.getPath()));
        noEdges = Integer.parseInt(edgeReader.readLine().trim());
        String line;
        //leser kantene fra fil
        while((line = edgeReader.readLine()) != null){
            String[] edgeTokens = line.split("\t");
            edges.pushBack(Integer.parseInt(edgeTokens[0]),
                new Edge(Integer.parseInt(edgeTokens[0]), Integer.parseInt(edgeTokens[1]),
                    Integer.parseInt(edgeTokens[2]),
                null));
        }
        edgeReader.close();

        //leser nodene fra fil
        while((line = nodeReader.readLine()) != null){
            String[] edgeTokens = line.split(" ");
            nodes[Integer.parseInt(edgeTokens[0])] = new Node(Integer.parseInt(edgeTokens[0]),
                Double.parseDouble(edgeTokens[1]),
                Double.parseDouble(edgeTokens[2]));
        }
        nodeReader.close();

        System.out.println("Innlesing av fil ferdig...");
        dijkstra(142, nodes);
    }

    public LinkedList<Edge> dijkstra(int start, Node[] results){
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Edge current = edges.getEdges()[start];
        do {
            for (Edge edge : current) {
                if (results[edge.getTo()].distTo == -1) {
                    System.out.println(":)");
                }
            }
            current = pq.poll();
        } while(!pq.isEmpty());
        return  null;
    }
    public void aStar(){

    }

    public static void main(String[] args) throws IOException {
        Graph g = new Graph(new File("files/island/kanter.txt"), new File("files/island/noder" +
            ".txt"));
    }
}

class Node{
    int pastNode;
    int index;
    int distTo = -1;
    double lat;
    double longitude;

    public Node(int index, double lat, double longitude) {
        this.index = index;
        this.lat = lat;
        this.longitude = longitude;
    }
}


