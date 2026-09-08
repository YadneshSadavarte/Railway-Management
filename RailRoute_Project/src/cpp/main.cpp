#include "data_structures/TrainArray.h"
#include "core/RouteManager.h"
#include <iostream>
using namespace std;

// Entry point for the RailRoute C++ backend (Review 1).
// Loads and displays the hardcoded train inventory (1D array)
// and the station distance matrix (2D array).
int main() {

    // ----- Train Inventory (1D Array) -----
    TrainArray trainArray;
    trainArray.loadHardcodedTrains();
    trainArray.displayAllTrains();

    cout << endl;

    // ----- Station Distance Matrix (2D Array) -----
    RouteManager routeManager;
    routeManager.loadHardcodedStations();
    routeManager.loadHardcodedDistances();
    routeManager.displayDistanceMatrix();

    cout << endl;

    // Quick lookup test: distance between Pune (STN001) and Mumbai (STN002)
    int distance = routeManager.getDistance("STN001", "STN002");
    cout << "Distance from STN001 to STN002: " << distance << " km" << endl;

    return 0;
}