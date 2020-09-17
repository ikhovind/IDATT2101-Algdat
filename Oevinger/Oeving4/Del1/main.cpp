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
unsigned long myHash(unsigned char str[]);
#define MAXCHAR 1000
int main() {
    using namespace std;
    unsigned char line[MAXCHAR];
    char str[MAXCHAR];
    char* filename = "navn20.txt";
    FILE *fp;
    fp = fopen(filename, "r");
    if (fp == NULL){
        return 1;
    }
    while (fgets(str, MAXCHAR, fp) != NULL)
    {
        struct placedLink name = {NULL, NULL};
        memcpy(name.name,str, sizeof(str));
        printf("%lu\n",myHash(name.name));
    }
    fclose(fp);
    return 0;
}

unsigned long myHash(unsigned char str[]){
    unsigned long sum = 0;
    for(int i = 0; i < strlen(reinterpret_cast<const char *>(str)); i++){
        //ganger med  7^i
        sum = sum * 7+ (str[i])*(7);
    }
    return sum;
}