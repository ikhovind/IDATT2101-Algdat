import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        File graph = new File("L7g6");
        LinkedListGraph linkedTable = new LinkedListGraph(new BufferedReader(new FileReader(graph)));
        //svar fra nabolisten
        linkedTable.getStronglyConnnected();
        for(Node n : linkedTable.nodes){
            System.out.println(n.toString());
        }
        Node.resetTime();
        System.out.println("-------------------------------------");

        GraphTable doubleArrayGraph = new GraphTable(new BufferedReader(new FileReader(graph)));

        HashMap<Integer, List<Node>> stronglyConnected = doubleArrayGraph.getStronglyConnnected();
        //svar fra nabotabellen
        String answer = "Komponent   :   Noder i Komponenten\n";
        int components = 0;
        for (Map.Entry<Integer, List<Node>> set : stronglyConnected.entrySet()) {
            answer += set.getKey() + " : ";
            components++;
            for(Node n : set.getValue()){
                answer += n.index + " ";
            }
            answer+="\n";
        }
        System.out.println("Grafen har " + components + " sterkt sammenhengende komponenter");
        System.out.println(answer);
    }

}



class Edge{
    Edge nextEdge = null;
    Node to = null;
    boolean exist = false;

    public Edge(){
    }
    public Edge(Node n, Edge nextEdge){
        to = n;
        this.nextEdge = nextEdge;
    }
}
 class Node{
    static int inf = 1000000000;
    static int time = 0;
    int index;
    int foundTime;
    int finsishedTime;
    int dist = inf;
    Edge nextEdge = null;

    public Node(int index){
        this.index = index;
    }
    public void setFoundTime(){
        this.foundTime = ++time;
    }
    public static void resetTime(){
        time = 0;
    }
    public void setFinsishedTime(){
        this.finsishedTime = ++time;
    }
    public String toString(){
        return "Index: " + index + "\n"+
            "Found time: " + foundTime + "\n" +
            "Finished time: " + finsishedTime + "\n";
    }
    public boolean equals(Object o){
        if(o == this) return true;
        if(!(o instanceof Node)) return false;
        if(this.index == ((Node) o).index){
            return true;
        }
        return false;
    }
}

/**
 * Grafer som nabotabell, denne klarer ikke håndtere Skandinaviagrafen, da pcen min har for lite
 * ram til heapen som trengs :)
 * I verste-fall så blir det n^2 plass, som blir ca 22 gb med 142 mb hver side, i realiteten
 * lavere enn dette, da tabellen er litt fullere enn dette
 */
class GraphTable {
    int N;
    Edge edgeTable[][];
    Node nodes[];
    public GraphTable(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        edgeTable = new Edge[N][N];
        nodes = new Node[N];
        for(int i = 0; i < N; ++i){
            nodes[i] =  new Node(i);
            for(int j = 0; j < N; ++j){
                edgeTable[i][j] = new Edge();
            }
        }
        int K = Integer.parseInt(st.nextToken());
        for(int i = 0; i < K; i++){
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            edgeTable[from][to].exist = true;
        }
    }
    public GraphTable(Edge[][] edgeTable){
        this.edgeTable = edgeTable;
        nodes = new Node[edgeTable.length];
        for(int i = 0; i < edgeTable.length; i++){
            nodes[i] =  new Node(i);
        }
        N = nodes.length;
    }
    public String toString(){
        String toString = "";
        for(Edge[] row : edgeTable){
            for(Edge edge : row){
                if ((edge.exist)) {
                    toString += 1;
                } else {
                    toString += 0;
                }
            }
            toString += "\n";
        }
        return toString;
    }
    public Edge[][] transposeTable() {
        Edge[][] transposedEdges = new Edge[edgeTable.length][edgeTable[0].length];
        for(int i = 0; i < edgeTable.length; i++){
            for(int j = 0; j < edgeTable[i].length; j++){
                transposedEdges[j][i] = edgeTable[i][j];
            }
        }
        return transposedEdges;
    }
    public List<Node> depthFirst(int nodeIndex, int dist){
        ArrayList<Node> foundNodes = new ArrayList<>();
        for(int i = 0; i < edgeTable[nodeIndex].length; i++){
            if(nodes[nodeIndex].dist > 100000000){
                nodes[nodeIndex].setFoundTime();
                nodes[nodeIndex].dist = ++dist;
                foundNodes.add(nodes[nodeIndex]);
            }
            else if(edgeTable[nodeIndex][i].exist && nodes[i].dist == Node.inf){
                nodes[i].setFoundTime();
                nodes[i].dist = ++dist;
                foundNodes.add(nodes[i]);
                foundNodes.addAll(depthFirst(i, dist));
            }
            if(nodes[nodeIndex].finsishedTime == 0 && i == edgeTable[nodeIndex].length-1){
                nodes[nodeIndex].setFinsishedTime();
            }
        }
        return foundNodes;
    }
    private List<Node> sortByFoundTime() {
        ArrayList<Node> foundNodes = new ArrayList<>();
        for (int i = 0; i < edgeTable.length; i++) {
            for (Node n : depthFirst(i, 0)) {
                if (!foundNodes.contains(n)) {
                    foundNodes.add(n);
                }
            }
        }
        //sorterer i synkende rekkefølge
        foundNodes.sort(Comparator.comparingInt(o -> -o.finsishedTime));
        return foundNodes;
    }
    public HashMap<Integer, List<Node>> getStronglyConnnected(){
        HashMap<Integer, List<Node>> testingHash = new HashMap<>();
        List<Node> foundNodes = sortByFoundTime();
        GraphTable transposeTable = new GraphTable(transposeTable());
        int counter = 0;
        for(Node n : foundNodes){
            ArrayList<Node> tempList = new ArrayList<>();
            for(Node n2 : transposeTable.depthFirst(n.index,0)){
                if(!tempList.contains(n2)){
                    tempList.add(n2);
                }
            }
            if(!tempList.isEmpty()){
                testingHash.put(counter, tempList);
                counter++;
            }

        }
        return testingHash;
    }
}

class LinkedListGraph {
    Node[] nodes;
    int N;
    public LinkedListGraph(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        nodes = new Node[N];
        for(int i = 0; i < N; ++i){
            nodes[i] =  new Node(i);
        }
        //antall kanter
        int numEdges = Integer.parseInt(st.nextToken());
        for(int i = 0; i < numEdges; i++){
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            Edge edge = new Edge(nodes[to],nodes[from].nextEdge);
            nodes[from].nextEdge = edge;
        }
    }
    public LinkedListGraph(Node[] nodes){
        this.nodes = nodes;
        N = nodes.length;
    }
    public LinkedListGraph transposeTable(){
        Node[] transposedTable = new Node[nodes.length];
        Edge tempEdge;
        for(Node n : nodes){
            transposedTable[n.index] = new Node(n.index);
        }
        //sjekker hvor det går veier til
        for(int i = 0; i < nodes.length; i++){
            //sjekker hvor det går veier fra
            for(int j = 0; j < nodes.length; j++){
                //dersom det er kobling fra j til i
                if(hasConnection(nodes[j],nodes[i])){
                    addConnection(transposedTable[i],transposedTable[j]);
                }
            }
        }
        return new LinkedListGraph(transposedTable);
    }

    private boolean hasConnection(Node from, Node to){
        Edge tempEdge = from.nextEdge;
        if(tempEdge == null){
            return false;
        }
        while(tempEdge.nextEdge != null){
            if(tempEdge.to.equals(to)){
                return true;
            }
            tempEdge = tempEdge.nextEdge;
        }
        return tempEdge.to.equals(to);
    }
    private void addConnection(Node fromNode, Node toNode){
        Edge tempEdge = fromNode.nextEdge;
        if(tempEdge == null){
            fromNode.nextEdge = new Edge(toNode,null);
            return;
        }
        //finner siste kant
        while(tempEdge.nextEdge != null){
            tempEdge = tempEdge.nextEdge;
        }
        tempEdge.nextEdge = new Edge(toNode,null);
    }

    public List<Node> depthFirst(int index, int dist){
        //TODO slett dette, bruk has connection-metodene istedenfor
        ArrayList<Node> foundNodes = new ArrayList<>();
        nodes[index].dist = ++dist;
        Edge tempEdge = nodes[index].nextEdge;
        foundNodes.add(nodes[index]);

        if(nodes[index].foundTime == 0){
            nodes[index].setFoundTime();
        }
        while(tempEdge != null){
            if(tempEdge.to.dist == Node.inf){
                tempEdge.to.dist = dist;
                tempEdge.to.setFoundTime();
                foundNodes.addAll(depthFirst(tempEdge.to.index,dist));
            }
            tempEdge = tempEdge.nextEdge;
        }
        nodes[index].setFinsishedTime();
        return foundNodes;
    }

    public List<Node> sortByFoundTime() {
        ArrayList<Node> foundNodes = new ArrayList<>();
        for (int i = 0; i < nodes.length; i++) {
            for (Node n : depthFirst(i, 0)) {
                if (!foundNodes.contains(n)) {
                    foundNodes.add(n);
                }
            }
        }
        //sorterer i synkende rekkefølge
        foundNodes.sort(Comparator.comparingInt(o -> -o.finsishedTime));
        return foundNodes;
    }

    public HashMap<Integer, List<Node>> getStronglyConnnected(){
        HashMap<Integer, List<Node>> testingHash = new HashMap<>();
        List<Node> foundNodes = sortByFoundTime();
        LinkedListGraph transposeTable = transposeTable();
        int counter = 0;
        for(Node n : foundNodes){
            ArrayList<Node> tempList = new ArrayList<>();
            for(Node n2 : transposeTable.depthFirst(n.index,0)){
                if(!tempList.contains(n2)){
                    tempList.add(n2);
                }
            }
            if(!tempList.isEmpty()){
                testingHash.put(counter, tempList);
                counter++;
            }

        }
        return testingHash;
    }




    @Override
    public String toString() {
        String answer = "";
        for(Node n : nodes){
            answer += n.index + " => ";
            Edge tempEdge = n.nextEdge;
            while(tempEdge != null){
                answer += tempEdge.to.index + " => ";
                tempEdge = tempEdge.nextEdge;
            }
            answer +="\n";
        }
        return answer;
    }
}

