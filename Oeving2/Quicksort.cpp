//
// Created by ingebrigt on 01.09.2020.
//

#ifndef OEVING2_QUICKSORT_H
#define OEVING2_QUICKSORT_H
#include <iostream>
#include <chrono>

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
    }
    ;
public:static void quickSort(int *array, int v, int h){
        if(h-v > 2){
            int delepos = splitt(array,v,h);
            quickSort(array,v, delepos-1);
            quickSort(array,delepos+1, h);
        } else{
            median3sort(array, v,h);
        }
    }

};


#endif //OEVING2_QUICKSORT_H
