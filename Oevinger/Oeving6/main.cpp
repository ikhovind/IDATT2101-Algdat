// C++ Implementation of Kosaraju's algorithm to print all SCCs 
#include <iostream>
#include <list>
#include <stack>
#include <fstream>
using namespace std;

struct Edge{
    int to;
    int weight;
    bool ferdig = false;
};
struct NodeResult{
    int index;
    int distFromStart;
};
//brukes til å lage en min-heap av et sub-tre i arrayet
void heapifySubTree(NodeResult *arr, int n, int i)
{
    int smallest = i; // Initialize smallest as root
    int l = 2 * i + 1; // left = 2*i + 1
    int r = 2 * i + 2; // right = 2*i + 2

    // If left child is smaller than root
    if (l < n && arr[l].distFromStart < arr[smallest].distFromStart)
        smallest = l;

    // If right child is smaller than smallest so far
    if (r < n && arr[r].distFromStart < arr[smallest].distFromStart)
        smallest = r;

    // If smallest is not root
    if (smallest != i) {
        swap(arr[i], arr[smallest]);

        // Recursively heapifySubTree the affected sub-tree
        heapifySubTree(arr, n, smallest);
    }
}

void buildHeap(NodeResult arr[], int n)
{
    // Index of last non-leaf node
    int startIdx = (n / 2) - 1;

    // Perform reverse level order traversal
    // from last non-leaf node and heapifySubTree
    // each node
    for (int i = startIdx; i >= 0; i--) {
        heapifySubTree(arr, n, i);
    }
}

// Function to delete the root from Heap
void deleteRoot(NodeResult arr[], int& n)
{
    // Get the last element
    NodeResult lastElement = arr[n - 1];

    // Replace root with first element
    arr[0] = lastElement;

    // Decrease size of heap by 1
    n = n - 1;

    // heapify the root node
    heapifySubTree(arr, n, 0);
}

class Graph
{
    int heapSize;
    int noEdges;    // No. of vertices
    list<Edge> *adj;    // An array of adjacency lists
    int noVertices;

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
        adj[from].push_back(to); // Add w to v’s list.
    }
    int getNoNodes(){
        return noVertices;
    }
    int* dijkstra(int start, int *distTo, NodeResult* minHeap) {
        if(heapSize == 0){
            return distTo;
        }
        //sletter noden vi akkurat gikk inn i fra heapen og heapsize reduseres
        deleteRoot(minHeap, heapSize);
        //dersom vi er i første node i grafen så er ikke denne funnet enda, så da setter vi den lik 0
        if(distTo[start] == INT32_MAX/2){
            distTo[start] = 0;
        }

        //alle kantene fra noden vi er i nå
        list<Edge> edgesFromNode = adj[start];


        //for hver kant fra startnoden
        for (auto const &edge : edgesFromNode) {
            //dersom noden vi er i nå har en kortere vei til i
            if(distTo[start] + edge.weight < distTo[edge.to]){
                distTo[edge.to] = distTo[start] + edge.weight;
            }
        }
        //oppdaterer heapen med alle de nye distansene
        for(int i = 0; i < noVertices; i++){
            minHeap[i].distFromStart = distTo[minHeap[i].index];
        }
        //Dersom heapen er i uorden etter at distansene har blitt oppdatert
        buildHeap(minHeap,heapSize);
        //Kaller dijkstra på nytt på elementet i heapen med minst distanse
        return dijkstra(minHeap[0].index,distTo,minHeap);
    }
    //brukes sånn at man skal slippe å lage heap i main
    int* outerDijkstra(int start, int *distTo){
        NodeResult minHeap[noVertices];
        for(int i = 0; i < noVertices; i++){
            distTo[i] = INT32_MAX/2;
            minHeap[i] = NodeResult{i,INT32_MAX/2};
        }
        return dijkstra(start,distTo,minHeap);
    }
    //TODO implementer printing av dijkstra
};

int main(int argc, char** argv){

    Graph *g = new Graph("Grafer/vg1.txt");
    int resultArray[g->getNoNodes()];

    g->outerDijkstra(0,resultArray);
    for(int i = 0; i < g->getNoNodes(); i++){
        std::cout << resultArray[i] << std::endl;
    }
}



