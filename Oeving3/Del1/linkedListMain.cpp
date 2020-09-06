#include <iostream>
#include <string>
#include "node.cpp"

int main() {
    struct Node* head = NULL;
    struct Node* head2 = NULL;
    std::string choice;
    std::string number1;
    std::string number2;
    std::cout << "Enter the first number:\n";
    std::cin >> number1;
    std::cout << "Enter the second number:\n";
    std::cin >> number2;
    for(unsigned int i = 0; i<number1.length(); i++) {
        char c = number1[i]; //this is your character
        insertAtBack(&head,c - '0');
    }
    for(unsigned int i = 0; i<number2.length(); i++) {
        char c = number2[i]; //this is your character
        insertAtBack(&head2,c - '0');
    }
    std::cout << "Enter 1 for addition, 2 for subtraction:\n";
    std::cin >> choice;
    if(choice == "1"){
        std::cout << toString(head) << "\n+\n";
        std::cout << toString(head2) << "\n=\n";
        addListToList(&head,&head2);
        std::cout << toString(head) << "\n";
    } else if(choice == "2"){
        std::cout << toString(head) << "\n-\n";
        std::cout << toString(head2) << "\n=\n";
        subtractListFromList(&head,&head2);
        std::cout << toString(head) << "\n";

    }
    else{
        std::cout << "please input a valid choice\n";
    }
    return 0;
}
