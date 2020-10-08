#include <iostream>
#include <list>
#include <fstream>

struct Edge {
    int to;
};
struct Node {
    int index;
    int foundTime;
    int finishedTime;
};

class Graph {
    std::list<Edge> *adj;    // Array med linked list
    int noVertices;     //antall noder

public:
    int myTime;

    Graph(std::string inputFileLoc) {
        myTime = 0;
        std::ifstream myfile;
        myfile.open(inputFileLoc);
        int from, to, weight;
        //leser inn antall noder og antall kanter
        myfile >> from >> to;
        this->noVertices = from;
        //lager tabell med lister
        adj = new std::list<Edge>[from];
        //størrelsen på den første heapen
        //leser inn kantene
        while (myfile >> from >> to) {
            Edge next = {to};
            addEdge(from, next);
        }
        myfile.close();
    }

    void addEdge(int from, Edge to) {
        adj[from].push_back(to); //legger til en kant fra from til to
    }

    int getNoNodes() {
        return noVertices;
    }

    int getTime() {
        return myTime++;
    }

    void depthFirst(Node *resultArray, int startIndex) {
        resultArray[startIndex].foundTime = getTime();
        for (auto const &edge : adj[startIndex]) {
            //dersom noden ikke er blitt funnet så legges den til i heapen og setter avstand
            if (resultArray[edge.to].foundTime == INT32_MAX / 2) {
                depthFirst(resultArray, edge.to);
            }
        }
        resultArray[startIndex].finishedTime = getTime();
    }
    //Topologisk sortering med bubble sort
    void topoSort(Node *resultArray) {
        for (int i = 0; i < noVertices; i++) {
            if (resultArray[i].foundTime == INT32_MAX / 2) {
                depthFirst(resultArray, i);
            }
        }
        for (int i = 0; i < noVertices; i++) {
            for (int j = 0; j < noVertices - 1; j++) {
                if (resultArray[j + 1].finishedTime > resultArray[j].finishedTime) {
                    Node temp = resultArray[j + 1];
                    resultArray[j + 1] = resultArray[j];
                    resultArray[j] = temp;
                }
            }
        }
    }
};

int main(int argc, char **argv) {
    Graph *g = new Graph(argv[1]);
    Node *resultArray = ((Node *) malloc(sizeof(struct Node) * ((g->getNoNodes()))));
    for (int i = 0; i < g->getNoNodes(); i++) {
        resultArray[i] = {i, INT32_MAX / 2, INT32_MAX / 2};
    }
    g->topoSort(resultArray);
    std::cout << "indeks         funnet tid          ferdig tid\n";
    for (int i = 0; i < g->getNoNodes(); i++) {
        std::cout << i << "               " << resultArray[i].foundTime << "                         "
                  << resultArray[i].finishedTime << std::endl;
    }
}
