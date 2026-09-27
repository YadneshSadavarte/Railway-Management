#ifndef ROUTE_MANAGER_H
#define ROUTE_MANAGER_H

#include <string>
using namespace std;

const int MAX_STATIONS = 5;
const int NO_DIRECT_ROUTE = -1;

class RouteManager {
private:
    // Node of the adjacency linked list
    struct Node {
        int stationIndex;
        int distance;
        Node* next;

        Node(int index, int dist) {
            stationIndex = index;
            distance = dist;
            next = nullptr;
        }
    };

    string stationIds[MAX_STATIONS];

    // Each station has a linked list of connected stations
    Node* adjacencyList[MAX_STATIONS];

    int stationCount;

    int getStationIndex(const string& stationId) const;

    void addConnection(int fromIndex, int toIndex, int distance);
    void clearRoutes();

public:
    RouteManager();
    ~RouteManager();

    // Prevent accidental shallow copying of linked-list pointers
    RouteManager(const RouteManager&) = delete;
    RouteManager& operator=(const RouteManager&) = delete;

    void loadHardcodedStations();
    void loadHardcodedDistances();

    void displayDistanceMatrix() const;

    int getDistance(
        const string& fromStationId,
        const string& toStationId
    ) const;
};

#endif