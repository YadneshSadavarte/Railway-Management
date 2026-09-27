#include "TrainArray.h"
#include <iostream>
#include <limits>
using namespace std;

TrainArray::TrainArray() {
    trainCount = 0;
}

void TrainArray::loadHardcodedTrains() {
    trains[0] = {"TRN101", "Deccan Express", "STN001", "STN002", 100};
    trains[1] = {"TRN102", "Pune-Nagpur SF", "STN001", "STN003", 80};
    trains[2] = {"TRN103", "Mumbai-Pune Local", "STN002", "STN001", 150};
    trains[3] = {"TRN104", "Nagpur-Mumbai Exp", "STN003", "STN002", 120};

    trainCount = 4;
}

int TrainArray::findTrainIndex(const string& trainId) const {
    for (int i = 0; i < trainCount; i++) {
        if (trains[i].trainId == trainId) {
            return i;
        }
    }
    return -1;
}

void TrainArray::insertTrain() {
    if (trainCount >= MAX_TRAINS) {
        cout << "Train array is full!\n";
        return;
    }

    TrainRecord t;

    cout << "Enter Train ID: ";
    cin >> t.trainId;

    if (findTrainIndex(t.trainId) != -1) {
        cout << "Train ID already exists!\n";
        return;
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

    trains[trainCount] = t;
    trainCount++;

    cout << "Train inserted successfully!\n";
}

void TrainArray::displayAllTrains() const {
    if (trainCount == 0) {
        cout << "No trains available.\n";
        return;
    }

    cout << "\n----- Train Inventory (Array) -----\n";

    for (int i = 0; i < trainCount; i++) {
        cout << "\nTrain ID: " << trains[i].trainId
             << "\nName: " << trains[i].trainName
             << "\nFrom: " << trains[i].sourceStationId
             << "\nTo: " << trains[i].destinationStationId
             << "\nSeats: " << trains[i].totalSeats
             << "\n";
    }
}

void TrainArray::searchTrain() const {
    string id;

    cout << "Enter Train ID to search: ";
    cin >> id;

    int index = findTrainIndex(id);

    if (index == -1) {
        cout << "Train not found!\n";
        return;
    }

    cout << "\nTrain Found!\n";
    cout << "Train ID: " << trains[index].trainId
         << "\nName: " << trains[index].trainName
         << "\nFrom: " << trains[index].sourceStationId
         << "\nTo: " << trains[index].destinationStationId
         << "\nSeats: " << trains[index].totalSeats
         << "\n";
}

void TrainArray::updateTrain() {
    string id;

    cout << "Enter Train ID to update: ";
    cin >> id;

    int index = findTrainIndex(id);

    if (index == -1) {
        cout << "Train not found!\n";
        return;
    }

    cin.ignore(numeric_limits<streamsize>::max(), '\n');

    cout << "Enter New Train Name: ";
    getline(cin, trains[index].trainName);

    cout << "Enter New Source Station ID: ";
    cin >> trains[index].sourceStationId;

    cout << "Enter New Destination Station ID: ";
    cin >> trains[index].destinationStationId;

    cout << "Enter New Total Seats: ";
    if (!(cin >> trains[index].totalSeats) ||
        trains[index].totalSeats < 0) {
        cout << "Invalid seat count!\n";
        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        return;
    }

    cout << "Train updated successfully!\n";
}

void TrainArray::deleteTrain() {
    string id;

    cout << "Enter Train ID to delete: ";
    cin >> id;

    int index = findTrainIndex(id);

    if (index == -1) {
        cout << "Train not found!\n";
        return;
    }

    for (int i = index; i < trainCount - 1; i++) {
        trains[i] = trains[i + 1];
    }

    trainCount--;

    cout << "Train deleted successfully!\n";
}

int TrainArray::getTrainCount() const {
    return trainCount;
}

TrainRecord TrainArray::getTrainAt(int index) const {
    if (index >= 0 && index < trainCount) {
        return trains[index];
    }

    TrainRecord empty = {"", "", "", "", 0};
    return empty;
}