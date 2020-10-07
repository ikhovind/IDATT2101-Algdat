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
struct NodeOnHeap{
    //denne sparer litt plass i forhold til å bruke NodeResult, da jeg ikke trenger pastNode på heapen
    int index;
    int distFromStart;
};
//brukes til å lage en min-heap av et sub-tre i arrayet
void heapifySubTree(NodeOnHeap *arr, int n, int i)
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
    }
}

void buildHeap(NodeOnHeap arr[], int n)
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
void removeRoot(NodeOnHeap *arr, int& n)
{
    buildHeap(arr,n);

    // Siste element i heapen
    NodeOnHeap lastElement = arr[n - 1];

    // Setter siste inn i første
    arr[0] = lastElement;

    // Endrer på heapSize
    n = n - 1;

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
        heapSize = 0;

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
    NodeResult* dijkstra(int start, NodeResult *distTo) {
        distTo[start].distFromStart = 0;
        //størst mulig heap er at alle nodene er i den samtidig
        NodeOnHeap *minHeap = ((NodeOnHeap *) malloc(sizeof(struct  NodeResult) * (noVertices)));
        heapSize++;
        //bryter dersom man har kommet til siste node og det ikke er flere å sjekke avstand til
        do{
            //sletter den noden med kortest distanse og passer på at heapen er i riktig rekkefølge
            removeRoot(minHeap, heapSize);
            //går gjennom alle kantene og sjekker om det er noen kortere veier til noen av nodene
            for (auto const &edge : adj[start]) {
                //dersom noden ikke er blitt funnet så legges den til i heapen og setter avstand
                if (distTo[edge.to].distFromStart == INT32_MAX/2){
                    distTo[edge.to].distFromStart = distTo[start].distFromStart + edge.weight;
                    minHeap[heapSize] = NodeOnHeap{edge.to, distTo[edge.to].distFromStart};
                    heapSize++;
                    distTo[edge.to].pastNode = start;
                }
                    //dersom noden vi er i nå har en kortere vei til edge.to
                else if(distTo[start].distFromStart + edge.weight < distTo[edge.to].distFromStart){
                    //oppdaterer avstanden i distTo
                    distTo[edge.to].distFromStart = distTo[start].distFromStart + edge.weight;
                    distTo[edge.to].pastNode = start;
                }
            }
            //kunne ha sluppet denne sjekken hvis jeg hadde hatt pekere i minHeap, men fikk ikke det til
            for(int i = 0; i < heapSize; i++){
                minHeap[i].distFromStart = distTo[minHeap[i].index].distFromStart;
            }
            //dersom det er uorden i heapen etter at man har oppdatert distansene
            buildHeap(minHeap,heapSize);
            start = minHeap[0].index;
        }while (heapSize != 0);
        return distTo;
    }
    
    void printIndex(int i, NodeResult* resultArray, int start){
        std::cout << resultArray[i].index << "         ";
        if(resultArray[i].distFromStart == INT32_MAX/2){
            std::cout << "N/A\n";
        }
        else {
            //dersom noden ikke har noen forrige node så printes den ikke
            if (resultArray[i].pastNode != -1) {
                std::cout << resultArray[i].pastNode << "           ";
            }

            if (resultArray[i].index == start) {
                std::cout << "start       0\n";
            } else {
                std::cout << resultArray[i].distFromStart << std::endl;
            }
        }
    }
};

int main(int argc, char** argv){
    Graph *g = new Graph(argv[1]);
    NodeResult *resultArray = ((NodeResult *) malloc(sizeof(struct  NodeResult) * ((g->getNoNodes()))));
    for(int i = 0; i < g->getNoNodes(); i++){
        //bruker max/2 for å ikke få integer overflow når jeg summerer avstander
        resultArray[i] = {i ,INT32_MAX/2,-1};
    }
    int x = atoi(argv[2]);
    g->dijkstra(x,resultArray);
    std::cout << "index | forgjenger | avstand\n";
    for(int i = 0; i < g->getNoNodes(); i++){
        g->printIndex(i,resultArray, x);
    }
}



