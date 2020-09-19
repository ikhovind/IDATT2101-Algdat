
#include <iostream>
#include <cstdint>
#include <climits>

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
    if(value == 0 || value == twoToTwentyFour){
        std::cout << "feil" << std::endl;
    }
    return (value);
}

unsigned insertNum(int *key, int length, int *hashTable[]){
    //std::cout << "\ninputrunde:\n";
    long int hash1 = hashOne(*key);
    int index = -1;
    if(!hashTable[hash1]){
        hashTable[hash1] = key;
        index = hash1;
    }
    else{
        collisions++;
        long int hash2 = hashTwo(*key);
        if(hash1+hash2 >= length){
            hash1 = (hash1 > hash2) ? length-hash1 : length-hash2;
        }
        while(hashTable[hash1+hash2] != NULL){
            std::cout << "kollisjon\n" << std::endl;
            /*
            std::cout << "hash1 " << hash1 << std::endl;
            std::cout << "hash2 " << hash2 << std::endl;
            std::cout << "lengde " << length << std::endl;
             */
            hash2 += hash2;
            if((hash1 + hash2) >= length){
                hash2 = hash1 + hash2;
                hash2 -= length;
                hash1 = 0;
                /*
                std::cout << "hash1 etter " << hash1 << std::endl;
                std::cout << "test \n\n\n\n\n";
                std::cout << "hash2 etter " << hash2 << std::endl;
                 */
            }
        }
        hashTable[hash1 + hash2] = key;
        index = hash1+ hash2;
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
    //TODO kjører bra opp til ca 7-8 millioner, så blir det bare kollisjoner?
    for(int i = 0; i < 10000000; i++){
        std::cout << "indeks: " << i << std::endl;
        insertNum(&normalArray[i], twoToTwentyFour, hashMap);
    }
    std::cout << "Antall kollisjoner: " << collisions << std::endl;
    return 0;
}
