import com.sothawo.mapjfx.Coordinate;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Graph {
    private LinkedEdges edges;
    private int noNodes;
    private int noEdges;
    private Node[] nodes;
    static ArrayList<Coordinate> result;
    public Graph(File edgeFile, File nodeFile) throws IOException {
        System.out.println("Innlesing av fil starter...");
        BufferedReader nodeReader = new BufferedReader(new FileReader(nodeFile.getPath()));
        BufferedReader edgeReader = new BufferedReader(new FileReader(edgeFile.getPath()));

        noNodes = Integer.parseInt(nodeReader.readLine().trim());
        noEdges = Integer.parseInt(edgeReader.readLine().trim());

        nodes = new Node[noNodes];
        edges = new LinkedEdges(noNodes);

        String[] edgeTokens = new String[10];

        String line;
        //leser kantene fra fil
        while((line = edgeReader.readLine()) != null){
            splitLine(line, 3, edgeTokens);
            edges.pushBack(Integer.parseInt(edgeTokens[0]),
                new Edge(Integer.parseInt(edgeTokens[0]), Integer.parseInt(edgeTokens[1]),
                    Integer.parseInt(edgeTokens[2]),
                null));
        }
        edgeReader.close();


        //leser nodene fra fil
        while((line = nodeReader.readLine()) != null){

            //Hopp over innledende blanke, finn starten på ordetwhile
            splitLine(line, 3 , edgeTokens);
            nodes[Integer.parseInt(edgeTokens[0])] = new Node(Integer.parseInt(edgeTokens[0]),
            Double.parseDouble(edgeTokens[1]),
            Double.parseDouble(edgeTokens[2]));

        }
        nodeReader.close();

        System.out.println("Innlesing av fil ferdig...");
    }

    private void splitLine(String line, int numWords, String[] results){
        int j = 0;
        int lengde = line.length();
        for (int i = 0; i < numWords; ++i) {
            while (line.charAt(j) <= ' ') ++j;
            int ordstart = j;//Finn slutten på ordet, hopp over ikke-blanke
            while (j < lengde && line.charAt(j) > ' ') ++j;
            results[i] = line.substring(ordstart, j);
        }
    }

    public LinkedList<Node> dijkstraShortestPath(int start, int goal){
        System.out.println("Dijkstra starter");
        long startTime = System.currentTimeMillis();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        nodes[start].distTo = 0;
        pq.add(nodes[start]);
        Node current;
        //mens det finnes kanter fra den nåværende noden til andre noder
        int counter = 0;
        while(!pq.isEmpty()) {
            current = pq.poll();
            counter++;
            if(current.index == goal){
                System.out.println("Dijkstra ferdig");
                System.out.println("dijkstra behandlet: " + counter + " noder");
                //gjør om nanosekunder til millisekunder
                System.out.println("dijkstra brukte: " + (System.currentTimeMillis() - startTime) + " millisekunder" );
                return shortestPathLinkedList(start,goal);
            }
            if(edges.getHead(current.index) != null)  {
                for (Edge edge : edges.getHead(current.index)) {
                    //dersom noden ikke har blitt funnet før
                    if (nodes[edge.getTo()].distTo == Integer.MAX_VALUE / 2) {
                        nodes[edge.getTo()].distTo =
                                nodes[edge.getFrom()].distTo + edge.getWeight();
                        pq.add(nodes[edge.getTo()]);
                        nodes[edge.getTo()].pastNode = current.index;
                    }
                    //dersom noden vi er i nå har en kortere vei til noden som kanten peker på
                    else if (nodes[current.index].distTo + edge.getWeight() < nodes[edge.getTo()].distTo) {
                        //oppdaterer avstanden i distTo
                        nodes[edge.getTo()].distTo = nodes[current.index].distTo + edge.getWeight();
                        nodes[edge.getTo()].pastNode = current.index;
                    }
                }
            }
        }
        System.out.println("Dijkstra ferdig, fant ingen vei :(");
        return null;
    }

    /**
     * lager en lenket liste med korteste vei ved å bruke node-arrayet
     */
    private LinkedList<Node> shortestPathLinkedList(int start, int to){
        Node current = nodes[to];
        LinkedList<Node> result = new LinkedList<>();
        result.addFirst(current);
        while(current.index != start){
            current = nodes[current.pastNode];
            result.addFirst(current);
        }
        return result;
    }
    public void aStar(){

    }

    public static void main(String[] args) throws IOException {
        //todo dette er feil helsinki :)
        int helsinki = 4142089;
        int trondheim = 2399829;
        int oslo =  2353304;
        Graph g = new Graph(new File("files/skandinavia/kanter.txt"), new File("files/skandinavia" +
            "/noder.txt"));
        result = new ArrayList<>();
        for(Node n : g.dijkstraShortestPath(trondheim,helsinki)){
            Coordinate coord = new Coordinate(n.lat,n.longitude);
            result.add(coord);
        }
        DemoApp.main(args);
    }

}




