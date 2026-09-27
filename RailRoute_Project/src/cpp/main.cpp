#include "data_structures/TrainArray.h"
#include "data_structures/TrainLinkedList.h"
#include "core/RouteManager.h"

#include <iostream>
#include <limits>
using namespace std;

void clearInput() {
    cin.clear();
    cin.ignore(numeric_limits<streamsize>::max(), '\n');
}

int readChoice() {
    int choice;

    if (!(cin >> choice)) {
        clearInput();
        return -1;
    }

    return choice;
}

int main() {
    // Task 1: Initialize array-based train records
    TrainArray trainArray;
    trainArray.loadHardcodedTrains();

    // Task 2: Copy initial records into linked list
    TrainLinkedList trains;

    for (int i = 0; i < trainArray.getTrainCount(); ++i) {
        trains.appendRecord(trainArray.getTrainAt(i));
    }

    // Initialize route manager
    RouteManager routeManager;
    routeManager.loadHardcodedStations();
    routeManager.loadHardcodedDistances();

    int choice;

    do {
        cout << "\n========== RAILROUTE ==========\n";
        cout << "1. Add Train\n";
        cout << "2. Display All Trains\n";
        cout << "3. Search Train\n";
        cout << "4. Update Train\n";
        cout << "5. Delete Train\n";
        cout << "6. Display Route Distance Matrix\n";
        cout << "7. Show Initial Array Records (Task 1)\n";
        cout << "0. Exit\n";
        cout << "Enter choice: ";

        choice = readChoice();

        switch (choice) {
            case 1:
                trains.insertTrain();
                break;

            case 2:
                trains.displayAllTrains();
                break;

            case 3:
                trains.searchTrain();
                break;

            case 4:
                trains.updateTrain();
                break;

            case 5:
                trains.deleteTrain();
                break;

            case 6:
                routeManager.displayDistanceMatrix();
                break;

            case 7:
                cout << "\n--- Initial Train Records Stored in Array ---\n";
                trainArray.displayAllTrains();
                break;

            case 0:
                cout << "Exiting RailRoute. Goodbye!\n";
                break;

            default:
                cout << "Invalid choice. Please enter a listed option.\n";
        }

    } while (choice != 0);

    return 0;
}