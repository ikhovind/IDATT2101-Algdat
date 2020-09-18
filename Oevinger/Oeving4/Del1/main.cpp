#include <stdio.h>
#include <memory.h>
#include "HashTable.cpp"
#define MAXCHAR 100

int main() {
    using namespace std;
    char str[MAXCHAR];
    char* filename = "navn20.txt";
    FILE *fp;
    fp = fopen(filename, "r");
    if (fp == NULL){
        return 1;
    }
    while (fgets(str, MAXCHAR, fp) != NULL)
    {
        struct placedLink name = {nullptr};
        memcpy(name.name, str, sizeof(str));
        place(&name,strlen(str));
    }
    for(int i = 0; i < 128; i++){
        printf("output %s\n",hashTable->name);
    }
    fclose(fp);
    return 0;
}

