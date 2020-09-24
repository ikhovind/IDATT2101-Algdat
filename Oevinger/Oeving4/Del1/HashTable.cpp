#include <iostream>
#include <list>

using namespace std;

class HashTable{
private:
    //Bruker c++ sine innebygde lenkede liste,
    // dette er en doubly linked list, men den brukes bare i én retning
    list<std::string> *table;
    int totalElements;

public:
    HashTable(int n){
        totalElements = n;
        table = new list<std::string>[totalElements];
    }

private:
    unsigned long keyGen(std::string name){
        using namespace std;
        unsigned long sum = 0;
        for(int i = 0; i < name.string::length(); i++){
            //ganger med  7^i
            sum += sum * 7+ (name.at(i))*(7);
        }
        //får segmentation fault dersom jeg prøver å flytte utregningen fra multHash inn i keyGen?
        //men det fungerer slik det er nå
        return multHash(sum, 7);
    }

    unsigned long multHash(unsigned sum, int x){
        const std::uint32_t knuth = 2654435769;
        return sum * knuth >> (32-x);
    }

public:
    //Sette data inn i hashTable:
    void insertElement(std::string name){
        table[keyGen(name)].push_back(name);
    }
    //Brukes til å lete etter et navn i tabellen
    bool existsInTable(std::string name){
        //finner hashen
        int index = keyGen(name);
        //itererer over alle navnene med samme hash
        for(std::string s : table[index]){
            //dersom et av navnene er like
            if (name == s){
                return true;
            }
        }
        return false;
    }

    int printAll(){
        //brukes til å telle antall kollisjoner totalt
        int counter = 0;

        for(int i = 0; i < totalElements; i++) {
            int innerCounter = 0;
            cout << "Index " << i << ": ";
            //Traverserer listen og printer ut denne
            for (std::string j : table[i]){
                //teller antall navn på samme indeks
                innerCounter++;
                //dersom det er en kollisjon så indikeres dette med en slik pil
                cout << j << " => ";
            }
            //dersom det er mer enn ett navn på en indeks så er det kollisjoner
            if(innerCounter > 1){
                //counter teller antall kollisjoner, som er antall navn som ble lagt til etter det første
                counter += innerCounter-1;
            }
            cout << endl;
        }
        return counter;
    }
};

