#include "RouteManager.h"
#include <iostream>
using namespace std;

RouteManager::RouteManager() {
    stationCount = 0;
    // Initialize matrix to 0 by default (overwritten by loadHardcodedDistances)
    for (int i = 0; i < MAX_STATIONS; i++) {
        for (int j = 0; j < MAX_STATIONS; j++) {
            distanceMatrix[i][j] = 0;
        }
    }
}

// Fills stationIds[] with predefined station IDs.
// Same IDs used across Java (Main.java) and C++ (TrainArray.cpp) for consistency.
void RouteManager::loadHardcodedStations() {
    stationIds[0] = "STN001"; // Pune Junction
    stationIds[1] = "STN002"; // Mumbai CST
    stationIds[2] = "STN003"; // Nagpur
    stationIds[3] = "STN004"; // Nashik Road
    stationIds[4] = "STN005"; // Solapur

    stationCount = 5;
}

// Fills the 2D adjacency matrix with hardcoded distances (in km).
// Matrix is symmetric since these are undirected physical tracks.
void RouteManager::loadHardcodedDistances() {
    // STN001-Pune, STN002-Mumbai, STN003-Nagpur, STN004-Nashik, STN005-Solapur
    int data[MAX_STATIONS][MAX_STATIONS] = {
        {0,   192, 720, 210, 256},
        {192, 0,   NO_DIRECT_ROUTE, 165, NO_DIRECT_ROUTE},
        {720, NO_DIRECT_ROUTE, 0,   NO_DIRECT_ROUTE, 615},
        {210, 165, NO_DIRECT_ROUTE, 0,   NO_DIRECT_ROUTE},
        {256, NO_DIRECT_ROUTE, 615, NO_DIRECT_ROUTE, 0}
    };

    for (int i = 0; i < MAX_STATIONS; i++) {
        for (int j = 0; j < MAX_STATIONS; j++) {
            distanceMatrix[i][j] = data[i][j];
        }
    }
}

int RouteManager::getStationIndex(const string& stationId) const {
    for (int i = 0; i < stationCount; i++) {
        if (stationIds[i] == stationId) {
            return i;
        }
    }
    return -1; // not found
}

void RouteManager::displayDistanceMatrix() const {
    cout << "----- Station Distance Matrix (2D Array) -----" << endl;
    cout << "\t";
    for (int i = 0; i < stationCount; i++) {
        cout << stationIds[i] << "\t";
    }
    cout << endl;

    for (int i = 0; i < stationCount; i++) {
        cout << stationIds[i] << "\t";
        for (int j = 0; j < stationCount; j++) {
            cout << distanceMatrix[i][j] << "\t";
        }
        cout << endl;
    }
}

int RouteManager::getDistance(const string& fromStationId, const string& toStationId) const {
    int fromIndex = getStationIndex(fromStationId);
    int toIndex = getStationIndex(toStationId);

    if (fromIndex == -1 || toIndex == -1) {
        return NO_DIRECT_ROUTE; // invalid station ID given
    }

    return distanceMatrix[fromIndex][toIndex];
}