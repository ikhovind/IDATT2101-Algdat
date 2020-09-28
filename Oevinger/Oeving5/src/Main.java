import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        File graph = new File("L7g6");
        BufferedReader br = new BufferedReader(new FileReader(graph));
        Graphtable table = new Graphtable(br);
        System.out.println(table.toString());
        for(Node n : table.depthFirst(1,0)){
            System.out.println(n.toString());
        }
        //Gå gjennom nodene og finn de med senest ferdig tid, kjør dybde først på hver disse, grafene man får ut da etter å has kjørt det sekvensielt blir de sterkt sammenhengende
        //dvs start med kolonner istedenfor rader når man søker kanskje?
        //Det eneste vi er interessert i fra første søk er finishTime, så kan simpelthen legge til alle nodene etter sorteringene i én liste
        //Flytt for-løkka ut av metoden? 
    }
}



class Edge{
    boolean exist;
}
 class Node{
    static int time = 0;
    int index;
    int foundTime;
    int finsishedTime;
    int dist = 1000000000;
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
}
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
    public List<Node> depthFirst(int nodeIndex, int dist){
        ArrayList<Node> foundNodes = new ArrayList<>();
        for(int i = 0; i < edgeTable[nodeIndex].length; i++){
            if(edgeTable[nodeIndex][i].exist && nodes[i].dist > 100000000){
                nodes[i].setFoundTime();
                nodes[i].dist = ++dist;
                foundNodes.add(nodes[i]);
                foundNodes.addAll(depthFirst(i, dist));
                nodes[i].setFinsishedTime();
            }
        }
        return foundNodes;
    }
}
