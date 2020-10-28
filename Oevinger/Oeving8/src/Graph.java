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
            String[] edgeTokens = line.split(" +");
            nodes[Integer.parseInt(edgeTokens[0])] = new Node(Integer.parseInt(edgeTokens[0]),
            Double.parseDouble(edgeTokens[1]),
            Double.parseDouble(edgeTokens[2]));

        }
        nodeReader.close();

        System.out.println("Innlesing av fil ferdig...");

    }
    //todo få dijkstra til å slutte når den har finnet målet
    //todo mål tiden og antall noder behandlet
    //todo endre returtypen fra et node-array til en lenket liste eller noe annet som egner seg
    // for å presentere det grafisk senere
    public Node[] dijkstra(int start){
        PriorityQueue<Node> pq = new PriorityQueue<>();
        nodes[start].distTo = 0;
        pq.add(nodes[start]);
        Node current;
        //mens det finnes kanter fra den nåværende noden til andre noder
        while(edges.hasEdges((current = pq.poll()).index)) {
            for (Edge edge : edges.getEdges()[current.index]) {
                //dersom noden ikke har blitt funnet før
                if (nodes[edge.getTo()].distTo == Integer.MAX_VALUE/2) {
                    nodes[edge.getTo()].distTo =
                        nodes[edge.getFrom()].distTo + edge.getWeight();
                    pq.add(nodes[edge.getTo()]);
                    nodes[edge.getTo()].pastNode = current.index;
                }
                //dersom noden vi er i nå har en kortere vei til noden som kanten peker på
                else if(nodes[current.index].distTo + edge.getWeight() < nodes[edge.getTo()].distTo){
                    //oppdaterer avstanden i distTo
                    nodes[edge.getTo()].distTo = nodes[current.index].distTo + edge.getWeight();
                    nodes[edge.getTo()].pastNode = current.index;
                }
            }
        }
        return nodes;
    }
    public void aStar(){

    }

    public static void main(String[] args) throws IOException {
        Graph g = new Graph(new File("files/skandinavia/kanter.txt"), new File("files/skandinavia" +
            "/noder" +
            ".txt"));
        Node[] result = g.dijkstra(30695);
        for (int i = 0; i < result.length; i++) {
            Node node = result[i];
            if (node.distTo != Integer.MAX_VALUE / 2) {
                System.out.println(node.index + "  " + node.pastNode + "  " + node.distTo);
            }
        }
    }
}




