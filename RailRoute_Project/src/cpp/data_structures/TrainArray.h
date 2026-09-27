#ifndef TRAIN_ARRAY_H
#define TRAIN_ARRAY_H

#include <string>
using namespace std;

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
    TrainRecord trains[MAX_TRAINS];
    int trainCount;

    int findTrainIndex(const string& trainId) const;

public:
    TrainArray();

    void loadHardcodedTrains();
    void insertTrain();
    void displayAllTrains() const;
    void searchTrain() const;
    void updateTrain();
    void deleteTrain();

    int getTrainCount() const;
    TrainRecord getTrainAt(int index) const;
};

#endif