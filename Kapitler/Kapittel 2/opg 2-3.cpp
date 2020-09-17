#include <iostream>
#include "math.h"

int* fliselegging(int *gulv[], int m, int start, int slutt){
    if(m == 0){
        int finalGulv[] = {-1};
        return finalGulv;
    }
    
}

int main (){
    int m = 2;
    int gulv[2][2] = {};
    int *pointerArray[4] = { gulv[0], gulv[1], gulv[2] , gulv[3] };

    fliselegging(pointerArray, m, 0, pow(2,m));
    for (int i = 0; i < pow(2, m); i++) {
        for (int j = 0; j < pow(2,m); ++j) {
            std::cout << gulv[i][j];
        }
        std::cout << "\n";
    }
}
