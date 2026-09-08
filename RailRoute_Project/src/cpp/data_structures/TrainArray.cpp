#include "TrainArray.h"
#include <iostream>
using namespace std;

TrainArray::TrainArray() {
    trainCount = 0;
}

// Fills the array with predefined, hardcoded train data.
// No file I/O or live input for Review 1 - strictly static simulation data.
void TrainArray::loadHardcodedTrains() {
    trains[0] = {"TRN101", "Deccan Express", "STN001", "STN002", 100};
    trains[1] = {"TRN102", "Pune-Nagpur SF", "STN001", "STN003", 80};
    trains[2] = {"TRN103", "Mumbai-Pune Local", "STN002", "STN001", 150};
    trains[3] = {"TRN104", "Nagpur-Mumbai Exp", "STN003", "STN002", 120};

    trainCount = 4;
}

void TrainArray::displayAllTrains() const {
    cout << "----- Train Inventory (1D Array) -----" << endl;
    for (int i = 0; i < trainCount; i++) {
        cout << "Train ID: " << trains[i].trainId
             << " | Name: " << trains[i].trainName
             << " | From: " << trains[i].sourceStationId
             << " | To: " << trains[i].destinationStationId
             << " | Seats: " << trains[i].totalSeats
             << endl;
    }
}

int TrainArray::getTrainCount() const {
    return trainCount;
}

TrainRecord TrainArray::getTrainAt(int index) const {
    if (index >= 0 && index < trainCount) {
        return trains[index];
    }
    // Return an empty record if the index is invalid.
    // Full custom exception handling is a later-phase concern (Java side, Week 4-5).
    TrainRecord empty = {"", "", "", "", 0};
    return empty;
}