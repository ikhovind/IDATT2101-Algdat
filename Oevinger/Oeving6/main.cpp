// C++ Implementation of Kosaraju's algorithm to print all SCCs 
#include <iostream>
#include <list>
#include <stack>
#include <fstream>
using namespace std;

struct Edge{
    int to;
    int weight;
};
//brukes til å lage en min-heap av et sub-tre i arrayet
void heapify(int arr[], int n, int i)
{
    int smallest = i; // Initialize smallest as root
    int l = 2 * i + 1; // left = 2*i + 1
    int r = 2 * i + 2; // right = 2*i + 2

    // If left child is smaller than root
    if (l < n && arr[l] < arr[smallest])
        smallest = l;

    // If right child is smaller than smallest so far
    if (r < n && arr[r] < arr[smallest])
        smallest = r;

    // If smallest is not root
    if (smallest != i) {
        swap(arr[i], arr[smallest]);

        // Recursively heapify the affected sub-tree
        heapify(arr, n, smallest);
    }
}

void buildHeap(int arr[], int n)
{
    // Index of last non-leaf node
    int startIdx = (n / 2) - 1;

    // Perform reverse level order traversal
    // from last non-leaf node and heapify
    // each node
    for (int i = startIdx; i >= 0; i--) {
        heapify(arr, n, i);
    }
}
class Graph
{
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
    //TODO usikker om denne skal ha returtype eller om det bare blir lettere dersom jeg deklarerer arrayet eller dataen inne i metoden og printer den der
    int* dijkstra(int start){

        //minimum heap som holder på avstandene til de andre noden
        int minHeap[adj->size()];
        list<Edge> startNode = adj[start];
        for(int i = 0; i < adj->size(); i++){
            //TODO vurder å bruke bool found for første sjekk av avstand istedenfor
            minHeap[i] = INT32_MAX / 2;
            std::cout << "max int " << minHeap[i] << std::endl;
        }
        minHeap[start] = 0;
        //itererer over listen
        for (auto const& i : startNode) {
            //alle nodene som det går direkte vei til
            minHeap[start] = i.weight;
        }

        adj[start];
    };
    //TODO implementer printing av dijkstra
};

int main(int argc, char** argv){
    int testHeap[] = {8,5,6,14,3, 231};
    //Graph *g = new Graph(argv[1]);
   // g->dijkstra(9);
    buildHeap(testHeap,6);
    for(int i = 0; i < 6; i++){
        std::cout << testHeap[i] << " ";
    }
}



