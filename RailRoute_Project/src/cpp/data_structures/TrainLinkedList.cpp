#include "TrainLinkedList.h"

#include <iostream>
#include <limits>
using namespace std;

TrainLinkedList::TrainLinkedList() {
    head = nullptr;
}

TrainLinkedList::~TrainLinkedList() {
    while (head != nullptr) {
        Node* temp = head;
        head = head->next;
        delete temp;
    }
}

// Append an existing train record to the linked list
void TrainLinkedList::appendRecord(const TrainRecord& train) {
    Node* current = head;

    while (current != nullptr) {
        if (current->data.trainId == train.trainId) {
            return;
        }

        current = current->next;
    }

    Node* newNode = new Node(train);

    if (head == nullptr) {
        head = newNode;
        return;
    }

    current = head;

    while (current->next != nullptr) {
        current = current->next;
    }

    current->next = newNode;
}

// Insert a new train
void TrainLinkedList::insertTrain() {
    TrainRecord t;

    cout << "Enter Train ID: ";
    cin >> t.trainId;

    Node* current = head;

    while (current != nullptr) {
        if (current->data.trainId == t.trainId) {
            cout << "Train ID already exists!\n";
            return;
        }

        current = current->next;
    }

    cin.ignore(numeric_limits<streamsize>::max(), '\n');

    cout << "Enter Train Name: ";
    getline(cin, t.trainName);

    cout << "Enter Source Station ID: ";
    cin >> t.sourceStationId;

    cout << "Enter Destination Station ID: ";
    cin >> t.destinationStationId;

    cout << "Enter Total Seats: ";

    if (!(cin >> t.totalSeats) || t.totalSeats < 0) {
        cout << "Invalid seat count!\n";
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        return;
    }

    Node* newNode = new Node(t);

    if (head == nullptr) {
        head = newNode;
    } else {
        current = head;

        while (current->next != nullptr) {
            current = current->next;
        }

        current->next = newNode;
    }

    cout << "Train inserted successfully!\n";
}

// Display all trains
void TrainLinkedList::displayAllTrains() const {
    if (head == nullptr) {
        cout << "No trains available.\n";
        return;
    }

    Node* current = head;

    cout << "\n----- Train Inventory (Linked List) -----\n";

    while (current != nullptr) {
        cout << "\nTrain ID: " << current->data.trainId
             << "\nName: " << current->data.trainName
             << "\nFrom: " << current->data.sourceStationId
             << "\nTo: " << current->data.destinationStationId
             << "\nSeats: " << current->data.totalSeats
             << "\n";

        current = current->next;
    }
}

// Search for a train
void TrainLinkedList::searchTrain() const {
    string id;

    cout << "Enter Train ID to search: ";
    cin >> id;

    Node* current = head;

    while (current != nullptr) {
        if (current->data.trainId == id) {
            cout << "\nTrain Found!\n";

            cout << "Train ID: " << current->data.trainId
                 << "\nName: " << current->data.trainName
                 << "\nFrom: " << current->data.sourceStationId
                 << "\nTo: " << current->data.destinationStationId
                 << "\nSeats: " << current->data.totalSeats
                 << "\n";

            return;
        }

        current = current->next;
    }

    cout << "Train not found!\n";
}

// Update train details
void TrainLinkedList::updateTrain() {
    string id;

    cout << "Enter Train ID to update: ";
    cin >> id;

    Node* current = head;

    while (current != nullptr) {
        if (current->data.trainId == id) {
            cin.ignore(numeric_limits<streamsize>::max(), '\n');

            cout << "Enter New Train Name: ";
            getline(cin, current->data.trainName);

            cout << "Enter New Source Station ID: ";
            cin >> current->data.sourceStationId;

            cout << "Enter New Destination Station ID: ";
            cin >> current->data.destinationStationId;

            cout << "Enter New Total Seats: ";

            if (!(cin >> current->data.totalSeats) ||
                current->data.totalSeats < 0) {
                cout << "Invalid seat count!\n";
                cin.clear();
                cin.ignore(
                    numeric_limits<streamsize>::max(), '\n'
                );
                return;
            }

            cout << "Train updated successfully!\n";
            return;
        }

        current = current->next;
    }

    cout << "Train not found!\n";
}

// Delete a train
void TrainLinkedList::deleteTrain() {
    string id;

    cout << "Enter Train ID to delete: ";
    cin >> id;

    Node* current = head;
    Node* previous = nullptr;

    while (current != nullptr) {
        if (current->data.trainId == id) {
            if (previous == nullptr) {
                head = current->next;
            } else {
                previous->next = current->next;
            }

            delete current;

            cout << "Train deleted successfully!\n";
            return;
        }

        previous = current;
        current = current->next;
    }

    cout << "Train not found!\n";
}