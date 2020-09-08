#include <iostream>
#include <string>
#include "node.cpp"

int main(int argc, char *argv[]) {
    struct Node* head = NULL;
    struct Node* head2 = NULL;
    std::string number1 = argv[1];
    std::string choice = argv[2];
    std::string number2 = argv[3];

    for(unsigned int i = 0; i<number1.length(); i++) {
        char c = number1[i]; //character
        //gjør c numerisk
        insertAtBack(&head,c - '0');
    }
    for(unsigned int i = 0; i<number2.length(); i++) {
        char c = number2[i]; //charcter
        insertAtBack(&head2,c - '0');
    }
    if(choice == "+"){
        std::cout << toString(head) << "\n+\n";
        std::cout << toString(head2) << "\n=\n";
        addListToList(&head,&head2);
        std::cout << toString(head) << "\n";
    } else if(choice == "-"){
        std::cout << toString(head) << "\n-\n";
        std::cout << toString(head2) << "\n=\n";
        head = (subtractLinkedList(head,head2));
        std::cout << toString(head) << "\n";
    }
    return 0;
}
