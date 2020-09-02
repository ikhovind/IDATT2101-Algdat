//
// Created by ingebrigt on 01.09.2020.
//

#ifndef OEVING2_MAIN_H
#define OEVING2_MAIN_H
#include <iostream>
#include <chrono>
#include "Quicksort.cpp"
#include "DualPivot.cpp"

int* generateArray(int length){
    int* array = new int[length];
    for(int i = 0; i < length; i++){
        array[i] = (rand() % 100000) + 1;
    }
    return array;
}
int* generateSortedArray(int length){
    int* array = generateArray(length);
    Quicksort::quickSort(array,0,length-1, 2);
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
	int* monoPivotArray = generateArray(length);
	int monoPivotArraySum = getChecksum(monoPivotArray, length);

	//int* dualPivotArray = generateArray(length);
	//int dualPivotArraySum = getChecksum(dualPivotArray, length);


	using namespace std::chrono;
    milliseconds start = duration_cast< milliseconds 	>(system_clock::now().time_since_epoch());
    milliseconds end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());

    //denne kodesnutten ble brukt til å finne optimal lengde for å bruke insertion sort
    int optimizedInsertionLength = 0;
    int fastestTime = INT16_MAX;

    for(int i = 0; i < 200; i++){
    //starttid
        start = duration_cast< milliseconds 	>(system_clock::now().time_since_epoch());
        //Sorterer
        Quicksort::quickSort(monoPivotArray, 0, length-1,10);
        //sluttid
        end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
        //Hvis sorteringen som akkurat ble sortert var raskere enn den tidligere raskeste
        if((end-start).count()<fastestTime){
            fastestTime = (end-start).count();
            optimizedInsertionLength = i;
        }
    }
    std::cout << "Quicksort med 1 pivot på en tabell med " << length << " elementer brukte: " << fastestTime << "ms" << " med insertion sort på tabbeller som er kortere enn: " << optimizedInsertionLength <<"\n";


    /*
	start = duration_cast< milliseconds 	>(system_clock::now().time_since_epoch());
	DualPivot::dualPivotQuicksort(dualPivotArray,0,length-1);
	end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
	std::cout << "Quicksort med dual pivot på en tabell med " << length << " elementer brukte: " << (end-start).count() << "ms" <<"\n";
    */

	//Sjekker sortering
	if(checkSort(monoPivotArray, length)){
	    std::cout << "Sortering på mono pivot riktig\n";
	} else{
	    std::cout << "mono pivot sortering feil\n";
	}
	/*
	if(checkSort(dualPivotArray, length)){
	    std::cout << "Sortering på dual pivot riktig\n";
	} else{
	    std::cout << "dual pivot sortering feil\n";
	}
	 */
	//sjekker checksum
	if(monoPivotArraySum == getChecksum(monoPivotArray, length)){
	    std::cout << "Checksum monopivot riktig\n";
	} else{
	    std::cout << "checksum monopivot feil\n";
	}
	/*
	if(dualPivotArraySum == getChecksum(dualPivotArray, length)){
	    std::cout << "Checksum dualpivot riktig\n";
	} else{
	    std::cout << "Checksum dualpivot feil\n";
	}
    */

	delete[] monoPivotArray;
	//delete[] dualPivotArray;
}

#endif //OEVING2_MAIN_H
