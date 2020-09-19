
#include <iostream>
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
    unsigned hash1 = hashOne(*key);
    int index = -1;
    if(!hashTable[hash1]){
        hashTable[hash1] = key;
        index = hash1;
    }
    else{
        collisions++;
        unsigned hash2 = hashTwo(*key);
        if(hash1+hash2 >= length){

            hash1 = (hash1 > hash2) ? length-hash1 : length-hash2;
        }
        while(hashTable[hash1+hash2] != NULL){
            hash2 += hash2;
            if(hash1 + hash2 >= length){
                hash1 = 0;
                hash2 = hashTwo(*key);
            }
        }
        hashTable[hash1 + hash2] = key;
        index = hash1+ hash2;
    }
    return index;
}
int normalArray[twoToTwentyFour];
int *hashMap[twoToTwentyFour];

int main() {

    /* initialize random seed: */
    srand (time(NULL));

    /* generate secret number between 1 and 10: */
    for(int i = 0; i < twoToTwentyFour; i++){
        //RAND_MAX er på ca 2 milliarder, som er mye større enn vår 16 millioner lange tabell
        int random = rand();
        normalArray[i] = random;
    }
    //kjører bra i en liten stund, etter det så fryser den bare, uendelig løkke kanskje?
    for(int i = 0; i < twoToTwentyFour; i++){
        std::cout << "index: " << insertNum(&normalArray[i], twoToTwentyFour, hashMap) << std::endl;
    }
    std::cout << "Antall kollisjoner: " << collisions << std::endl;
    return 0;
}

