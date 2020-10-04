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
    //antall noder finnes via lengden av lista, så er viktigere å lagre antall kanter her
    int noEdges;    // No. of vertices
    list<Edge> *adj;    // An array of adjacency lists

public:
    Graph(std::string inputFileLoc){
        ifstream myfile;
        myfile.open (inputFileLoc);
        int from, to, weight;
        //leser inn antall noder og antall kanter
        myfile >> from >> to;
        this->noEdges = to;
        adj = new list<Edge>[from];
        heapSize = adj->size();
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
    int* dijkstra(int start, int *distTo, NodeResult* minHeap) {
        /*
         * Starter med å slette rota fra heapen - Det er den jeg jobber på nå
         *sjekker så om det er noen av veiene i minheap som blir kortere dersom jeg går via start
         * Kaller buildHeap og og kaller så dijkstra på det nye treet
         * elementært resultat dersom sizeOf er null om det funker
         * eller så kan jeg sjekke om det er en nullpointer
         * eller så kan jeg gi alle nodene en funnet bool, og returnerer dersom alle er true
         * Men dette skal ikke være nødvendig ettersom at jeg sletter de fra heapen etter at jeg er ferdig
         * sjekk om heapsize blir redusert automatisk fra deleteRoot()
         * Hvis så kan dette være den elementære utveien
         */
        //alle kantene fra noden vi starter fra
        list<Edge> startNode = adj[start];

        //for hver kant fra startnoden
        for (auto const &i : startNode) {
            //dersom noden vi er i nå har en kortere vei til i
            if(distTo[start] + i.weight < distTo[i.to]){
                distTo[i.to] = distTo[start] + i.weight;
            }
        }
        //lager ny heap, skal så bruke denne til å finne den korteste som ikke er ferdig
        buildHeap(minHeap,heapSize);

    }
    //TODO implementer printing av dijkstra
};

int main(int argc, char** argv){


}



