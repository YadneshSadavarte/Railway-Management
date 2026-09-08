#ifndef TRAIN_ARRAY_H
#define TRAIN_ARRAY_H

#include <string>
using namespace std;

// Simple struct representing one train record.
// Mirrors the fields used in Java's Train.java, kept plain (no STL containers)
// as required for Review 1 (Phase 1: static arrays only).
struct TrainRecord {
    string trainId;
    string trainName;
    string sourceStationId;
    string destinationStationId;
    int totalSeats;
};

const int MAX_TRAINS = 10;

class TrainArray {
private:
    TrainRecord trains[MAX_TRAINS]; // 1D array - fixed size, hardcoded inventory
    int trainCount;

public:
    TrainArray();

    void loadHardcodedTrains();       // fills the array with predefined train data
    void displayAllTrains() const;    // prints every train record
    int getTrainCount() const;
    TrainRecord getTrainAt(int index) const;
};

#endif