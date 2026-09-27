#include "RouteManager.h"
#include <iostream>
using namespace std;

// Constructor
RouteManager::RouteManager() {
    stationCount = 0;

    for (int i = 0; i < MAX_STATIONS; i++) {
        adjacencyList[i] = nullptr;
    }
}

// Destructor
RouteManager::~RouteManager() {
    clearRoutes();
}

// Delete all linked-list nodes
void RouteManager::clearRoutes() {
    for (int i = 0; i < MAX_STATIONS; i++) {
        Node* current = adjacencyList[i];

        while (current != nullptr) {
            Node* temp = current;
            current = current->next;
            delete temp;
        }

        adjacencyList[i] = nullptr;
    }
}

// Load station IDs
void RouteManager::loadHardcodedStations() {
    stationIds[0] = "STN001"; // Pune Junction
    stationIds[1] = "STN002"; // Mumbai CST
    stationIds[2] = "STN003"; // Nagpur
    stationIds[3] = "STN004"; // Nashik Road
    stationIds[4] = "STN005"; // Solapur

    stationCount = 5;
}

// Find station index
int RouteManager::getStationIndex(
    const string& stationId
) const {
    for (int i = 0; i < stationCount; i++) {
        if (stationIds[i] == stationId) {
            return i;
        }
    }

    return -1;
}

// Add a connection to the adjacency linked list
void RouteManager::addConnection(
    int fromIndex,
    int toIndex,
    int distance
) {
    Node* newNode = new Node(toIndex, distance);

    // Insert at the beginning of the list
    newNode->next = adjacencyList[fromIndex];
    adjacencyList[fromIndex] = newNode;
}

// Load route distances into the linked lists
void RouteManager::loadHardcodedDistances() {
    clearRoutes();

    // Distances between stations
    int data[MAX_STATIONS][MAX_STATIONS] = {
        {0,   192, 720, 210, 256},
        {192, 0,   -1,  165, -1},
        {720, -1,  0,   -1,  615},
        {210, 165, -1,  0,   -1},
        {256, -1,  615, -1,  0}
    };

    // Build adjacency lists from the distance data
    for (int i = 0; i < stationCount; i++) {
        for (int j = 0; j < stationCount; j++) {

            if (i != j && data[i][j] != NO_DIRECT_ROUTE) {
                addConnection(i, j, data[i][j]);
            }
        }
    }
}

// Display distances in matrix format
void RouteManager::displayDistanceMatrix() const {
    cout << "\n----- Station Distance Matrix (Linked List) -----\n";

    cout << "\t";

    for (int i = 0; i < stationCount; i++) {
        cout << stationIds[i] << "\t";
    }

    cout << endl;

    for (int i = 0; i < stationCount; i++) {
        cout << stationIds[i] << "\t";

        for (int j = 0; j < stationCount; j++) {
            cout << getDistance(stationIds[i], stationIds[j]) << "\t";
        }

        cout << endl;
    }
}

// Get direct distance between two stations
int RouteManager::getDistance(
    const string& fromStationId,
    const string& toStationId
) const {
    int fromIndex = getStationIndex(fromStationId);
    int toIndex = getStationIndex(toStationId);

    if (fromIndex == -1 || toIndex == -1) {
        return NO_DIRECT_ROUTE;
    }

    // Distance from a station to itself
    if (fromIndex == toIndex) {
        return 0;
    }

    Node* current = adjacencyList[fromIndex];

    while (current != nullptr) {
        if (current->stationIndex == toIndex) {
            return current->distance;
        }

        current = current->next;
    }

    return NO_DIRECT_ROUTE;
}