#include <iostream>

int gcd(int a, int b){
    if(a>b){
        if(a%b == 0){
            return b;
        }
        return gcd(a%b,b);
    }
    if(b%a == 0){
        return a;
    }
    return gcd(a,b%a);
}
int main() {
    std::cout << gcd(16,20) << std::endl;
    return 0;
}
