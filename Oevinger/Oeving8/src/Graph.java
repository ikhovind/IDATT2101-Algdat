import com.sothawo.mapjfx.Coordinate;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Graph {
    private LinkedEdges edges;
    private int noNodes;
    private int noEdges;
    private Node[] nodes;
    /**
     * Fordi at man regner med hele kilometer når man bruker haversine-formelen så må man bruke
     * desimaltall siden dette er en stor avstand
     * Haversine-formelen gir svar i kilometer, men man sorterer basert på estimert reisetid
     * Så etter at man har fått svaret fra haversine-formelen så kan man regne om dette i
     * reisetid ved å bruke 130-km som fartsgrense
     * denne reisetiden kan man gjerne lagre som en int
     */

    static ArrayList<Coordinate> result;
    static LinkedList<Coordinate>[] pathsToCodes;

    public Graph(File edgeFile, File nodeFile, File typeFile) throws IOException {
        System.out.println("Innlesing av fil starter...");
        BufferedReader nodeReader = new BufferedReader(new FileReader(nodeFile.getPath()));
        BufferedReader edgeReader = new BufferedReader(new FileReader(edgeFile.getPath()));
        BufferedReader typeReader = new BufferedReader(new FileReader(typeFile.getPath()));

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
            //Hopp over innledende blanke, finn starten på ordet while
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

        /*for(int i = 0; i < nodes.length; i ++){
            nodesForAStars[i] = (NodeForAStar)nodes[i];
        }*/

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

    public LinkedList<Node>[] dijkstraToStation(int start, int typeGoal, int numToFind){
        System.out.println("Dijkstra begynner søk etter nærmeste bensin/ladestasjoner");
        long startTime = System.currentTimeMillis();
        int otherType = 6-typeGoal;
        int leftToFind = numToFind;
        LinkedList<Node>[] results = new LinkedList[numToFind];
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
     * denne brukes i begge dijkstrametodene
     * sjekker om en gitt node har kanter til noder som ikke er funnet, eller om de har kortere
     * veier til noen kortere noder
     *
     * @param goal er den estimerte verdien fra noden current til målet, i dijkstra settes
     *                  0, i A* så brukes denne verdien
     */
    private void checkConnectedNodes(PriorityQueue<Node> pq, Node current, Node goal) {
        if(edges.getHead(current.index) != null)  {
            for (Edge edge : edges.getHead(current.index)) {
                //dersom noden ikke har blitt funnet før
                if (nodes[edge.getTo()].distTo == Integer.MAX_VALUE / 2) {
                    if(goal != null){
                        nodes[edge.getTo()].distTo = current.distToTime + edge.getWeight(); // + (int)((findDistance(nodes[edge.getTo()], goal)/130)*360000);
                        nodes[edge.getTo()].distToTime = current.distToTime + edge.getWeight();
                        /*nodes[edge.getTo()].distTo =
                                current.distTo + edge.getWeight() /*- (int)((findDistance(current, goal)/130)*360000) + (int)((findDistance(nodes[edge.getTo()], goal)/130)*360000)*/;
                    }else{
                        nodes[edge.getTo()].distTo =
                                current.distTo + edge.getWeight();
                    }

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
            //oppdaterer nodene
            Node top = pq.poll();
            pq.add(top);
        }
    }

    public void resetNodes(){
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].distTo = Integer.MAX_VALUE/2;
        }
    }

    public LinkedList<Node> dijkstraShortestPath(int start, int goal){
        System.out.println("Dijkstra leter etter korteste vei ");
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
                System.out.println("Dijkstra fant korteste vei, behandlet "+ counter + " noder");
                //gjør om nanosekunder til millisekunder
                System.out.println("dijkstra brukte: " + (System.currentTimeMillis() - startTime) + " millisekunder" );
                return shortestPathLinkedList(start,goal);
            }
            checkConnectedNodes(pq, current,null);
        }
        System.out.println("Dijkstra fant ingen vei :(");
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

    public LinkedList<Node> aStar(int start, int goal){
        System.out.println("A* starter med å finne korteste vei...");
        long startTime = System.currentTimeMillis();
        PriorityQueue<Node> pQueue = new PriorityQueue<>();
        nodes[start].distTo = (int)(findDistance(nodes[start], nodes[goal]));
        pQueue.add(nodes[start]);
        Node currentNode;
        int counter = 0;

        while (!pQueue.isEmpty()){
            currentNode = pQueue.poll();
            counter++;
            if(currentNode.index == goal){
                System.out.println("A* fant korteste vei, behandlet " + counter + " noder");
                System.out.println("A* brukte: " + (System.currentTimeMillis() - startTime) + " milliseksunder");
                return shortestPathLinkedList(start, goal);
            }
            checkConnectedNodes(pQueue, currentNode, nodes[goal]);
        }
        System.out.println("A* fant ingen veier :(");
        return null;
    }

    private double findDistance(Node currentNode, Node goal){
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

        return distance;
    }

    public static void main(String[] args) throws IOException {
        int helsinki = 4142089;
        int trondheim = 2399829;
        int oslo =  2353304;
        int trondheimLufthavn =  6198111;
        int rorosHotell = 1117256;
        int kaarvaag = 6013683;
        int gjemnes = 6225195;
        int helsinki2 = 1221382;

        Graph g = new Graph(new File("files/skandinavia/kanter.txt"), new File("files/skandinavia" +
            "/noder.txt"), new File("files/skandinavia/interessepkt.txt"));
        result = new ArrayList<>();
        System.out.println("avstand " + g.findDistance(g.nodes[trondheim],g.nodes[helsinki2]));
        //LinkedList<Node> path = g.dijkstraShortestPath(trondheim, helsinki2);
        LinkedList<Node> path = g.aStar(trondheim, oslo);
        System.out.println("Total reisetid med A* er " + ((float)path.getLast().distTo)/360000 + " timer");

        g.resetNodes();
        g.dijkstraShortestPath(trondheim,helsinki2);


        for(Node n : path){
            Coordinate coord = new Coordinate(n.lat,n.longitude);
            result.add(coord);
        }


        g.resetNodes();

        LinkedList<Node>[] arrayOfNodesToStations = g.dijkstraToStation(rorosHotell,4,10);

        //konverterer fra Noder til et array med lenkede lister av koordinater
        pathsToCodes = new LinkedList[arrayOfNodesToStations.length];
        //går gjennom hver av de linkede listene med korteste vei
        for (int i = 0; i < arrayOfNodesToStations.length; i++) {
            //arrayet vårt med lenkede koordinater
            pathsToCodes[i] = new LinkedList<Coordinate>();
            //korteste vei til én enkelt stasjon
            LinkedList<Node> nodesToSingleStation = arrayOfNodesToStations[i];
            //Går gjennom veien til denne stasjonen
            for (int j = 0; j < nodesToSingleStation.size(); j++) {
                Node node = nodesToSingleStation.get(j);
                Coordinate coordinate = new Coordinate(node.lat,node.longitude);
                //legger til koordinatene i arrayet vårt
                pathsToCodes[i].add(coordinate);
            }
        }



        DemoApp.main(args);
    }

}




