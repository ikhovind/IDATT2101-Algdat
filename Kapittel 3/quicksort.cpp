#include <iostream>

void bytt(int *i, int *j){
    int k = *j;
    *j = *i;
    *i = k;
}
int median3sort(int *t, int v, int h){
    int m = (v+h)/2;
    if(t[v]>t[m]) bytt(&t[v], &t[m]);
    if(t[m]>t[h]){
        bytt(&t[m], &t[h]);
        if(t[v]>t[m]) bytt(&t[v], &t[m]);
    }
    return m;
}
int splitt(int *t, int v, int h){
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
void quickSort(int *array, int v, int h){
    if(h-v > 2){
        int delepos = splitt(array,v,h);
        quickSort(array,v, delepos-1);
        quickSort(array,delepos+1, h);
    } else{
        median3sort(array, v,h);
    }
}

int main(){
    int test[] = {6,8,2,3,5,7,7};
    quickSort(test, 0, 6);
    for(int i = 0; i < 7; i++){
        std::cout << test[i] << "\n";
    }
}