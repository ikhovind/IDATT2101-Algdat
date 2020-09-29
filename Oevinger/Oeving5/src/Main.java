import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        File graph = new File("L7g6");

        BufferedReader br = new BufferedReader(new FileReader(graph));
        Graphtable table = new Graphtable(br);

        HashMap<Integer, List<Node>> stronglyConnected = table.getStronglyConnnected();
        String answer = "";
        answer = "Komponent   :   Noder i Komponenten\n";
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
        GraphLinkedList test = new GraphLinkedList(new BufferedReader(new FileReader(graph)));
        System.out.println(test.toString());
    }

}



class Edge{
    Edge next;
    Node to;
    public Edge(){
        exist = false;
    }
    public Edge(Node n, Edge next){
        to = n;
        this.next = next;
    }
    boolean exist;
}
 class Node{
    static int inf = 1000000000;
    static int time = 0;
    int index;
    int foundTime;
    int finsishedTime;
    int dist = inf;
    Edge next = null;
    public Node(int index){
        this.index = index;
    }
    public void setFoundTime(){
        this.foundTime = ++time;
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
 */
class Graphtable{
    int N;
    Edge edgeTable[][];
    Node nodes[];
    public Graphtable(BufferedReader br) throws IOException {
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
    public Graphtable(Edge[][] edgeTable){
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
        Graphtable transposeTable = new Graphtable(transposeTable());
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

class GraphLinkedList{
    Node[] nodes;
    int N;
    public GraphLinkedList(BufferedReader br) throws IOException {
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
            Edge edge = new Edge(nodes[to],nodes[from].next);
            nodes[from].next = edge;
        }
    }

    @Override
    public String toString() {
        String answer = "";
        for(Node n : nodes){
            answer += n.index + " => ";
            Edge tempEdge = n.next;
            while(tempEdge != null){
                answer += tempEdge.to.index + " => ";
                tempEdge = tempEdge.next;
            }
            answer +="\n";
        }
        return answer;
    }
}

