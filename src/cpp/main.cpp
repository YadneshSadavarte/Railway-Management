#include <iostream>
#include <string>
#include <iomanip> // For neat console layout and spacing

/**
 * RailRoute backend data layer - Phase 1: Arrays & Basic Static Data
 * 
 * This program establishes the core structs, 1D inventories, and the 2D network 
 * adjacency matrix representing distances between key stations. It verifies 
 * initialization by printing a Train Timetable and a Distance Matrix to the console.
 * 
 * Designed for 2nd-year B.Tech Data Structures / C++ Lab Review 1.
 */

// Define constants for array bounds to avoid dynamic allocations (complying with raw array constraint)
const int NUM_STATIONS = 5;
const int NUM_TRAINS = 5;

// =============================================================================
// STRUCT DEFINITIONS
// =============================================================================

/**
 * Represents a railway station.
 */
struct Station {
    std::string stationId;
    std::string name;
    int elevationInMeters;
};

/**
 * Represents a train service.
 */
struct Train {
    std::string trainNumber;
    std::string name;
    std::string source;
    std::string destination;
    int availableSeats;
};

// =============================================================================
// MAIN ENTRY POINT
// =============================================================================

int main() {
    std::cout << "==========================================================\n";
    std::cout << "      RAILROUTE BACKEND - PHASE 1: STATIC DATA LAYER      \n";
    std::cout << "==========================================================\n\n";

    // 1. Initialize Station List (1D Array of raw structs)
    Station stations[NUM_STATIONS] = {
        {"PNE", "Pune Junction", 560},
        {"LNL", "Lonavala", 624},
        {"CSTM", "Mumbai CSMT", 14},
        {"NK", "Nashik Road", 580},
        {"SUR", "Solapur Junction", 485}
    };

    // 2. Initialize Train Inventory (1D Array of raw structs)
    Train trains[NUM_TRAINS] = {
        {"12124", "Deccan Queen", "Pune Junction", "Mumbai CSMT", 120},
        {"11010", "Sinhagad Express", "Pune Junction", "Mumbai CSMT", 80},
        {"12110", "Panchavati Express", "Nashik Road", "Mumbai CSMT", 95},
        {"12116", "Siddheshwar Express", "Solapur Junction", "Mumbai CSMT", 110},
        {"22106", "Indrayani Express", "Pune Junction", "Mumbai CSMT", 75}
    };

    // 3. Initialize Distance Adjacency Matrix (2D Array)
    // Indices map to the stations array: 0=PNE, 1=LNL, 2=CSTM, 3=NK, 4=SUR
    // Value = Distance in Km. A value of -1.0 represents no direct track connection.
    double distanceMatrix[NUM_STATIONS][NUM_STATIONS] = {
        // PNE     LNL     CSTM    NK      SUR
        { 0.0,   64.0,   -1.0,   -1.0,   264.0 }, // PNE (Pune)
        { 64.0,   0.0,   64.0,   -1.0,    -1.0 }, // LNL (Lonavala)
        { -1.0,  64.0,    0.0,  188.0,    -1.0 }, // CSTM (Mumbai)
        { -1.0,  -1.0,  188.0,    0.0,    -1.0 }, // NK (Nashik)
        { 264.0, -1.0,   -1.0,   -1.0,     0.0 }  // SUR (Solapur)
    };

    // =========================================================================
    // OUTPUT REPORTING & VISUALIZATION
    // =========================================================================

    // Report A: Train Timetable
    std::cout << "--- REPORT A: TRAIN INVENTORY TIMETABLE ---\n";
    std::cout << std::left 
              << std::setw(10) << "Train #" 
              << std::setw(22) << "Train Name" 
              << std::setw(20) << "Source" 
              << std::setw(20) << "Destination" 
              << std::right << std::setw(15) << "Available Seats" << "\n";
    std::cout << std::string(87, '-') << "\n";

    for (int i = 0; i < NUM_TRAINS; ++i) {
        std::cout << std::left 
                  << std::setw(10) << trains[i].trainNumber 
                  << std::setw(22) << trains[i].name 
                  << std::setw(20) << trains[i].source 
                  << std::setw(20) << trains[i].destination 
                  << std::right << std::setw(15) << trains[i].availableSeats << "\n";
    }
    std::cout << "\n";

    // Report B: Station elevation report
    std::cout << "--- REPORT B: STATION ELEVATION DATA ---\n";
    std::cout << std::left 
              << std::setw(10) << "Code" 
              << std::setw(22) << "Station Name" 
              << std::right << std::setw(20) << "Elevation (meters)" << "\n";
    std::cout << std::string(52, '-') << "\n";
    for (int i = 0; i < NUM_STATIONS; ++i) {
        std::cout << std::left 
                  << std::setw(10) << stations[i].stationId 
                  << std::setw(22) << stations[i].name 
                  << std::right << std::setw(20) << stations[i].elevationInMeters << "\n";
    }
    std::cout << "\n";

    // Report C: Adjacency Matrix
    std::cout << "--- REPORT C: NETWORK DISTANCE ADJACENCY MATRIX (KM) ---\n";
    std::cout << "Note: '-1.0' indicates no direct railway track exists between stations.\n\n";

    // Print top horizontal header
    std::cout << std::left << std::setw(18) << "Stations";
    for (int i = 0; i < NUM_STATIONS; ++i) {
        std::cout << std::right << std::setw(10) << stations[i].stationId;
    }
    std::cout << "\n" << std::string(68, '-') << "\n";

    // Print matrix grid
    for (int r = 0; r < NUM_STATIONS; ++r) {
        // Label the row with the station code and name
        std::string rowLabel = stations[r].stationId + " (" + stations[r].name.substr(0, 4) + ")";
        std::cout << std::left << std::setw(18) << rowLabel;
        
        for (int c = 0; c < NUM_STATIONS; ++c) {
            std::cout << std::right << std::setw(10) << std::fixed << std::setprecision(1) << distanceMatrix[r][c];
        }
        std::cout << "\n";
    }
    
    std::cout << "\n==========================================================\n";
    std::cout << "        END OF PHASE 1 STATIC DATA LAYER DEMO             \n";
    std::cout << "==========================================================\n";

    return 0;
}
