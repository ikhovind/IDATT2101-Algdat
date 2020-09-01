#include <iostream>
#include <array>
#include <chrono>
#include <cmath>

double stockPow(double x, int n);

int main() {

	std::cout <<"Testdata:\n";
	std::cout <<"3^12 er lik: " << stockPow(3,12) << "\n";
	std::cout <<"2^10 er lik: " << stockPow(2,10) << "\n";

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
		b = stockPow(grunn, potens);

		end = duration_cast< milliseconds >(system_clock::now().time_since_epoch());
	} while (std::chrono::duration_cast<std::chrono::milliseconds>(end- start).count()<1000);
	
	//1000 millisekunder delt på antall runder, dette blir millisekund per runde
	std::cout << "Millisekunder per utregning: " << (((end-start).count()*1.0)/(runder*1.0)) <<"\n";

}

//kaller C sin egen metode på samme måte som mine egne
double stockPow(double x, int n){
	return pow(x,n);
}

