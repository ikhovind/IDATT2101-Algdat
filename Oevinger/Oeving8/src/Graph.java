
import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Graph {
    private LinkedEdges edges;
    private int noNodes;
    private Node[] nodes;

    static ArrayList<Node> shortestDijkstra;
    static ArrayList<Node> shortestAStar;
    static ArrayList<Node>[] pathsToCodes;

    public Graph(File edgeFile, File nodeFile, File typeFile) throws IOException {
        System.out.println("Innlesing av fil starter...");
        BufferedReader nodeReader = new BufferedReader(new FileReader(nodeFile.getPath()));
        BufferedReader edgeReader = new BufferedReader(new FileReader(edgeFile.getPath()));
        BufferedReader typeReader = new BufferedReader(new FileReader(typeFile.getPath()));

        noNodes = Integer.parseInt(nodeReader.readLine().trim());
        edgeReader.readLine();

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
            //Hopp over innledende blanke, finn starten på ordet
            splitLine(line, 3 , edgeTokens);
            nodes[Integer.parseInt(edgeTokens[0])] = new Node(Integer.parseInt(edgeTokens[0]),
            Double.parseDouble(edgeTokens[1]),
            Double.parseDouble(edgeTokens[2]));
        }
        nodeReader.close();

        typeReader.readLine();
        //leser type noder fra fil
        while((line = typeReader.readLine()) != null){
            //Hopp over innledende blanke, finn starten på ordetwhile
            splitLine(line, 2 , edgeTokens);
            nodes[Integer.parseInt(edgeTokens[0])].type = Integer.parseInt(edgeTokens[1]);
        }
        nodeReader.close();

        System.out.println("Innlesing av fil ferdig...");
    }

    private void splitLine(String line, int numWords, String[] results) {
        int j = 0;
        int lengde = line.length();
        for (int i = 0; i < numWords; ++i) {
            while (line.charAt(j) <= ' ') ++j;
            int ordstart = j;//Finn slutten på ordet, hopp over ikke-blanke
            while (j < lengde && line.charAt(j) > ' ') ++j;
            results[i] = line.substring(ordstart, j);
        }
    }

    public ArrayList<Node>[] dijkstraToStation(int start, int typeGoal, int numToFind){
        resetNodes();
        System.out.println("Dijkstra begynner søk etter nærmeste bensin/ladestasjoner");
        long startTime = System.currentTimeMillis();
        int otherType = 6-typeGoal;
        int leftToFind = numToFind;
        ArrayList<Node>[] results = new ArrayList[numToFind];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        nodes[start].distTo = 0;
        pq.add(nodes[start]);
        int counter = 0;
        Node current;
        //mens det finnes kanter fra den nåværende noden til andre noder
        while(!pq.isEmpty()) {
            counter++;
            current = pq.poll();
            //sjekker om det er noen nye noder/kortere veier og oppdaterer distansene
            checkConnectedNodes(pq, current, null);
            //sjekker om noden vi er i nå er passende type
            if((current.type == typeGoal || 6 - current.type == otherType) && !current.found){
                current.found = true;
                //legger til den lenkede listen med dette resultatet i arrayet vårt
                results[numToFind - leftToFind] = shortestPathLinkedList(start, current.index);
                leftToFind--;
            }
            //dersom vi har funnet alle
            if(leftToFind == 0){
                System.out.println("Dijkstra fant " + numToFind + " stasjoner, brukte " + (System.currentTimeMillis() - startTime) + " Millisekunder");
                System.out.println("Behandlet " + counter + " noder");
                return results;
            }
        }
        return results;
    }

    /**
     * denne brukes i alle metodene til å sjekke kantene til en gitt node
     * sjekker om en gitt node har kanter til noder som ikke er funnet, eller om de har kortere
     * veier til noen kortere noder
     *
     * @param goal er den estimerte verdien fra noden current til målet, i dijkstra settes
     *                  0, i A* så brukes denne verdien
     */
    private void checkConnectedNodes(PriorityQueue<Node> pq, Node current, Node goal) {
        if(edges.getHead(current.index) != null)  {
            for (Edge edge : edges.getHead(current.index)) {
                Node nextNode = nodes[edge.getTo()];
                //dersom noden ikke har blitt funnet før
                if (nextNode.distTo == Integer.MAX_VALUE / 2) {
                    //find distance returnerer 0 dersom goal er null
                    nextNode.distTo = current.distToTime + edge.getWeight() + findDistance(nextNode, goal);
                    nextNode.distToTime = current.distToTime + edge.getWeight();
                    pq.add(nextNode);
                    nextNode.pastNode = current.index;
                }
                //dersom noden vi er i nå har en kortere vei til noden som kanten peker på
                else if (current.distToTime + edge.getWeight() < nextNode.distToTime) {
                    nextNode.distTo = current.distToTime + edge.getWeight()  + findDistance(nextNode,
                        goal);
                    nextNode.distToTime = current.distToTime + edge.getWeight();
                    nextNode.pastNode = current.index;
                    //oppdaterer nodene slik i tilfelle noden som vi akkurat oppdaterte nå skal
                    // ligge øverst
                    Node top = pq.poll();
                    if(top != null) pq.add(top);
                }
            }
        }
    }

    private void resetNodes(){
        for (Node node : nodes) {
            node.distTo = Integer.MAX_VALUE / 2;
        }
    }

    public ArrayList<Node> shortestPath(int start, int goal, boolean useAStar){
        resetNodes();
        //noden som vi bruker til å estimere reisetid, med null så blir ikke denne estimert
        Node goalEstimator = (useAStar) ? nodes[goal] : null;

        System.out.println(((useAStar) ? "A*" : "Dijkstra") +" leter etter korteste vei ");

        long startTime = System.currentTimeMillis();

        PriorityQueue<Node> pq = new PriorityQueue<>();
        nodes[start].distTo = 0;
        nodes[start].distToTime = 0;
        pq.add(nodes[start]);
        Node current;
        //mens det finnes kanter fra den nåværende noden til andre noder
        int counter = 0;
        while(!pq.isEmpty()) {
            current = pq.poll();
            counter++;
            if(current.index == goal){
                System.out.println(((useAStar) ? "A*" : "Dijkstra") + " fant korteste vei " +
                    "behandlet " + counter + " noder");
                //gjør om nanosekunder til millisekunder
                System.out.println("Brukte: " + (System.currentTimeMillis() - startTime) + " " +
                    "millisekunder" );
                return shortestPathLinkedList(start,goal);
            }
            checkConnectedNodes(pq, current, goalEstimator);
        }
        System.out.println("Fant ingen vei :(");
        return null;
    }

    /**
     * lager en lenket liste med korteste vei ved å bruke node-arrayet
     */
    private ArrayList<Node> shortestPathLinkedList(int start, int to){
        Node current = nodes[to];
        ArrayList<Node> result = new ArrayList<>();
        result.add(current);
        while(current.index != start){
            current = nodes[current.pastNode];
            Node newNode = new Node(current.index,current.lat,current.longitude);
            newNode.distTo = current.distTo;
            newNode.distToTime = current.distToTime;
            result.add(newNode);
        }
        return result;
    }

    private int findDistance(Node currentNode, Node goal){
        if(goal == null) return 0;
        double currentBreddegrad = currentNode.lat * Math.PI / 180;
        double currentLengdegrad = currentNode.longitude * Math.PI / 180;

        double goalBreddegrad = goal.lat * Math.PI / 180;
        double goalLengdegrad = goal.longitude * Math.PI / 180;

        int EARTH_RADIUS = 6371;

        double distance = 2 * EARTH_RADIUS *
            Math.asin(
                Math.sqrt(
                    (Math.sin( ((currentBreddegrad - goalBreddegrad) / 2 )
                    * Math.sin( (currentBreddegrad - goalBreddegrad) / 2 ))

                    + ( ( Math.cos(currentBreddegrad) )
                    * ( Math.cos(goalBreddegrad) )
                    * ( Math.sin( (currentLengdegrad - goalLengdegrad) / 2 ) )
                    * ( Math.sin( (currentLengdegrad - goalLengdegrad) / 2 ) )
                    )
                )
            ));

        return (int) (distance * 360000/130);
    }

    public static void main(String[] args) throws IOException {
        int fromNode = Integer.parseInt(args[0]);
        int toNode = Integer.parseInt(args[1]);
        int stationNode = Integer.parseInt(args[2]);
        int stationType = Integer.parseInt(args[3]);
        int numStations = Integer.parseInt(args[4]);

        shortestDijkstra = new ArrayList<>();
        shortestAStar = new ArrayList<>();

<<<<<<< HEAD
        Graph g = new Graph(new File("files/skandinavia/kanter.txt"), new File("files/skandinavia/noder.txt"), new File(
=======
        Graph g = new Graph(new File("files/skandinavia/kanter.txt"),
            new File("files/skandinavia/noder.txt"), new File(
>>>>>>> 5a9de8e57835076282b750d32d153519ff038264
            "files/skandinavia/interessepkt.txt"));

        shortestDijkstra = g.shortestPath(fromNode, toNode, false);
        System.out.println("Total reisetid med dijkstra er " + (shortestDijkstra.get(0).distToTime));

        shortestAStar = g.shortestPath(fromNode, toNode, true);
        System.out.println("Total reisetid med A* er " + (shortestAStar.get(0).distToTime/100)/3600 +
            " Timer, " + ((((shortestAStar.get(0).distToTime)/100)%3600)/60) + " Minutter " +
            "og " + ((shortestAStar.get(0).distToTime/100)%60) + " sekunder");

        pathsToCodes = g.dijkstraToStation(stationNode, stationType, numStations);
        DemoApp.main(args);
    }
}




