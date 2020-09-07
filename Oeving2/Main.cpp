//
// Created by ingebrigt on 01.09.2020.
//

#ifndef OEVING2_MAIN_H
#define OEVING2_MAIN_H
#include <iostream>
#include <chrono>
#include "Quicksort.cpp"

int* generateArray(int length){
    int* array = new int[length];
    for(int i = 0; i < length; i++){
        array[i] = (rand() % 100000) + 1;
    }
    return array;
}
int* generateSortedArray(int length){
    int* array = generateArray(length);
    Quicksort::quickSort(array,0,length-1);
    return array;
}
int* generateArrayWithReccuringNumbers(int length){
    int* array = new int[length];
    for(int i = 0; i < length; i++){
        if(i%2 == 0){
            array[i] = 15;
        }
        else {
            array[i] = (rand() % 100000) + 1;
        }
    }
    return array;
}

int getChecksum(int* array, int length){
    int sum = 0;
    for(int i = 0; i<length; i++){
        sum += array[i];
    }
    return sum;
}

bool checkSort(int* array, int length){
    for(int i = 0; i<length-1; i++){
        if(array[i] > array[i+1]){
            return false;
        }
    }
    return true;
}

int main(){
    int length = 10000000;
    //endrer metodekallet her for å endre hvilket array som blir sortert
	int* monoPivotArray = generateArray(length);
	int monoPivotArraySum = getChecksum(monoPivotArray, length);

    int* dualPivotArray = generateArray(length);
    int dualPivotArraySum = getChecksum(dualPivotArray, length);


    using namespace std::chrono;
    milliseconds start = duration_cast< milliseconds 	>(system_clock::now().time_since_epoch());
    milliseconds end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
   

	start = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
	Quicksort::quickSort(monoPivotArray,0,length-1);
	end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
	std::cout << "Quicksort med 1 pivot på en tabell med " << length << " elementer brukte: " << (end-start).count() << "ms" <<"\n";

    start = duration_cast< milliseconds 	>(system_clock::now().time_since_epoch());
    Quicksort::dualPivotQuicksort(dualPivotArray,0,length-1);
    end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
    std::cout << "Quicksort med 2 pivots på en tabell med " << length << " elementer brukte: " << (end-start).count() << "ms" <<"\n";

    //Sjekker sortering
	if(checkSort(monoPivotArray, length)){
	    std::cout << "Sortering på 1 pivot riktig\n";
	} else{
	    std::cout << "mono pivot sortering feil\n";
	}
    if(checkSort(dualPivotArray, length)){
        std::cout << "Sortering på dual pivot riktig\n";
    } else{
        std::cout << "dual pivot sortering feil\n";
    }
	//sjekker checksum
	if(monoPivotArraySum == getChecksum(monoPivotArray, length)){
	    std::cout << "Checksum 1 pivot riktig\n";
	} else{
	    std::cout << "checksum 1 pivot feil\n";
	}
    if(monoPivotArraySum == getChecksum(monoPivotArray, length)){
        std::cout << "Checksum dual pivot riktig\n";
    } else{
        std::cout << "checksum dual pivot feil\n";
    }

    delete[] dualPivotArray;
	delete[] monoPivotArray;
}

#endif //OEVING2_MAIN_H
