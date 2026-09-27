#ifndef TRAIN_LINKED_LIST_H
#define TRAIN_LINKED_LIST_H

#include "TrainArray.h"

class TrainLinkedList {
private:
    struct Node {
        TrainRecord data;
        Node* next;

        Node(const TrainRecord& train) {
            data = train;
            next = nullptr;
        }
    };

    Node* head;

public:
    TrainLinkedList();
    ~TrainLinkedList();

    void appendRecord(const TrainRecord& train);

    void insertTrain();
    void displayAllTrains() const;
    void searchTrain() const;
    void updateTrain();
    void deleteTrain();
};

#endif