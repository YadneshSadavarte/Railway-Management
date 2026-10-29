#ifndef MAP_RENDERER_H
#define MAP_RENDERER_H

#include "ClippingUtil.h"
#include <cstdio>

struct StationPoint {
    float x;
    float y;
    const char* id;
    const char* code;
    const char* label;
    int elevation;
};

struct TrackLine {
    int fromIndex;
    int toIndex;
    int distanceKm;
};

class MapRenderer {
public:
    static const int NUM_STATIONS = 32;
    static const int NUM_TRACKS = 46;
    static const int MAX_PATH_LEGS = 40;

    static MapRenderer& getInstance();

    ~MapRenderer();
    void init(const char* filepath = 0);
    void loadActiveRouteFromFile(const char* filepath);
    void switchRouteMode(bool shortest);
    void pan(float dx, float dy);
    void zoom(float factor);
    void resetView();
    void togglePause();
    void toggleLighting();
    void toggleTextures();
    void printClippingStatus() const;
    void updateAnimation(float deltaSeconds = 0.016f);
    void render() const;
    void setWindowDimensions(int w, int h);
    float getAspectRatio() const;

    static void rgbToHsv(float r, float g, float b, float& h, float& s, float& v);
    static void hsvToRgb(float h, float s, float v, float& r, float& g, float& b);

private:
    MapRenderer();

    StationPoint stations[NUM_STATIONS];
    TrackLine tracks[NUM_TRACKS];

    int shortestPath[MAX_PATH_LEGS];
    int shortestLegCount;
    int shortestDistance;

    int longestPath[MAX_PATH_LEGS];
    int longestLegCount;
    int longestDistance;

    int activePath[MAX_PATH_LEGS];
    int activeLegCount;
    int activeDistance;
    bool isShortestActive;

    float panX;
    float panY;
    float zoomLevel;

    float trainProgress;
    int currentLegIndex;
    float trainSpeed;
    bool isPaused;
    float pulsePhase;
    bool lightingEnabled;
    bool texturesEnabled;

    int windowWidth;
    int windowHeight;
    float aspectRatio;

    char activeRouteFilepath[512];

    unsigned int ballastTextureId;
    unsigned int metalTextureId;

    void initTextures();
    void cleanupTextures();

    void setupSceneLighting(float trainX, float trainY) const;
    void disableSceneLighting() const;

    void drawMapBoundary(const ClipWindow& win) const;
    void drawDistrictDemarcations(const ClipWindow& win) const;
    void drawTracks(const ClipWindow& win) const;
    void drawDoubleRailSegment(float x1, float y1, float x2, float y2) const;
    void drawActiveRoute(const ClipWindow& win) const;
    void drawStations(const ClipWindow& win) const;
    void drawStationGlow(float cx, float cy, float radius, float r, float g, float b) const;
    void drawTrain(const ClipWindow& win) const;
    void drawTrainCar(float x, float y, float angleDeg, float length, float width, bool isEngine) const;
    void drawHUD() const;
    void drawLegend() const;
    void drawSelectedRoutePanel() const;
    void drawString(float x, float y, const char* str) const;
    void drawStringWithShadow(float x, float y, const char* str, float r, float g, float b) const;
    void drawPill(float x, float y, float w, float h, float r, float g, float b, float a) const;
    void getStationLabelLayout(int idx, float& offX, float& offY, float& boxW, float& boxH) const;
    int findStationIndexByCodeOrId(const char* codeOrId) const;
    void readRouteLegs(FILE* fp, int count, int* path, int& legCount, int& dist);
    int getLegDistanceKm(int fromIdx, int toIdx) const;
};

#endif
