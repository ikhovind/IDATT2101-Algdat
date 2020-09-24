#include <stdio.h>
#include <memory.h>
#include <fstream>
#include "HashTable.cpp"
#define MAXCHAR 100

int main() {
    using namespace std;
    string item_name;
    ifstream nameFileout;
    nameFileout.open("navn20.txt");
    string line;
    HashTable hashTable1(128);
    int nameCounter = 0;
    while(std::getline(nameFileout, line))
    {
        nameCounter++;
        hashTable1.insertElement(line);
    }
    //Hvilke navn som kolliderte vises grafisk
    int collisions = hashTable1.printAll();
    //kunne ha oppnådd en en mer gunstig lastfaktor dersom vi hadde hashet med modulo og ikke heltallsdivisjon
    //Fokus på hastighet førte til at vi valgte 2^7 som tabellstørrelse, som er en del større enn de 85 navnene som skulle lagres
    cout << "Lastfaktor: " << nameCounter/128.0 << endl;
    cout << "Kollisjoner per navn: " << ((double)collisions)/nameCounter << endl;
    //Viser at den finnes igjen i tabellen
    if(hashTable1.existsInTable("Ingebrigt Kristoffer Thomassen,Hovind")){
        cout << "Ingebrigt finnes i tabellen" << endl;
    }
    //viser at den returnerer false dersom navnet ikke finnes
    if(hashTable1.existsInTable("Linus Benedict,Torvalds")){
        cout << "Linus finnes i tabellen" << endl;
    }
    else{
        cout << "Linus finnes ikke i tabellen" << endl;
    }

}

