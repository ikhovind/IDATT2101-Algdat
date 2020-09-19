#include <stdio.h>
#include <memory.h>
#include "HashTable.cpp"
#define MAXCHAR 100

int main() {
    using namespace std;
    string item_name;
    ifstream nameFileout;
    nameFileout.open("navn20.txt");
    string line;
    HashTable hashTable1(128);
    while(std::getline(nameFileout, line))
    {
        hashTable1.insertElement(line);
    }
    hashTable1.printAll();
}

