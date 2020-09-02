//
// Created by ingebrigt on 01.09.2020.
//

#ifndef OEVING2_QUICKSORT_H
#define OEVING2_QUICKSORT_H
#include "InsertionSort.cpp"

class Quicksort {
    static void bytt(int *i, int *j){
        int k = *j;
        *j = *i;
        *i = k;
    }
    static int median3sort(int *t, int v, int h){
        int m = (v+h)/2;
        if(t[v]>t[m]) bytt(&t[v], &t[m]);
        if(t[m]>t[h]){
            bytt(&t[m], &t[h]);
            if(t[v]>t[m]) bytt(&t[v], &t[m]);
        }
        return m;
    }
    static int splitt(int *t, int v, int h){
        int iv, ih;
        int m = median3sort(t,v,h);
        int dv = t[m];
        bytt(&t[m], &t[h-1]);
        for(iv = v, ih = h - 1;;){
            while (t[++iv] < dv);
            while(t[--ih]>dv);
            if(iv >= ih) break;
            bytt(&t[iv], &t[ih]);
        }
        bytt(&t[iv], &t[h-1]);
        return iv;
    };
public:static void quickSort(int *array, int v, int h, int insertionSortLength){
        if(h-v > insertionSortLength){
            int delepos = splitt(array,v,h);
            quickSort(array,v, delepos-1, insertionSortLength);
            quickSort(array,delepos+1, h, insertionSortLength);
        } else{
            InsertionSort::insertionSort(array, v,h);
            median3sort(array, v,h);
        }
    }

    static int partition(int * arr, int low, int high, int* lp)
    {
        swap(&arr[low],&arr[low+(high-low)/3]);
        swap(&arr[high],  &arr[high-(high-low)/3]);

        if (arr[low] > arr[high])
            swap(&arr[low], &arr[high]);

        // p is the left pivot, and q is the right pivot.
        int j = low + 1;
        int g = high - 1, k = low + 1, p = arr[low], q = arr[high];
        while (k <= g) {

            // if elements are less than the left pivot
            if (arr[k] < p) {
                swap(&arr[k], &arr[j]);
                j++;
            }

                // if elements are greater than or equal
                // to the right pivot
            else if (arr[k] >= q) {
                while (arr[g] > q && k < g)
                    g--;
                swap(&arr[k], &arr[g]);
                g--;
                if (arr[k] < p) {
                    swap(&arr[k], &arr[j]);
                    j++;
                }
            }
            k++;
        }
        j--;
        g++;

        // bring pivots to their appropriate positions.
        swap(&arr[low], &arr[j]);
        swap(&arr[high], &arr[g]);

        // returning the indices of the pivots.
        *lp = j; // because we cannot return two elements
        // from a function.

        return g;
    }
    static void swap(int* a, int* b)
    {
        int temp = *a;
        *a = *b;
        *b = temp;
    }
public:
    static void dualPivotQuicksort(int* arr, int low, int high, int insertionLength)
    {
        if(low-high > insertionLength) {
            if (low < high) {
                // lp means left pivot, and rp means right pivot.
                int lp, rp;
                rp = partition(arr, low, high, &lp);
                dualPivotQuicksort(arr, low, lp - 1,insertionLength);
                dualPivotQuicksort(arr, lp + 1, rp - 1, insertionLength);
                dualPivotQuicksort(arr, rp + 1, high, insertionLength);
            }
        } else{
            return InsertionSort::insertionSort(arr,low,high);
        }
    }

};


#endif //OEVING2_QUICKSORT_H
