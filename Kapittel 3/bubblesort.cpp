#include <iostream>


void bubbleSort(int* array, int length){
    for(int i = 0; i < length; i++){
        bool isSorted = true;
        for(int j = i; j < length; j++){
            if(array[i] > array[j]){
                int k = array[j];
                array[j] = array[i];
                array[i] = k;
                isSorted = false;
            }
        }
        if(isSorted){
            return;
        }
    }
}

int main() {
    int testListe[] = {1,2,3,4,5,6};
    bubbleSort(testListe, 6);
    for(int i = 0; i<6; i++){
        std::cout << testListe[i] << "\n";
    }
    return 0;
}

