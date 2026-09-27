#include "MapRenderer.h"
#include <GL/glut.h>
#include <iostream>
#include <iomanip>

MapRenderer::MapRenderer() {
    stationCount = 0;
    trackCount = 0;
    panX = 0.0f;
    panY = 0.0f;
    zoomLevel = 1.0f;
}

void MapRenderer::loadHardcodedMap() {
    stationPoints[0] = {0.0f,  0.5f,  "Pune"};
    stationPoints[1] = {0.6f,  0.8f,  "Mumbai"};
    stationPoints[2] = {-0.7f, -0.3f, "Nagpur"};
    stationPoints[3] = {0.4f,  0.2f,  "Nashik"};
    stationPoints[4] = {-0.2f, -0.7f, "Solapur"};
    stationCount = 5;

    trackLines[0] = {0, 1};
    trackLines[1] = {0, 2};
    trackLines[2] = {0, 3};
    trackLines[3] = {0, 4};
    trackLines[4] = {2, 4};
    trackCount = 5;
}

void MapRenderer::pan(float dx, float dy) {
    panX += dx;
    panY += dy;
}

void MapRenderer::zoom(float factor) {
    zoomLevel *= factor;
    if (zoomLevel < 0.5f) {
        zoomLevel = 0.5f;
    }
    if (zoomLevel > 3.0f) {
        zoomLevel = 3.0f;
    }
}

void MapRenderer::resetView() {
    panX = 0.0f;
    panY = 0.0f;
    zoomLevel = 1.0f;
}

void MapRenderer::applyViewTransform() const {
    glTranslatef(panX, panY, 0.0f);
    glScalef(zoomLevel, zoomLevel, 1.0f);
}

void MapRenderer::drawStations() const {
    glColor3f(0.047f, 0.063f, 0.396f);
    glPointSize(8.0f);

    ClipWindow window = calculateVisibleBounds(panX, panY, zoomLevel);

    glBegin(GL_POINTS);
    for (int i = 0; i < stationCount; i++) {
        if (computeOutCode(stationPoints[i].x, stationPoints[i].y, window) == INSIDE) {
            glVertex2f(stationPoints[i].x, stationPoints[i].y);
        }
    }
    glEnd();
}

void MapRenderer::drawTracks() const {
    glColor3f(0.047f, 0.063f, 0.396f);
    glLineWidth(2.0f);

    ClipWindow window = calculateVisibleBounds(panX, panY, zoomLevel);

    glBegin(GL_LINES);
    for (int i = 0; i < trackCount; i++) {
        const StationPoint& from = stationPoints[trackLines[i].fromIndex];
        const StationPoint& to   = stationPoints[trackLines[i].toIndex];

        float x1 = from.x;
        float y1 = from.y;
        float x2 = to.x;
        float y2 = to.y;

        if (cohenSutherlandClip(x1, y1, x2, y2, window)) {
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
        }
    }
    glEnd();
}

void MapRenderer::render() const {
    glPushMatrix();
    applyViewTransform();
    drawTracks();
    drawStations();
    glPopMatrix();
}

void MapRenderer::printClippingStatus() const {
    ClipWindow win = calculateVisibleBounds(panX, panY, zoomLevel);
    std::cout << std::fixed << std::setprecision(2);
    std::cout << "\n===================================================================\n";
    std::cout << "[CO3 Transformation] Pan: (" << panX << ", " << panY << ") | Zoom: " << zoomLevel << "x\n";
    std::cout << "[CO5 Clip Window]   X: [" << win.xmin << ", " << win.xmax << "] | Y: [" << win.ymin << ", " << win.ymax << "]\n";
    std::cout << "---------------- Cohen-Sutherland Track Evaluation ----------------\n";

    for (int i = 0; i < trackCount; i++) {
        const StationPoint& from = stationPoints[trackLines[i].fromIndex];
        const StationPoint& to   = stationPoints[trackLines[i].toIndex];
        float x1 = from.x;
        float y1 = from.y;
        float x2 = to.x;
        float y2 = to.y;

        int initialCode1 = computeOutCode(x1, y1, win);
        int initialCode2 = computeOutCode(x2, y2, win);
        bool visible = cohenSutherlandClip(x1, y1, x2, y2, win);

        std::cout << "  Track " << i << " (" << std::setw(7) << from.label << " -> " << std::setw(7) << to.label << "): ";
        if (!visible) {
            std::cout << "REJECTED (Trivially outside; Outcodes: 0x" << std::hex << initialCode1
                      << " & 0x" << initialCode2 << std::dec << ")\n";
        } else if (x1 != from.x || y1 != from.y || x2 != to.x || y2 != to.y) {
            std::cout << "CLIPPED  -> [(" << from.x << ", " << from.y << ") -> (" << to.x << ", " << to.y << ")] "
                      << "trimmed to [(" << x1 << ", " << y1 << ") -> (" << x2 << ", " << y2 << ")]\n";
        } else {
            std::cout << "ACCEPTED (Fully inside viewport)\n";
        }
    }
    std::cout << "===================================================================\n";
}