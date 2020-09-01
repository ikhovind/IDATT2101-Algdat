#include <iostream>
#include "stdio.h"

int veksle(int kroner, int sorter){
    int typer[] = {1,5,10,20,50};
    if(sorter==0){
        return 0;
    }
    if(kroner<0){
        return 0;
    }
    if(kroner == 0){
        return 1;
    }
    int sum = 0;
    sum += veksle(kroner-typer[sorter-1],sorter);
    sum += veksle(kroner, sorter-1);
    return sum;
}
int main(){
    std::cout << veksle(100,5);
}
