#include "MapRenderer.h"
#include <GL/glut.h>

MapRenderer::MapRenderer() {
    stationCount = 0;
    trackCount = 0;
}

// Fills stationPoints[] and trackLines[] with predefined, static map data.
// Coordinates are simple OpenGL world-space values for Review 1 (no real geography).
void MapRenderer::loadHardcodedMap() {
    // Station layout roughly mirrors the C++ RouteManager's 5 stations
    stationPoints[0] = {0.0f,  0.5f,  "Pune"};      // STN001
    stationPoints[1] = {0.6f,  0.8f,  "Mumbai"};    // STN002
    stationPoints[2] = {-0.7f, -0.3f, "Nagpur"};    // STN003
    stationPoints[3] = {0.4f,  0.2f,  "Nashik"};    // STN004
    stationPoints[4] = {-0.2f, -0.7f, "Solapur"};   // STN005
    stationCount = 5;

    // Tracks connecting stations by index (matches RouteManager's direct-route pairs)
    trackLines[0] = {0, 1}; // Pune - Mumbai
    trackLines[1] = {0, 2}; // Pune - Nagpur
    trackLines[2] = {0, 3}; // Pune - Nashik
    trackLines[3] = {0, 4}; // Pune - Solapur
    trackLines[4] = {2, 4}; // Nagpur - Solapur
    trackCount = 5;
}

// Renders every station as a single GL_POINT.
void MapRenderer::drawStations() const {
    glColor3f(0.047f, 0.063f, 0.396f); // Primary navy blue (#0C1065) per Design.md
    glPointSize(8.0f);

    glBegin(GL_POINTS);
    for (int i = 0; i < stationCount; i++) {
        glVertex2f(stationPoints[i].x, stationPoints[i].y);
    }
    glEnd();
}

// Renders every track as a single GL_LINE between two station points.
void MapRenderer::drawTracks() const {
    glColor3f(0.047f, 0.063f, 0.396f); // Primary navy blue (#0C1065) per Design.md
    glLineWidth(2.0f);

    glBegin(GL_LINES);
    for (int i = 0; i < trackCount; i++) {
        const StationPoint& from = stationPoints[trackLines[i].fromIndex];
        const StationPoint& to   = stationPoints[trackLines[i].toIndex];
        glVertex2f(from.x, from.y);
        glVertex2f(to.x, to.y);
    }
    glEnd();
}

void MapRenderer::render() const {
    drawTracks();   // tracks first, so station points draw on top
    drawStations();
}