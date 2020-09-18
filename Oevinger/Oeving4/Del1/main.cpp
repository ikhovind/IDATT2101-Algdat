#include <stdio.h>
#include <memory.h>
#include <iostream>
#include <fstream>

struct placedLink{
    struct indexLink *next;
    unsigned index;
    unsigned char name[];
};
using namespace std;
unsigned long multhash(int k, int x);
unsigned long keyGen(unsigned char *str);
#define MAXCHAR 1000
int main() {
    using namespace std;
    char str[MAXCHAR];
    char* filename = "navn20.txt";
    FILE *fp;
    fp = fopen(filename, "r");
    if (fp == NULL){
        return 1;
    }
    int counter = 0;
    unsigned long max = 0;
    unsigned long min = 7963063666499351619;
    while (fgets(str, MAXCHAR, fp) != NULL)
    {
        if(max < keyGen(reinterpret_cast<unsigned char *>(str))){
            max = keyGen(reinterpret_cast<unsigned char *>(str));
        }
        if(min > keyGen(reinterpret_cast<unsigned char *>(str))){
            min = keyGen(reinterpret_cast<unsigned char *>(str));
        }
        struct placedLink name = {NULL, NULL};
        memcpy(name.name,str, sizeof(str));
        printf("%lu\n", multhash(keyGen(name.name),7));
    }
    fclose(fp);
    return 0;
}

unsigned long keyGen(unsigned char *str){
    unsigned long sum = 0;
    for(int i = 0; i < strlen(reinterpret_cast<const char *>(str)); i++){
        //ganger med  7^i
        sum = sum * 7+ (str[i])*(7);
    }
    return sum;
}

unsigned long multhash(int k, int x){
    const std::uint32_t knuth = 2654435769;
    return k * knuth >> (32-x);
}