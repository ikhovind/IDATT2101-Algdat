
#include <iostream>
#include <cstdint>
#include <climits>
#include <iostream>
#include <chrono>
#include <cmath>
#include <unordered_map>
#define twoToTwentyFour 16777216U
int collisions = 0;
unsigned hashOne(int num){
    const unsigned knuth = 2654435769U;
    //får et svar mellom 0 og 2^24
    return num * knuth >> (32-24);

}
unsigned hashTwo(int num){
    //gir kun oddetall
    unsigned value = ((2*num+1)%twoToTwentyFour);
    return (value);
}

unsigned insertNum(int *key, int length, int *hashTable[]){
    //std::cout << "\ninputrunde:\n";
    const long int hash1 = hashOne(*key);
    int index = -1;
    if(!hashTable[hash1]){
        hashTable[hash1] = key;
        index = hash1;
    }
    else{
        //kolliderer etter første hash
        collisions++;
        const long int hash2 = hashTwo(*key);
        long int sum = (hash1 + hash2) % length;
        while(hashTable[sum] != NULL){
            //dersom det kolliderer etter andre og påfølgende hash
            collisions++;
            sum = (sum+hash2)%length;
        }
        hashTable[sum] = key;
        index = sum;
    }
    return index;
}
int normalArray[10000000];
int *hashMap[twoToTwentyFour];

int main() {

    /* initialize random seed: */
    srand (time(NULL));

    /* generate secret number between 1 and 10: */
    for(int i = 0; i < 10000000; i++){
        //RAND_MAX er på ca 2 milliarder, som er mye større enn vår 16 millioner lange tabell
        int random = rand();
        normalArray[i] = random;
    }
    std::chrono::milliseconds start = std::chrono::duration_cast< std::chrono::milliseconds 	>(std::chrono::system_clock::now().time_since_epoch());
    for(int i = 0; i < 10000000; i++){
        insertNum(&normalArray[i], twoToTwentyFour, hashMap);
    }
    std::chrono::milliseconds end = std::chrono::duration_cast< std::chrono::milliseconds 	>(std::chrono::system_clock::now().time_since_epoch());

    std::cout << "Millisekunder: " << (end-start).count() << std::endl;
    std::cout << "Antall kollisjoner: " << collisions << std::endl;
    std::cout << "Lastfaktor: " << 10000000.0/twoToTwentyFour << std::endl;

    std::unordered_map<int, int> m;

    std::chrono::milliseconds start2 = std::chrono::duration_cast< std::chrono::milliseconds 	>(std::chrono::system_clock::now().time_since_epoch());
    for(int i = 0; i < 10000000; i++){
        //TODO this does not hash, only places the number at its own index i think
        m[normalArray[i]] = normalArray[i];
    }
    std::chrono::milliseconds end2 = std::chrono::duration_cast< std::chrono::milliseconds 	>(std::chrono::system_clock::now().time_since_epoch());
    std::cout << "Millisekunder: " << (end2-start2).count() << std::endl;

    return 0;
}

