#include <iostream>
#include <list>
#include <fstream>
using namespace std;

struct Edge{
    int to;
    int weight;
};
struct NodeResult{
    int index;
    int distFromStart;
    int pastNode;
};
//brukes til å lage en min-heap av et sub-tre i arrayet
void heapifySubTree(NodeResult *arr, int n, int i)
{
    int smallest = i; //vi har oppgitt indeks til roten som vi skal sjekke om er minst
    int l = 2 * i + 1; // left = 2*i + 1
    int r = 2 * i + 2; // right = 2*i + 2

    // Dersom venstre barn er mindre enn roten
    if (l < n && arr[l].distFromStart < arr[smallest].distFromStart)
        smallest = l;

    // Dersom høyre barn er mindre enn den minste indeksen så langt
    if (r < n && arr[r].distFromStart < arr[smallest].distFromStart)
        smallest = r;

    // Dersom vi har endret på smallest så betyr det at roten ikke er minst
    if (smallest != i) {
        swap(arr[i], arr[smallest]);

        // Recursively heapifySubTree the affected sub-tree
        //heapifySubTree(arr, n, smallest);
    }
}

void buildHeap(NodeResult arr[], int n)
{
    // siste noden som ikke er et blad
    int startIdx = (n / 2) - 1;

    // går gjennom hvert sub-tre i arrayet og heapifyer dette, fra bunn til topp
    //de minste nodene vil dermed flyte oppover
    for (int i = startIdx; i >= 0; i--) {
        heapifySubTree(arr, n, i);
    }
}

// Brukes til å slette roten når vi er ferdig med den i heapen
void deleteRoot(NodeResult arr[], int& n)
{
    // Siste element i heapen
    NodeResult lastElement = arr[n - 1];

    // Setter siste inn i første
    arr[0] = lastElement;

    // Endrer på heapSize
    n = n - 1;
    //bygger heapen på nytt slik at den blir i orden igjen etter at vi har slettet rota
    buildHeap(arr,n);
}

class Graph
{
    int heapSize;
    int noEdges;    // ant. kanter
    list<Edge> *adj;    // Array med linked list
    int noVertices;     //antall noder

public:
    Graph(std::string inputFileLoc){
        ifstream myfile;
        myfile.open (inputFileLoc);
        int from, to, weight;
        //leser inn antall noder og antall kanter
        myfile >> from >> to;
        this->noVertices = from;
        this->noEdges = to;
        //lager tabell med lister
        adj = new list<Edge>[from];
        //størrelsen på den første heapen
        heapSize = noVertices;

        //leser inn kantene
        while (myfile >> from >> to >> weight)
        {
            Edge next = {to, weight};
            addEdge(from, next);
        }
        myfile.close();
    }
    void addEdge(int from, Edge to){
        adj[from].push_back(to); //legger til en kant fra from til to
    }
    int getNoNodes(){
        return noVertices;
    }
    NodeResult* dijkstra(int start, NodeResult *distTo, NodeResult* minHeap) {
        //dersom man har kommet til siste node og det ikke er flere å sjekke avstand til
        if(heapSize == 0){
            return distTo;
        }

        //sletter noden vi akkurat gikk inn i fra heapen, heapsize reduseres i metoden deleteRoot
        deleteRoot(minHeap, heapSize);
        //alle kantene fra noden vi er i nå
        list<Edge> edgesFromNode = adj[start];
        //går gjennom alle kantene og sjekker om det er noen kortere veier til noen av nodene
        for (auto const &edge : edgesFromNode) {
            //dersom noden vi er i nå har en kortere vei til edge.to
            if(distTo[start].distFromStart + edge.weight < distTo[edge.to].distFromStart){
                //oppdaterer avstanden i distTo
                distTo[edge.to].distFromStart = distTo[start].distFromStart + edge.weight;
            }
        }
        //oppdaterer heapen med alle de nye distansene
        for(int i = 0; i < noVertices; i++){
            minHeap[i].distFromStart = distTo[minHeap[i].index].distFromStart;
        }
        //Dersom heapen er i uorden etter at distansene har blitt oppdatert
        buildHeap(minHeap,heapSize);
        //setter forrige node, denne brukes kun til når output vises
        distTo[minHeap[0].index].pastNode = start;
        //Kaller dijkstra på nytt på elementet i heapen med minst distanse
        return dijkstra(minHeap[0].index,distTo,minHeap);
    }
    //brukes sånn at man skal slippe å lage heap i main
    NodeResult* outerDijkstra(int start, NodeResult *distTo){
        NodeResult minHeap[noVertices];
        for(int i = 0; i < noVertices; i++){
            //lager nodeResultObjekter i hver heap indeks
            minHeap[i] = NodeResult{i,INT32_MAX/2,-1};
        }
        //avstand til startnode er alltid 0
        distTo[start].distFromStart = 0;
        minHeap[start].distFromStart = 0;
        buildHeap(minHeap,noVertices);
        return dijkstra(start,distTo,minHeap);
    }
    void printIndex(int i, NodeResult* resultArray){
        std::cout << resultArray[i].index << "         ";
        if(resultArray[i].distFromStart == INT32_MAX/2){
            std::cout << "N/A\n";
        }
        else {
            //dersom noden ikke har noen forrige node så printes den ikke
            if (resultArray[i].pastNode != -1) {
                std::cout << resultArray[i].pastNode << "           ";
            }

            if (resultArray[i].distFromStart == 0) {
                std::cout << "start       0\n";
            } else {
                std::cout << resultArray[i].distFromStart << std::endl;
            }
        }
    }
    //TODO implementer printing av dijkstra
};

int main(int argc, char** argv){

    Graph *g = new Graph("/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Oevinger/Oeving6/Grafer/vg4.txt");
    NodeResult resultArray[g->getNoNodes()];
    for(int i = 0; i < g->getNoNodes(); i++){
        resultArray[i] = {i,INT32_MAX/2,-1};
    }
    g->outerDijkstra(0,resultArray);
    std::cout << "index | forgjenger | avstand\n";
    for(int i = 0; i < g->getNoNodes(); i++){
        g->printIndex(i,resultArray);
    }
}



