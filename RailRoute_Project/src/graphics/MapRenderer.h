#ifndef MAP_RENDERER_H
#define MAP_RENDERER_H

// Simple struct representing one station's position on the 2D map.
// Coordinates are in OpenGL world space, not real-world geography,
// since this is a simulated closed-loop network (Review 1: static only).
struct StationPoint {
    float x;
    float y;
    const char* label;
};

const int MAX_MAP_STATIONS = 5;
const int MAX_MAP_TRACKS = 5;

// Simple struct representing a track (edge) connecting two stations
// by their index into the stationPoints[] array.
struct TrackLine {
    int fromIndex;
    int toIndex;
};

class MapRenderer {
private:
    StationPoint stationPoints[MAX_MAP_STATIONS];
    TrackLine trackLines[MAX_MAP_TRACKS];
    int stationCount;
    int trackCount;

public:
    MapRenderer();

    void loadHardcodedMap();   // fills stationPoints[] and trackLines[] with static data
    void drawStations() const; // renders GL_POINTS for every station
    void drawTracks() const;   // renders GL_LINES for every track
    void render() const;       // calls drawStations() + drawTracks() together
};

#endif