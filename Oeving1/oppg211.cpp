#include <iostream>
#include <chrono>
#include <cmath>

double power211(double x, int n);

int main() {

	std::cout <<"Testdata:\n";
	std::cout <<"3^12 er lik: " << power211(3,12) << "\n";
	std::cout <<"2^10 er lik: " << power211 (2,10) << "\n";

	double grunn;
	int potens;
	std::cout << "Tidsmåling:\nSkriv inn grunntallet, så eksponenten:\n";
	std::cin >> grunn;
	std::cin >> potens;

	using namespace std::chrono;

	milliseconds end;
	int runder = 0;
	double b;

	milliseconds start = duration_cast< milliseconds 	>(system_clock::now().time_since_epoch());
	//kjører funkjsonen i 1000 millisekunder
	do {
	runder ++;
	b = power211(grunn, potens);

	end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
	} while (std::chrono::duration_cast<std::chrono::milliseconds>(end- start).count()<1000);
	//1000 millisekunder delt på antall runder, dette blir millisekund per runde
	std::cout << "Millisekunder per utregning: " << (((end-start).count()*1.0)/(runder*1.0)) <<"\n";

}


double power211(double x, int n){
    if(n == 0){
        return 1;
    }
    if(n>0){
        return x * power211(x, n - 1);
    }
    else{
        return 1 / power211(x, (n * -1));        
    }
}

