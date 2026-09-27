#ifndef MAP_RENDERER_H
#define MAP_RENDERER_H

#include "ClippingUtil.h"

struct StationPoint {
    float x;
    float y;
    const char* label;
};

const int MAX_MAP_STATIONS = 5;
const int MAX_MAP_TRACKS = 5;

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

    float panX;
    float panY;
    float zoomLevel;

public:
    MapRenderer();

    void loadHardcodedMap();

    void pan(float dx, float dy);
    void zoom(float factor);
    void resetView();
    void applyViewTransform() const;

    float getPanX() const { return panX; }
    float getPanY() const { return panY; }
    float getZoomLevel() const { return zoomLevel; }

    void drawStations() const;
    void drawTracks() const;
    void render() const;

    void printClippingStatus() const;
};

#endif