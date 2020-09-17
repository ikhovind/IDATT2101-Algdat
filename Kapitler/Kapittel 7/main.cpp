#include <iostream>

typedef struct{
    int len;
    int node[5];
} heap;

int venstre(int pos){
    return pos<<1+1;
}
int forelder(int pos){
    return (pos-1) >> 1;
}
int hoyre(int pos){
    return (pos+1) << 1;
}

void bytt(int *pos1, int *pos2){
    int temp = *pos1;
    *pos1 = *pos2;
    *pos2 = temp;
}
void fiks_heap(heap *hp, int pos){
    int m = venstre(pos);
    if(m < hp->len){
        int h = m+1;
        if(h<hp->len && hp->node[h]>hp->node[m]) m = h;
        if(hp->node[m] > hp->node[pos]){
            bytt(hp->node+pos,hp->node+m);
            fiks_heap(hp,m);
        }
    }
}

void lag_heap(heap *hp){
    int i = hp->len/2;
    while(i--) fiks_heap(hp, i);
}

int hent_indeks(heap *hp, int pos){
    int indeks = hp->node[pos];
    hp->node[pos] = hp->node[--hp->len];
    fiks_heap(hp,pos);
    return indeks;
}

void printHeap(heap test){
    for(int i = 0; i < test.len; i++){
    std::cout << test.node[i] << "\n";
    }
    std::cout << "\n";
}
void just_opp(heap *hp, int pos){
    if(hp->node[pos] > hp->node[forelder(pos)]){
        int temp = forelder(pos);
        bytt(hp->node+ pos,hp->node + forelder(pos));
        //std::cout << pos << "\n";
        just_opp(hp, temp);
    }
}
void just_prio(heap *hp, int pos, int oek){
    hp->node[pos] += oek;
    just_opp(hp, pos);
}
int main() {
    heap test = {5, 66,3,5,22,61} ;
    lag_heap(&test);
    printHeap(test);
    just_prio(&test,3, 50);
    //fucka, kanskje fordi at den er rar når den bytter på, får sykt svære tall så kan være adresser idk
    printHeap(test);
    return 0;
}
