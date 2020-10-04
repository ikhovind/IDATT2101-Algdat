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
    Graph *g = new Graph(argv[1]);
    g->dijkstra(9);
}



