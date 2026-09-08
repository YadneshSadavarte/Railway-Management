#ifndef ROUTE_MANAGER_H
#define ROUTE_MANAGER_H

#include <string>
using namespace std;

const int MAX_STATIONS = 5;
const int NO_DIRECT_ROUTE = -1; // marks two stations with no direct track between them

class RouteManager {
private:
    string stationIds[MAX_STATIONS];                  // e.g. "STN001", "STN002", ...
    int distanceMatrix[MAX_STATIONS][MAX_STATIONS];    // 2D array - adjacency matrix (distance in km)
    int stationCount;

    int getStationIndex(const string& stationId) const; // helper: finds row/col index for a station ID

public:
    RouteManager();

    void loadHardcodedStations();     // fills stationIds[] with predefined station data
    void loadHardcodedDistances();    // fills distanceMatrix[][] with predefined distances
    void displayDistanceMatrix() const;
    int getDistance(const string& fromStationId, const string& toStationId) const;
};

#endif