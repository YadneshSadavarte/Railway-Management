#include "MapRenderer.h"
#include <GL/freeglut.h>
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <cstring>

const float PI = 3.14159265358979323846f;

MapRenderer::MapRenderer() {
    shortestLegCount = 0;
    shortestDistance = 0;
    longestLegCount = 0;
    longestDistance = 0;
    activeLegCount = 0;
    activeDistance = 0;
    isShortestActive = true;

    panX = 0.0f;
    panY = 0.0f;
    zoomLevel = 1.0f;

    trainProgress = 0.0f;
    currentLegIndex = 0;
    trainSpeed = 0.008f;
    isPaused = false;
    pulsePhase = 0.0f;
    lightingEnabled = true;
    texturesEnabled = true;

    windowWidth = 1024;
    windowHeight = 768;
    aspectRatio = 1024.0f / 768.0f;

    ballastTextureId = 0;
    metalTextureId = 0;
    activeRouteFilepath[0] = '\0';
    for (int i = 0; i < MAX_PATH_LEGS; i++) {
        shortestPath[i] = -1;
        longestPath[i] = -1;
        activePath[i] = -1;
    }

    stations[0]  = { -0.496f,  0.784f, "MH-001", "NDB",  "Nandurbar (500m)", 500 };
    stations[1]  = { -0.383f,  0.641f, "MH-002", "DHI",  "Dhule (500m)", 500 };
    stations[2]  = { -0.212f,  0.679f, "MH-003", "JL",   "Jalgaon (500m)", 500 };
    stations[3]  = {  0.098f,  0.593f, "MH-004", "AK",   "Akola (500m)", 500 };
    stations[4]  = {  0.260f,  0.654f, "MH-005", "AMI",  "Amravati (500m)", 500 };
    stations[5]  = {  0.546f,  0.719f, "MH-006", "NGP",  "Nagpur (310m)", 310 };
    stations[6]  = {  0.667f,  0.744f, "MH-007", "BRD",  "Bhandara Rd (500m)", 500 };
    stations[7]  = {  0.783f,  0.810f, "MH-008", "G",    "Gondia (500m)", 500 };
    stations[8]  = { -0.583f,  0.366f, "MH-009", "NK",   "Nashik Rd (565m)", 565 };
    stations[9]  = { -0.813f,  0.293f, "MH-010", "PLG",  "Palghar (500m)", 500 };
    stations[10] = { -0.769f,  0.142f, "MH-011", "TNA",  "Thane (9m)", 9 };
    stations[11] = { -0.798f,  0.104f, "MH-012", "BA",   "Bandra (500m)", 500 };
    stations[12] = { -0.799f,  0.070f, "MH-013", "CSMT", "Mumbai CSMT (8m)", 8 };
    stations[13] = { -0.576f, -0.050f, "MH-014", "PUNE", "Pune (559m)", 559 };
    stations[14] = { -0.393f,  0.110f, "MH-015", "ANG",  "Ahilyanagar (500m)", 500 };
    stations[15] = { -0.176f,  0.080f, "MH-016", "BEED", "Beed (500m)", 500 };
    stations[16] = { -0.267f,  0.340f, "MH-017", "AWB",  "Aurangabad (500m)", 500 };
    stations[17] = { -0.141f,  0.331f, "MH-018", "J",    "Jalna (500m)", 500 };
    stations[18] = {  0.129f,  0.411f, "MH-019", "WHM",  "Washim (500m)", 500 };
    stations[19] = {  0.128f,  0.301f, "MH-020", "HNL",  "Hingoli (500m)", 500 };
    stations[20] = {  0.440f,  0.596f, "MH-021", "WR",   "Wardha (500m)", 500 };
    stations[21] = {  0.592f,  0.369f, "MH-022", "CD",   "Chandrapur (500m)", 500 };
    stations[22] = {  0.048f,  0.163f, "MH-023", "PBN",  "Parbhani (500m)", 500 };
    stations[23] = {  0.164f,  0.135f, "MH-024", "NED",  "Nanded (500m)", 500 };
    stations[24] = {  0.001f, -0.079f, "MH-025", "LUR",  "Latur (500m)", 500 };
    stations[25] = { -0.113f, -0.132f, "MH-026", "UMD",  "Usmanabad (500m)", 500 };
    stations[26] = { -0.141f, -0.304f, "MH-027", "SUR",  "Solapur (457m)", 457 };
    stations[27] = { -0.535f, -0.297f, "MH-028", "STR",  "Satara (500m)", 500 };
    stations[28] = { -0.687f, -0.497f, "MH-029", "RN",   "Ratnagiri (500m)", 500 };
    stations[29] = { -0.422f, -0.540f, "MH-030", "SLI",  "Sangli (500m)", 500 };
    stations[30] = { -0.498f, -0.585f, "MH-031", "KOP",  "Kolhapur (500m)", 500 };
    stations[31] = { -0.618f, -0.786f, "MH-032", "KUDL", "Kudal (500m)", 500 };

    tracks[0]  = { 0, 1, 95 };
    tracks[1]  = { 1, 2, 60 };
    tracks[2]  = { 2, 3, 160 };
    tracks[3]  = { 3, 4, 85 };
    tracks[4]  = { 4, 5, 155 };
    tracks[5]  = { 4, 20, 100 };
    tracks[6]  = { 3, 20, 170 };
    tracks[7]  = { 20, 5, 78 };
    tracks[8]  = { 5, 6, 62 };
    tracks[9]  = { 6, 7, 68 };
    tracks[10] = { 20, 21, 125 };
    tracks[11] = { 9, 10, 60 };
    tracks[12] = { 10, 11, 25 };
    tracks[13] = { 11, 12, 15 };
    tracks[14] = { 10, 8, 140 };
    tracks[15] = { 8, 2, 225 };
    tracks[16] = { 10, 13, 150 };
    tracks[17] = { 12, 13, 192 };
    tracks[18] = { 13, 27, 115 };
    tracks[19] = { 27, 29, 115 };
    tracks[20] = { 29, 30, 48 };
    tracks[21] = { 13, 14, 120 };
    tracks[22] = { 14, 15, 110 };
    tracks[23] = { 14, 16, 115 };
    tracks[24] = { 8, 16, 180 };
    tracks[25] = { 2, 16, 155 };
    tracks[26] = { 16, 17, 65 };
    tracks[27] = { 17, 22, 115 };
    tracks[28] = { 22, 23, 35 };
    tracks[29] = { 22, 19, 80 };
    tracks[30] = { 19, 18, 45 };
    tracks[31] = { 18, 3, 80 };
    tracks[32] = { 15, 22, 125 };
    tracks[33] = { 15, 24, 120 };
    tracks[34] = { 13, 26, 260 };
    tracks[35] = { 14, 26, 210 };
    tracks[36] = { 26, 25, 70 };
    tracks[37] = { 25, 24, 75 };
    tracks[38] = { 24, 22, 120 };
    tracks[39] = { 23, 21, 210 };
    tracks[40] = { 26, 29, 150 };
    tracks[41] = { 10, 28, 330 };
    tracks[42] = { 28, 31, 130 };
    tracks[43] = { 27, 28, 160 };
    tracks[44] = { 30, 31, 140 };
    tracks[45] = { 23, 24, 130 };
}

MapRenderer& MapRenderer::getInstance() {
    static MapRenderer instance;
    return instance;
}

MapRenderer::~MapRenderer() {
    cleanupTextures();
}

void MapRenderer::cleanupTextures() {
    if (ballastTextureId != 0) {
        glDeleteTextures(1, &ballastTextureId);
        ballastTextureId = 0;
    }
    if (metalTextureId != 0) {
        glDeleteTextures(1, &metalTextureId);
        metalTextureId = 0;
    }
}

static void setupMipmappedTexture(unsigned int& texId, const unsigned char* data, int w, int h) {
    glGenTextures(1, &texId);
    glBindTexture(GL_TEXTURE_2D, texId);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    gluBuild2DMipmaps(GL_TEXTURE_2D, GL_RGB, w, h, GL_RGB, GL_UNSIGNED_BYTE, data);
}

void MapRenderer::initTextures() {
    const int texW = 128;
    const int texH = 128;
    unsigned char ballastData[texW * texH * 3];
    unsigned int seed = 123456789;

    for (int y = 0; y < texH; y++) {
        for (int x = 0; x < texW; x++) {
            seed = seed * 1103515245 + 12345;
            int n1 = ((seed >> 16) % 70) - 35;
            seed = seed * 1103515245 + 12345;
            int n2 = ((seed >> 16) % 36) - 18;

            int stoneGridX = (x / 8) % 2;
            int stoneGridY = (y / 8) % 2;
            int edgeShadow = (x % 8 == 0 || y % 8 == 0) ? -45 : 0;
            int facetLight = (stoneGridX == stoneGridY) ? 28 : -20;
            int speckle = ((seed >> 24) % 10 == 0) ? 40 : (((seed >> 24) % 10 == 1) ? -40 : 0);

            int r = 142 + n1 + n2 + edgeShadow + facetLight + speckle;
            int g = 136 + n1 + edgeShadow + facetLight + speckle;
            int b = 128 + n1 - n2 + edgeShadow + facetLight + speckle;

            if (r < 25) r = 25; else if (r > 245) r = 245;
            if (g < 25) g = 25; else if (g > 245) g = 245;
            if (b < 25) b = 25; else if (b > 245) b = 245;

            int idx = (y * texW + x) * 3;
            ballastData[idx]     = (unsigned char)r;
            ballastData[idx + 1] = (unsigned char)g;
            ballastData[idx + 2] = (unsigned char)b;
        }
    }
    setupMipmappedTexture(ballastTextureId, ballastData, texW, texH);

    unsigned char metalData[texW * texH * 3];
    seed = 987654321;
    for (int y = 0; y < texH; y++) {
        float yNorm = (float)y / (float)texH;
        float specularCylinder = sin(yNorm * PI);
        int specGlint = (int)(specularCylinder * 55.0f);

        seed = seed * 1103515245 + 12345;
        int rowNoise = ((seed >> 16) % 24) - 12;

        for (int x = 0; x < texW; x++) {
            seed = seed * 1103515245 + 12345;
            int fineBrush = ((seed >> 16) % 14) - 7;

            int r = 188 + specGlint + rowNoise + fineBrush;
            int g = 202 + specGlint + rowNoise + fineBrush;
            int b = 224 + specGlint + rowNoise + fineBrush;

            if (r < 30) r = 30; else if (r > 255) r = 255;
            if (g < 30) g = 30; else if (g > 255) g = 255;
            if (b < 30) b = 30; else if (b > 255) b = 255;

            int idx = (y * texW + x) * 3;
            metalData[idx]     = (unsigned char)r;
            metalData[idx + 1] = (unsigned char)g;
            metalData[idx + 2] = (unsigned char)b;
        }
    }
    setupMipmappedTexture(metalTextureId, metalData, texW, texH);
    glBindTexture(GL_TEXTURE_2D, 0);
}

void MapRenderer::init(const char* filepath) {
    initTextures();
    if (filepath && filepath[0] != '\0') {
        loadActiveRouteFromFile(filepath);
    }
    if (activeLegCount < 2) {
        loadActiveRouteFromFile("data/active_route.txt");
    }
    if (activeLegCount < 2) {
        loadActiveRouteFromFile("../data/active_route.txt");
    }
    if (activeLegCount < 2) {
        loadActiveRouteFromFile("../../data/active_route.txt");
    }
    if (activeLegCount < 2) {
        loadActiveRouteFromFile("RailRoute_Final/data/active_route.txt");
    }
    printClippingStatus();
}

int MapRenderer::findStationIndexByCodeOrId(const char* codeOrId) const {
    if (!codeOrId) return -1;
    for (int i = 0; i < NUM_STATIONS; i++) {
        if (_stricmp(stations[i].code, codeOrId) == 0 || _stricmp(stations[i].id, codeOrId) == 0) {
            return i;
        }
    }
    return -1;
}

int MapRenderer::getLegDistanceKm(int fromIdx, int toIdx) const {
    for (int i = 0; i < NUM_TRACKS; i++) {
        if ((tracks[i].fromIndex == fromIdx && tracks[i].toIndex == toIdx) ||
            (tracks[i].fromIndex == toIdx && tracks[i].toIndex == fromIdx)) {
            return tracks[i].distanceKm;
        }
    }
    float dx = stations[toIdx].x - stations[fromIdx].x;
    float dy = stations[toIdx].y - stations[fromIdx].y;
    int est = (int)(sqrt(dx * dx + dy * dy) * 1150.0f);
    return (est > 15) ? est : 45;
}

void MapRenderer::readRouteLegs(FILE* fp, int count, int* path, int& legCount, int& dist) {
    int maxLegs = (count < MAX_PATH_LEGS) ? count : MAX_PATH_LEGS;
    char line[256];
    int validCount = 0;
    dist = 0;
    for (int i = 0; i < maxLegs; i++) {
        if (!fgets(line, sizeof(line), fp)) break;
        float ox, oy;
        char code[32];
        int cumDist;
        if (sscanf(line, "%f %f %31s %d", &ox, &oy, code, &cumDist) >= 3) {
            int idx = findStationIndexByCodeOrId(code);
            if (idx == -1) {
                for (int s = 0; s < NUM_STATIONS; s++) {
                    if (fabs(stations[s].x - ox) < 0.005f && fabs(stations[s].y - oy) < 0.005f) {
                        idx = s;
                        break;
                    }
                }
            }
            if (idx != -1) {
                path[validCount++] = idx;
            }
            dist = cumDist;
        }
    }
    legCount = validCount;
}

void MapRenderer::loadActiveRouteFromFile(const char* filepath) {
    shortestLegCount = 0;
    shortestDistance = 0;
    longestLegCount = 0;
    longestDistance = 0;
    activeLegCount = 0;
    activeDistance = 0;

    if (!filepath || filepath[0] == '\0') {
        switchRouteMode(true);
        return;
    }
    FILE* fp = fopen(filepath, "r");
    if (!fp) {
        switchRouteMode(true);
        return;
    }

    strncpy(activeRouteFilepath, filepath, sizeof(activeRouteFilepath) - 1);
    activeRouteFilepath[sizeof(activeRouteFilepath) - 1] = '\0';

    char line[256];
    char mode[64];
    int count = 0;
    mode[0] = '\0';
    int parsedShortest = 0;
    int parsedLongest = 0;

    while (fgets(line, sizeof(line), fp)) {
        if (sscanf(line, "ACTIVE_MODE %63s", mode) == 1) {
            isShortestActive = (_stricmp(mode, "LONGEST") != 0);
        } else if (sscanf(line, "SHORTEST_ROUTE %d", &count) == 1) {
            readRouteLegs(fp, count, shortestPath, shortestLegCount, shortestDistance);
            parsedShortest = shortestLegCount;
        } else if (sscanf(line, "LONGEST_ROUTE %d", &count) == 1) {
            readRouteLegs(fp, count, longestPath, longestLegCount, longestDistance);
            parsedLongest = longestLegCount;
        } else if (sscanf(line, "SINGLE_ROUTE %d", &count) == 1) {
            if (_stricmp(mode, "SINGLE_ROUTE") == 0 || parsedShortest == 0) {
                readRouteLegs(fp, count, shortestPath, shortestLegCount, shortestDistance);
                parsedShortest = shortestLegCount;
                longestLegCount = shortestLegCount;
                longestDistance = shortestDistance;
                for (int i = 0; i < shortestLegCount; i++) {
                    longestPath[i] = shortestPath[i];
                }
            } else {
                int dummyPath[MAX_PATH_LEGS];
                int dummyCount = 0;
                int dummyDist = 0;
                readRouteLegs(fp, count, dummyPath, dummyCount, dummyDist);
            }
        }
    }

    fclose(fp);

    if (parsedLongest == 0 && parsedShortest > 0) {
        longestLegCount = shortestLegCount;
        longestDistance = shortestDistance;
        for (int i = 0; i < shortestLegCount; i++) {
            longestPath[i] = shortestPath[i];
        }
    } else if (parsedShortest == 0 && parsedLongest > 0) {
        shortestLegCount = longestLegCount;
        shortestDistance = longestDistance;
        for (int i = 0; i < longestLegCount; i++) {
            shortestPath[i] = longestPath[i];
        }
    }

    switchRouteMode(isShortestActive);
}

void MapRenderer::switchRouteMode(bool shortest) {
    isShortestActive = shortest;
    int* src = isShortestActive ? shortestPath : longestPath;
    activeLegCount = isShortestActive ? shortestLegCount : longestLegCount;
    activeDistance = isShortestActive ? shortestDistance : longestDistance;
    for (int i = 0; i < activeLegCount; i++) {
        activePath[i] = src[i];
    }
    trainProgress = 0.0f;
    currentLegIndex = 0;
}

void MapRenderer::pan(float dx, float dy) {
    panX += dx / zoomLevel;
    panY += dy / zoomLevel;
}

void MapRenderer::zoom(float factor) {
    zoomLevel *= factor;
    if (zoomLevel < 0.5f) zoomLevel = 0.5f;
    if (zoomLevel > 4.5f) zoomLevel = 4.5f;
}

void MapRenderer::resetView() {
    panX = 0.0f;
    panY = 0.0f;
    zoomLevel = 1.0f;
    trainProgress = 0.0f;
    currentLegIndex = 0;
    if (activeRouteFilepath[0] != '\0') {
        loadActiveRouteFromFile(activeRouteFilepath);
    }
}

void MapRenderer::togglePause() {
    isPaused = !isPaused;
}

void MapRenderer::toggleLighting() {
    lightingEnabled = !lightingEnabled;
}

void MapRenderer::toggleTextures() {
    texturesEnabled = !texturesEnabled;
}

void MapRenderer::setWindowDimensions(int w, int h) {
    windowWidth = (w > 0) ? w : 1;
    windowHeight = (h > 0) ? h : 1;
    aspectRatio = (float)windowWidth / (float)windowHeight;
}

float MapRenderer::getAspectRatio() const {
    return aspectRatio;
}

void MapRenderer::rgbToHsv(float r, float g, float b, float& h, float& s, float& v) {
    float maxVal = r;
    if (g > maxVal) maxVal = g;
    if (b > maxVal) maxVal = b;

    float minVal = r;
    if (g < minVal) minVal = g;
    if (b < minVal) minVal = b;

    float delta = maxVal - minVal;
    v = maxVal;

    if (maxVal != 0.0f) {
        s = delta / maxVal;
    } else {
        s = 0.0f;
        h = 0.0f;
        return;
    }

    if (delta == 0.0f) {
        h = 0.0f;
        return;
    }

    if (r == maxVal) {
        h = (g - b) / delta;
    } else if (g == maxVal) {
        h = 2.0f + (b - r) / delta;
    } else {
        h = 4.0f + (r - g) / delta;
    }

    h *= 60.0f;
    if (h < 0.0f) h += 360.0f;
}

void MapRenderer::hsvToRgb(float h, float s, float v, float& r, float& g, float& b) {
    if (s == 0.0f) {
        r = g = b = v;
        return;
    }

    h /= 60.0f;
    int i = (int)floor(h);
    float f = h - i;
    float p = v * (1.0f - s);
    float q = v * (1.0f - s * f);
    float t = v * (1.0f - s * (1.0f - f));

    switch (i % 6) {
        case 0: r = v; g = t; b = p; break;
        case 1: r = q; g = v; b = p; break;
        case 2: r = p; g = v; b = t; break;
        case 3: r = p; g = q; b = v; break;
        case 4: r = t; g = p; b = v; break;
        case 5: r = v; g = p; b = q; break;
        default: r = g = b = 0.0f; break;
    }
}

void MapRenderer::printClippingStatus() const {
    ClipWindow win = calculateVisibleBounds(panX, panY, zoomLevel, aspectRatio);
    printf("========================================================================================\n");
    printf("   CO5: COHEN-SUTHERLAND 2D VIEWPORT LINE CLIPPING TELEMETRY STREAM                     \n");
    printf("   Visible Viewport Window Bounds: [Xmin=%.3f, Ymin=%.3f, Xmax=%.3f, Ymax=%.3f]         \n",
           win.xmin, win.ymin, win.xmax, win.ymax);
    printf("========================================================================================\n");

    int accepted = 0;
    int clipped = 0;
    int rejected = 0;

    for (int i = 0; i < NUM_TRACKS; i++) {
        float x1 = stations[tracks[i].fromIndex].x;
        float y1 = stations[tracks[i].fromIndex].y;
        float x2 = stations[tracks[i].toIndex].x;
        float y2 = stations[tracks[i].toIndex].y;

        float ox1 = x1, oy1 = y1, ox2 = x2, oy2 = y2;
        int code1 = computeOutCode(x1, y1, win);
        int code2 = computeOutCode(x2, y2, win);

        bool visible = cohenSutherlandClip(x1, y1, x2, y2, win);

        if (!visible) {
            rejected++;
            printf("Track %2d [%-4s -> %-4s]: REJECTED   (Codes: %d, %d) Outside Viewport\n",
                   i + 1, stations[tracks[i].fromIndex].code, stations[tracks[i].toIndex].code, code1, code2);
        } else if (ox1 == x1 && oy1 == y1 && ox2 == x2 && oy2 == y2) {
            accepted++;
            printf("Track %2d [%-4s -> %-4s]: ACCEPTED   (Trivially visible inside viewport)\n",
                   i + 1, stations[tracks[i].fromIndex].code, stations[tracks[i].toIndex].code);
        } else {
            clipped++;
            printf("Track %2d [%-4s -> %-4s]: CLIPPED    Boundary intersection calculated\n",
                   i + 1, stations[tracks[i].fromIndex].code, stations[tracks[i].toIndex].code);
        }
    }
    printf("----------------------------------------------------------------------------------------\n");
    printf("Summary: Accepted: %d | Clipped: %d | Rejected: %d | Total Tracks: %d\n",
           accepted, clipped, rejected, NUM_TRACKS);
    printf("========================================================================================\n\n");
    fflush(stdout);
}

void MapRenderer::updateAnimation(float deltaSeconds) {
    pulsePhase += deltaSeconds * 3.5f;
    if (pulsePhase > 2.0f * PI) {
        pulsePhase -= 2.0f * PI;
    }

    if (isPaused || activeLegCount < 2) {
        return;
    }

    float speedFactor = (deltaSeconds > 0.0f) ? (deltaSeconds / 0.016f) : 1.0f;
    if (speedFactor > 3.0f) speedFactor = 1.0f;
    trainProgress += trainSpeed * speedFactor;

    if (trainProgress >= 1.0f) {
        trainProgress = 0.0f;
        currentLegIndex++;
        if (currentLegIndex >= activeLegCount - 1) {
            currentLegIndex = 0;
        }
    }
}

void MapRenderer::drawString(float x, float y, const char* str) const {
    if (!str) return;
    glRasterPos2f(x, y);
    while (*str) {
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_10, *str);
        str++;
    }
}

void MapRenderer::drawStringWithShadow(float x, float y, const char* str, float r, float g, float b) const {
    if (!str) return;
    glColor4f(0.02f, 0.03f, 0.10f, 0.85f);
    drawString(x + 1.0f, y - 1.0f, str);
    glColor3f(r, g, b);
    drawString(x, y, str);
}

void MapRenderer::drawPill(float x, float y, float w, float h, float r, float g, float b, float a) const {
    glColor4f(r, g, b, a);
    glBegin(GL_QUADS);
    glVertex2f(x, y);
    glVertex2f(x + w, y);
    glVertex2f(x + w, y + h);
    glVertex2f(x, y + h);
    glEnd();
}

void MapRenderer::getStationLabelLayout(int idx, float& offX, float& offY, float& boxW, float& boxH) const {
    int len = (int)strlen(stations[idx].label);
    boxW = (float)len * 0.0072f + 0.012f;
    boxH = 0.020f;
    offX = 0.016f;
    offY = -0.004f;

    switch (idx) {
        case 0:
            offX = -boxW - 0.012f;
            offY = 0.012f;
            break;
        case 1:
            offX = -boxW - 0.012f;
            offY = -0.018f;
            break;
        case 2:
            offX = 0.016f;
            offY = 0.012f;
            break;
        case 3:
            offX = -boxW - 0.012f;
            offY = -0.018f;
            break;
        case 4:
            offX = 0.016f;
            offY = 0.010f;
            break;
        case 5:
            offX = -boxW - 0.012f;
            offY = 0.012f;
            break;
        case 6:
            offX = 0.016f;
            offY = -0.018f;
            break;
        case 7:
            offX = -boxW - 0.012f;
            offY = 0.012f;
            break;
        case 8:
            offX = -boxW - 0.012f;
            offY = 0.012f;
            break;
        case 9:
            offX = -boxW - 0.012f;
            offY = 0.004f;
            break;
        case 10:
            offX = 0.016f;
            offY = 0.012f;
            break;
        case 11:
            offX = -boxW - 0.012f;
            offY = -0.004f;
            break;
        case 12:
            offX = -boxW - 0.012f;
            offY = -0.022f;
            break;
        case 13:
            offX = -boxW - 0.012f;
            offY = -0.004f;
            break;
        case 14:
            offX = -boxW - 0.012f;
            offY = -0.020f;
            break;
        case 15:
            offX = 0.016f;
            offY = -0.018f;
            break;
        case 16:
            offX = -boxW - 0.012f;
            offY = 0.014f;
            break;
        case 17:
            offX = 0.016f;
            offY = 0.012f;
            break;
        case 18:
            offX = 0.016f;
            offY = 0.010f;
            break;
        case 19:
            offX = 0.016f;
            offY = -0.018f;
            break;
        case 20:
            offX = 0.016f;
            offY = -0.018f;
            break;
        case 21:
            offX = 0.016f;
            offY = 0.008f;
            break;
        case 22:
            offX = -boxW - 0.012f;
            offY = 0.012f;
            break;
        case 23:
            offX = 0.016f;
            offY = 0.010f;
            break;
        case 24:
            offX = 0.016f;
            offY = 0.008f;
            break;
        case 25:
            offX = -boxW - 0.012f;
            offY = -0.020f;
            break;
        case 26:
            offX = 0.016f;
            offY = 0.008f;
            break;
        case 27:
            offX = -boxW - 0.012f;
            offY = -0.004f;
            break;
        case 28:
            offX = -boxW - 0.012f;
            offY = -0.004f;
            break;
        case 29:
            offX = 0.016f;
            offY = 0.010f;
            break;
        case 30:
            offX = -boxW - 0.012f;
            offY = -0.020f;
            break;
        case 31:
            offX = -boxW - 0.012f;
            offY = -0.018f;
            break;
        default:
            break;
    }
}

void MapRenderer::setupSceneLighting(float trainX, float trainY) const {
    if (!lightingEnabled) return;

    glEnable(GL_LIGHTING);
    glEnable(GL_COLOR_MATERIAL);
    glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);

    GLfloat sunPos[] = { 0.45f, 0.65f, 1.20f, 0.0f };
    GLfloat sunAmbient[] = { 0.45f, 0.48f, 0.55f, 1.0f };
    GLfloat sunDiffuse[] = { 0.82f, 0.85f, 0.92f, 1.0f };
    GLfloat sunSpecular[] = { 0.65f, 0.65f, 0.75f, 1.0f };

    glEnable(GL_LIGHT0);
    glLightfv(GL_LIGHT0, GL_POSITION, sunPos);
    glLightfv(GL_LIGHT0, GL_AMBIENT, sunAmbient);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, sunDiffuse);
    glLightfv(GL_LIGHT0, GL_SPECULAR, sunSpecular);

    GLfloat trainLightPos[] = { trainX, trainY, 0.06f, 1.0f };
    GLfloat trainLightAmbient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    GLfloat trainLightDiffuse[] = { 1.0f, 0.94f, 0.72f, 1.0f };
    GLfloat trainLightSpecular[] = { 1.0f, 0.98f, 0.85f, 1.0f };

    glEnable(GL_LIGHT1);
    glLightfv(GL_LIGHT1, GL_POSITION, trainLightPos);
    glLightfv(GL_LIGHT1, GL_AMBIENT, trainLightAmbient);
    glLightfv(GL_LIGHT1, GL_DIFFUSE, trainLightDiffuse);
    glLightfv(GL_LIGHT1, GL_SPECULAR, trainLightSpecular);
    glLightf(GL_LIGHT1, GL_CONSTANT_ATTENUATION, 1.0f);
    glLightf(GL_LIGHT1, GL_LINEAR_ATTENUATION, 1.5f);
    glLightf(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, 3.8f);

    GLfloat matSpecular[] = { 0.85f, 0.85f, 0.90f, 1.0f };
    glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, matSpecular);
    glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 64.0f);
}

void MapRenderer::disableSceneLighting() const {
    if (!lightingEnabled) return;
    glDisable(GL_LIGHT0);
    glDisable(GL_LIGHT1);
    glDisable(GL_COLOR_MATERIAL);
    glDisable(GL_LIGHTING);
}

void MapRenderer::drawMapBoundary(const ClipWindow& win) const {
    const int numBoundaryPts = 26;
    float boundary[numBoundaryPts][2] = {
        { -0.60f,  0.88f },
        { -0.42f,  0.86f },
        { -0.22f,  0.78f },
        {  0.02f,  0.74f },
        {  0.28f,  0.76f },
        {  0.52f,  0.82f },
        {  0.76f,  0.88f },
        {  0.88f,  0.85f },
        {  0.84f,  0.68f },
        {  0.76f,  0.48f },
        {  0.68f,  0.30f },
        {  0.62f,  0.18f },
        {  0.42f,  0.12f },
        {  0.22f,  0.02f },
        {  0.04f, -0.16f },
        { -0.06f, -0.34f },
        { -0.22f, -0.44f },
        { -0.40f, -0.60f },
        { -0.52f, -0.70f },
        { -0.66f, -0.86f },
        { -0.74f, -0.68f },
        { -0.76f, -0.48f },
        { -0.84f, -0.28f },
        { -0.86f,  0.05f },
        { -0.88f,  0.36f },
        { -0.72f,  0.68f }
    };

    glColor4f(0.88f, 0.92f, 0.96f, 0.50f);
    glBegin(GL_POLYGON);
    glNormal3f(0.0f, 0.0f, 1.0f);
    for (int i = 0; i < numBoundaryPts; i++) {
        glVertex2f(boundary[i][0], boundary[i][1]);
    }
    glEnd();

    glLineWidth(2.8f);
    glColor3f(0.047f, 0.063f, 0.396f);
    glBegin(GL_LINE_LOOP);
    for (int i = 0; i < numBoundaryPts; i++) {
        glVertex2f(boundary[i][0], boundary[i][1]);
    }
    glEnd();

    glLineWidth(2.0f);
    glColor3f(0.12f, 0.45f, 0.75f);
    glBegin(GL_LINE_STRIP);
    for (int i = 19; i < numBoundaryPts; i++) {
        glVertex2f(boundary[i][0], boundary[i][1]);
    }
    glEnd();

    glColor3f(0.18f, 0.42f, 0.72f);
    drawString(-0.98f, -0.18f, "ARABIAN SEA");
    drawString(-0.98f, -0.23f, "~~~~~~~~~~~");
    drawString(-0.98f, -0.28f, "KONKAN COAST");
}

void MapRenderer::drawDistrictDemarcations(const ClipWindow& win) const {
    glEnable(GL_LINE_STIPPLE);
    glLineStipple(2, 0xAAAA);
    glLineWidth(1.2f);
    glColor4f(0.55f, 0.62f, 0.72f, 0.65f);

    glBegin(GL_LINE_STRIP);
    glVertex2f(0.32f, 0.76f);
    glVertex2f(0.34f, 0.45f);
    glVertex2f(0.40f, 0.12f);
    glEnd();

    glBegin(GL_LINE_STRIP);
    glVertex2f(-0.35f, 0.45f);
    glVertex2f(-0.05f, 0.40f);
    glVertex2f(0.20f, 0.20f);
    glEnd();

    glBegin(GL_LINE_STRIP);
    glVertex2f(-0.70f, 0.35f);
    glVertex2f(-0.64f, 0.00f);
    glVertex2f(-0.60f, -0.45f);
    glEnd();

    glDisable(GL_LINE_STIPPLE);
}

void MapRenderer::drawDoubleRailSegment(float x1, float y1, float x2, float y2) const {
    float dx = x2 - x1;
    float dy = y2 - y1;
    float len = sqrt(dx * dx + dy * dy);
    if (len < 0.0001f) return;

    float nx = -dy / len;
    float ny =  dx / len;
    float railHalfWidth = 0.0035f;
    float ballastHalfWidth = 0.0175f;
    float uLen = len * 10.0f;

    if (texturesEnabled && ballastTextureId != 0) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, ballastTextureId);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glColor3f(0.88f, 0.86f, 0.84f);
        glBegin(GL_QUADS);
        glNormal3f(-nx * 0.3f, -ny * 0.3f, 0.95f);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(x1 - nx * ballastHalfWidth, y1 - ny * ballastHalfWidth);
        glTexCoord2f(uLen, 0.0f); glVertex2f(x2 - nx * ballastHalfWidth, y2 - ny * ballastHalfWidth);
        glNormal3f(nx * 0.3f, ny * 0.3f, 0.95f);
        glTexCoord2f(uLen, 1.0f); glVertex2f(x2 + nx * ballastHalfWidth, y2 + ny * ballastHalfWidth);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(x1 + nx * ballastHalfWidth, y1 + ny * ballastHalfWidth);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    } else {
        glColor3f(0.78f, 0.76f, 0.74f);
        glBegin(GL_QUADS);
        glVertex2f(x1 - nx * ballastHalfWidth, y1 - ny * ballastHalfWidth);
        glVertex2f(x2 - nx * ballastHalfWidth, y2 - ny * ballastHalfWidth);
        glVertex2f(x2 + nx * ballastHalfWidth, y2 + ny * ballastHalfWidth);
        glVertex2f(x1 + nx * ballastHalfWidth, y1 + ny * ballastHalfWidth);
        glEnd();
    }

    float step = 0.014f;
    int numSleepers = (int)(len / step);
    glColor3f(0.38f, 0.28f, 0.20f);
    glLineWidth(1.6f);
    glBegin(GL_LINES);
    for (int i = 0; i <= numSleepers; i++) {
        float t = (float)i / (float)numSleepers;
        float px = x1 + dx * t;
        float py = y1 + dy * t;
        glVertex2f(px - nx * railHalfWidth * 2.2f, py - ny * railHalfWidth * 2.2f);
        glVertex2f(px + nx * railHalfWidth * 2.2f, py + ny * railHalfWidth * 2.2f);
    }
    glEnd();

    glColor3f(0.24f, 0.28f, 0.36f);
    glLineWidth(2.8f);
    glBegin(GL_LINES);
    glNormal3f(0.0f, 0.0f, 1.0f);
    glVertex2f(x1 - nx * railHalfWidth, y1 - ny * railHalfWidth);
    glVertex2f(x2 - nx * railHalfWidth, y2 - ny * railHalfWidth);
    glVertex2f(x1 + nx * railHalfWidth, y1 + ny * railHalfWidth);
    glVertex2f(x2 + nx * railHalfWidth, y2 + ny * railHalfWidth);
    glEnd();

    glColor3f(0.85f, 0.90f, 0.98f);
    glLineWidth(1.2f);
    glBegin(GL_LINES);
    glVertex2f(x1 - nx * railHalfWidth, y1 - ny * railHalfWidth);
    glVertex2f(x2 - nx * railHalfWidth, y2 - ny * railHalfWidth);
    glVertex2f(x1 + nx * railHalfWidth, y1 + ny * railHalfWidth);
    glVertex2f(x2 + nx * railHalfWidth, y2 + ny * railHalfWidth);
    glEnd();
}

void MapRenderer::drawTracks(const ClipWindow& win) const {
    for (int i = 0; i < NUM_TRACKS; i++) {
        float x1 = stations[tracks[i].fromIndex].x;
        float y1 = stations[tracks[i].fromIndex].y;
        float x2 = stations[tracks[i].toIndex].x;
        float y2 = stations[tracks[i].toIndex].y;

        if (cohenSutherlandClip(x1, y1, x2, y2, win)) {
            drawDoubleRailSegment(x1, y1, x2, y2);
        }
    }
}

void MapRenderer::drawActiveRoute(const ClipWindow& win) const {
    if (activeLegCount < 2) return;

    float baseH, baseS, baseV;
    rgbToHsv(0.949f, 0.561f, 0.231f, baseH, baseS, baseV);
    float animatedV = baseV + 0.18f * sin(pulsePhase * 2.0f);
    if (animatedV > 1.0f) animatedV = 1.0f;
    if (animatedV < 0.65f) animatedV = 0.65f;

    float r, g, b;
    hsvToRgb(baseH, baseS, animatedV, r, g, b);

    for (int i = 0; i < activeLegCount - 1; i++) {
        int u = activePath[i];
        int v = activePath[i + 1];

        float x1 = stations[u].x;
        float y1 = stations[u].y;
        float x2 = stations[v].x;
        float y2 = stations[v].y;

        if (cohenSutherlandClip(x1, y1, x2, y2, win)) {
            float dx = x2 - x1;
            float dy = y2 - y1;
            float len = sqrt(dx * dx + dy * dy);
            if (len > 0.0001f) {
                float nx = -dy / len;
                float ny =  dx / len;
                float ballastHalfWidth = 0.022f;
                float uLen = len * 10.0f;

                if (texturesEnabled && ballastTextureId != 0) {
                    glEnable(GL_TEXTURE_2D);
                    glBindTexture(GL_TEXTURE_2D, ballastTextureId);
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    glColor4f(r, g, b, 0.65f);
                    glBegin(GL_QUADS);
                    glTexCoord2f(0.0f, 0.0f); glVertex2f(x1 - nx * ballastHalfWidth, y1 - ny * ballastHalfWidth);
                    glTexCoord2f(uLen, 0.0f); glVertex2f(x2 - nx * ballastHalfWidth, y2 - ny * ballastHalfWidth);
                    glTexCoord2f(uLen, 1.0f); glVertex2f(x2 + nx * ballastHalfWidth, y2 + ny * ballastHalfWidth);
                    glTexCoord2f(0.0f, 1.0f); glVertex2f(x1 + nx * ballastHalfWidth, y1 + ny * ballastHalfWidth);
                    glEnd();
                    glDisable(GL_TEXTURE_2D);
                    glBindTexture(GL_TEXTURE_2D, 0);
                }
            }
        }
    }

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE);

    for (int i = 0; i < activeLegCount - 1; i++) {
        int u = activePath[i];
        int v = activePath[i + 1];

        float x1 = stations[u].x;
        float y1 = stations[u].y;
        float x2 = stations[v].x;
        float y2 = stations[v].y;

        if (cohenSutherlandClip(x1, y1, x2, y2, win)) {
            glColor4f(r, g, b, 0.25f);
            glLineWidth(8.0f);
            glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
            glEnd();

            glColor4f(r, g, b, 0.70f);
            glLineWidth(4.5f);
            glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
            glEnd();

            glColor3f(1.0f, 0.96f, 0.88f);
            glLineWidth(2.0f);
            glBegin(GL_LINES);
            glVertex2f(x1, y1);
            glVertex2f(x2, y2);
            glEnd();
        }
    }
    glDisable(GL_BLEND);
}

static void drawCircleFan(float cx, float cy, float radius, int segments) {
    glBegin(GL_TRIANGLE_FAN);
    glVertex2f(cx, cy);
    for (int a = 0; a <= segments; a++) {
        float angle = a * 2.0f * PI / (float)segments;
        glVertex2f(cx + cos(angle) * radius, cy + sin(angle) * radius);
    }
    glEnd();
}

void MapRenderer::drawStationGlow(float cx, float cy, float radius, float r, float g, float b) const {
    int rings = 6;
    for (int k = rings; k >= 1; k--) {
        float frac = (float)k / (float)rings;
        float rCurr = radius * frac;
        float alpha = 0.28f * (1.0f - frac * frac);
        glColor4f(r, g, b, alpha);
        drawCircleFan(cx, cy, rCurr, 24);
    }
}

void MapRenderer::drawStations(const ClipWindow& win) const {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    for (int i = 0; i < NUM_STATIONS; i++) {
        float cx = stations[i].x;
        float cy = stations[i].y;

        if (computeOutCode(cx, cy, win) != 0) {
            continue;
        }

        bool isActive = false;
        bool isOrigin = false;
        bool isDest = false;
        bool isCurrentStop = false;

        if (activeLegCount >= 2) {
            if (activePath[0] == i) isOrigin = true;
            if (activePath[activeLegCount - 1] == i) isDest = true;
            if (activePath[currentLegIndex] == i) isCurrentStop = true;
            for (int p = 0; p < activeLegCount; p++) {
                if (activePath[p] == i) {
                    isActive = true;
                    break;
                }
            }
        }

        if (isOrigin) {
            drawStationGlow(cx, cy, 0.038f, 0.08f, 0.75f, 0.40f);
        } else if (isDest) {
            drawStationGlow(cx, cy, 0.038f, 0.92f, 0.22f, 0.22f);
        } else if (isCurrentStop) {
            drawStationGlow(cx, cy, 0.034f, 0.98f, 0.80f, 0.15f);
        } else if (isActive) {
            drawStationGlow(cx, cy, 0.028f, 0.949f, 0.561f, 0.231f);
        }
    }
    glDisable(GL_BLEND);

    for (int i = 0; i < NUM_STATIONS; i++) {
        float cx = stations[i].x;
        float cy = stations[i].y;

        if (computeOutCode(cx, cy, win) != 0) {
            continue;
        }

        bool isActive = false;
        bool isOrigin = false;
        bool isDest = false;
        bool isCurrentStop = false;

        if (activeLegCount >= 2) {
            if (activePath[0] == i) isOrigin = true;
            if (activePath[activeLegCount - 1] == i) isDest = true;
            if (activePath[currentLegIndex] == i) isCurrentStop = true;
            for (int p = 0; p < activeLegCount; p++) {
                if (activePath[p] == i) {
                    isActive = true;
                    break;
                }
            }
        }

        glColor3f(0.047f, 0.063f, 0.396f);
        drawCircleFan(cx, cy, 0.0135f, 22);

        if (isOrigin) {
            glColor3f(0.08f, 0.75f, 0.40f);
        } else if (isDest) {
            glColor3f(0.92f, 0.22f, 0.22f);
        } else if (isCurrentStop) {
            glColor3f(0.98f, 0.80f, 0.15f);
        } else if (isActive) {
            glColor3f(0.949f, 0.561f, 0.231f);
        } else {
            glColor3f(1.0f, 1.0f, 1.0f);
        }
        drawCircleFan(cx, cy, 0.0085f, 22);
    }

    for (int i = 0; i < NUM_STATIONS; i++) {
        float cx = stations[i].x;
        float cy = stations[i].y;

        if (computeOutCode(cx, cy, win) != 0) {
            continue;
        }

        bool isActive = false;
        if (activeLegCount >= 2) {
            for (int p = 0; p < activeLegCount; p++) {
                if (activePath[p] == i) {
                    isActive = true;
                    break;
                }
            }
        }

        float offX = 0.0f;
        float offY = 0.0f;
        float boxW = 0.0f;
        float boxH = 0.0f;
        getStationLabelLayout(i, offX, offY, boxW, boxH);

        float pillX = (offX > 0.0f) ? (cx + offX - 0.004f) : (cx + offX - 0.004f);
        float pillY = cy + offY - 0.005f;

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (isActive) {
            drawPill(pillX, pillY, boxW, boxH, 1.0f, 1.0f, 1.0f, 0.92f);
            glColor4f(0.949f, 0.561f, 0.231f, 0.85f);
            glLineWidth(1.2f);
            glBegin(GL_LINE_LOOP);
            glVertex2f(pillX, pillY);
            glVertex2f(pillX + boxW, pillY);
            glVertex2f(pillX + boxW, pillY + boxH);
            glVertex2f(pillX, pillY + boxH);
            glEnd();
        } else {
            drawPill(pillX, pillY, boxW, boxH, 0.94f, 0.96f, 0.98f, 0.85f);
            glColor4f(0.70f, 0.78f, 0.86f, 0.65f);
            glLineWidth(1.0f);
            glBegin(GL_LINE_LOOP);
            glVertex2f(pillX, pillY);
            glVertex2f(pillX + boxW, pillY);
            glVertex2f(pillX + boxW, pillY + boxH);
            glVertex2f(pillX, pillY + boxH);
            glEnd();
        }
        glDisable(GL_BLEND);

        if (isActive) {
            drawStringWithShadow(cx + offX, cy + offY, stations[i].label, 0.047f, 0.063f, 0.396f);
        } else {
            glColor3f(0.12f, 0.16f, 0.30f);
            drawString(cx + offX, cy + offY, stations[i].label);
        }
    }
}

void MapRenderer::drawTrainCar(float x, float y, float angleDeg, float length, float width, bool isEngine) const {
    glPushMatrix();
    glTranslatef(x, y, 0.0f);
    glRotatef(angleDeg, 0.0f, 0.0f, 1.0f);

    float halfL = length * 0.5f;
    float halfW = width * 0.5f;

    if (texturesEnabled && metalTextureId != 0) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, metalTextureId);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glColor3f(0.95f, 0.95f, 0.98f);
        glBegin(GL_POLYGON);
        glNormal3f(0.0f, 0.0f, 1.0f);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(-halfL, -halfW);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(isEngine ? (halfL - 0.012f) : halfL, -halfW);
        if (isEngine) {
            glTexCoord2f(1.2f, 0.5f); glVertex2f(halfL + 0.008f, 0.0f);
        }
        glTexCoord2f(1.0f, 1.0f); glVertex2f(isEngine ? (halfL - 0.012f) : halfL, halfW);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(-halfL, halfW);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    } else {
        glColor3f(0.92f, 0.93f, 0.96f);
        glBegin(GL_POLYGON);
        glVertex2f(-halfL, -halfW);
        glVertex2f(isEngine ? (halfL - 0.012f) : halfL, -halfW);
        if (isEngine) {
            glVertex2f(halfL + 0.008f, 0.0f);
        }
        glVertex2f(isEngine ? (halfL - 0.012f) : halfL, halfW);
        glVertex2f(-halfL, halfW);
        glEnd();
    }

    glColor3f(0.047f, 0.063f, 0.396f);
    glBegin(GL_QUADS);
    glVertex2f(-halfL, -halfW * 0.35f);
    glVertex2f(isEngine ? (halfL - 0.010f) : halfL, -halfW * 0.35f);
    glVertex2f(isEngine ? (halfL - 0.010f) : halfL,  halfW * 0.35f);
    glVertex2f(-halfL,  halfW * 0.35f);
    glEnd();

    glColor3f(0.949f, 0.561f, 0.231f);
    glLineWidth(1.8f);
    glBegin(GL_LINES);
    glVertex2f(-halfL + 0.004f, halfW * 0.55f);
    glVertex2f(isEngine ? (halfL - 0.012f) : (halfL - 0.004f), halfW * 0.55f);
    glVertex2f(-halfL + 0.004f, -halfW * 0.55f);
    glVertex2f(isEngine ? (halfL - 0.012f) : (halfL - 0.004f), -halfW * 0.55f);
    glEnd();

    glColor3f(0.12f, 0.16f, 0.25f);
    int numWindows = isEngine ? 3 : 4;
    float wStep = (length - 0.015f) / (float)numWindows;
    for (int k = 0; k < numWindows; k++) {
        float wx = -halfL + 0.006f + k * wStep;
        glBegin(GL_QUADS);
        glVertex2f(wx, -halfW * 0.25f);
        glVertex2f(wx + wStep * 0.65f, -halfW * 0.25f);
        glVertex2f(wx + wStep * 0.65f,  halfW * 0.25f);
        glVertex2f(wx,  halfW * 0.25f);
        glEnd();
    }

    if (isEngine) {
        glColor3f(0.15f, 0.20f, 0.32f);
        glBegin(GL_QUADS);
        glVertex2f(halfL - 0.012f, -halfW * 0.6f);
        glVertex2f(halfL + 0.002f, -halfW * 0.2f);
        glVertex2f(halfL + 0.002f,  halfW * 0.2f);
        glVertex2f(halfL - 0.012f,  halfW * 0.6f);
        glEnd();

        glColor3f(1.0f, 0.95f, 0.40f);
        glPointSize(3.8f);
        glBegin(GL_POINTS);
        glVertex2f(halfL + 0.006f, -halfW * 0.25f);
        glVertex2f(halfL + 0.006f,  halfW * 0.25f);
        glEnd();
    } else {
        glColor3f(0.70f, 0.20f, 0.15f);
        glLineWidth(1.4f);
        glBegin(GL_LINE_STRIP);
        glVertex2f(-0.006f, 0.0f);
        glVertex2f( 0.000f, halfW * 1.5f);
        glVertex2f( 0.008f, halfW * 1.5f);
        glVertex2f( 0.002f, 0.0f);
        glEnd();
    }

    glPopMatrix();
}

void MapRenderer::drawTrain(const ClipWindow& win) const {
    if (activeLegCount < 2) return;

    int u = activePath[currentLegIndex];
    int v = activePath[currentLegIndex + 1];

    float x1 = stations[u].x;
    float y1 = stations[u].y;
    float x2 = stations[v].x;
    float y2 = stations[v].y;

    float dx = x2 - x1;
    float dy = y2 - y1;
    float segLen = sqrt(dx * dx + dy * dy);
    if (segLen < 0.0001f) return;

    float ux = dx / segLen;
    float uy = dy / segLen;

    float tx = x1 + dx * trainProgress;
    float ty = y1 + dy * trainProgress;

    if (computeOutCode(tx, ty, win) != 0) {
        return;
    }

    setupSceneLighting(tx, ty);

    float angleRad = atan2(dy, dx);
    float angleDeg = angleRad * 180.0f / PI;

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE);
    glColor4f(1.0f, 0.96f, 0.70f, 0.35f);
    glPushMatrix();
    glTranslatef(tx, ty, 0.0f);
    glRotatef(angleDeg, 0.0f, 0.0f, 1.0f);
    glBegin(GL_TRIANGLES);
    glVertex2f(0.035f, 0.0f);
    glVertex2f(0.160f, 0.035f);
    glVertex2f(0.160f, -0.035f);
    glEnd();
    glPopMatrix();
    glDisable(GL_BLEND);

    float carSpacing = 0.046f;
    float c2x = tx - ux * carSpacing * 2.0f;
    float c2y = ty - uy * carSpacing * 2.0f;
    drawTrainCar(c2x, c2y, angleDeg, 0.040f, 0.016f, false);

    float c1x = tx - ux * carSpacing;
    float c1y = ty - uy * carSpacing;
    drawTrainCar(c1x, c1y, angleDeg, 0.040f, 0.016f, false);

    drawTrainCar(tx, ty, angleDeg, 0.048f, 0.017f, true);

    disableSceneLighting();
}

void MapRenderer::drawHUD() const {
    glMatrixMode(GL_PROJECTION);
    glPushMatrix();
    glLoadIdentity();
    gluOrtho2D(0, windowWidth, 0, windowHeight);

    glMatrixMode(GL_MODELVIEW);
    glPushMatrix();
    glLoadIdentity();

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    int topBarH = 54;
    int topBarY = windowHeight - topBarH - 8;

    if (texturesEnabled && metalTextureId != 0) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, metalTextureId);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glColor4f(0.047f, 0.063f, 0.396f, 0.92f);
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2i(14, topBarY);
        glTexCoord2f(6.0f, 0.0f); glVertex2i(windowWidth - 14, topBarY);
        glTexCoord2f(6.0f, 1.0f); glVertex2i(windowWidth - 14, windowHeight - 8);
        glTexCoord2f(0.0f, 1.0f); glVertex2i(14, windowHeight - 8);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    } else {
        glColor4f(0.047f, 0.063f, 0.396f, 0.92f);
        glBegin(GL_QUADS);
        glVertex2i(14, topBarY);
        glVertex2i(windowWidth - 14, topBarY);
        glVertex2i(windowWidth - 14, windowHeight - 8);
        glVertex2i(14, windowHeight - 8);
        glEnd();
    }

    glColor3f(0.949f, 0.561f, 0.231f);
    glLineWidth(2.0f);
    glBegin(GL_LINE_LOOP);
    glVertex2i(14, topBarY);
    glVertex2i(windowWidth - 14, topBarY);
    glVertex2i(windowWidth - 14, windowHeight - 8);
    glVertex2i(14, windowHeight - 8);
    glEnd();

    char buf[256];
    glColor3f(1.0f, 1.0f, 1.0f);
    drawString(28, windowHeight - 26, "RAILROUTE: MAHARASHTRA 32-STATION RAILWAY NETWORK");

    glColor3f(0.949f, 0.561f, 0.231f);
    sprintf(buf, "Active Mode: %s", isShortestActive ? "SHORTEST ROUTE (OPTIMAL DIJKSTRA)" : "LONGEST ROUTE (SCENIC)");
    drawString(28, windowHeight - 46, buf);

    if (activeLegCount >= 2) {
        int u = activePath[currentLegIndex];
        int v = activePath[currentLegIndex + 1];
        int col2X = windowWidth / 2 - 120;
        if (col2X < 360) col2X = 360;

        glColor3f(0.85f, 0.92f, 1.0f);
        sprintf(buf, "Corridor: [%s] -> [%s] (%d km | %d stops)",
                stations[activePath[0]].code, stations[activePath[activeLegCount - 1]].code,
                activeDistance, activeLegCount - 1);
        drawString(col2X, windowHeight - 26, buf);

        glColor3f(1.0f, 0.95f, 0.70f);
        sprintf(buf, "Train: Vande Bharat 2D (Hop %d/%d: %s -> %s, Progress: %d%%)",
                currentLegIndex + 1, activeLegCount - 1,
                stations[u].code, stations[v].code, (int)(trainProgress * 100.0f));
        drawString(col2X, windowHeight - 46, buf);
    } else {
        int col2X = windowWidth / 2 - 120;
        if (col2X < 360) col2X = 360;
        glColor3f(0.85f, 0.90f, 1.0f);
        drawString(col2X, windowHeight - 26, "Corridor: [NONE] - Select Route in GUI to activate");
        drawString(col2X, windowHeight - 46, "Train: Standby (Awaiting route selection)");
    }

    int col3X = windowWidth - 280;
    if (col3X > windowWidth / 2 + 180) {
        glColor3f(0.80f, 0.88f, 1.0f);
        sprintf(buf, "Status: %s", isPaused ? "PAUSED" : "RUNNING (110 km/h)");
        drawString(col3X, windowHeight - 26, buf);

        sprintf(buf, "Zoom: %.2fx | Pan: (%.2f, %.2f)", zoomLevel, panX, panY);
        drawString(col3X, windowHeight - 46, buf);
    }

    glColor4f(0.047f, 0.063f, 0.396f, 0.88f);
    glBegin(GL_QUADS);
    glVertex2i(14, 12);
    glVertex2i(windowWidth - 14, 12);
    glVertex2i(windowWidth - 14, 38);
    glVertex2i(14, 38);
    glEnd();

    glColor3f(0.949f, 0.561f, 0.231f);
    glLineWidth(1.2f);
    glBegin(GL_LINE_LOOP);
    glVertex2i(14, 12);
    glVertex2i(windowWidth - 14, 12);
    glVertex2i(windowWidth - 14, 38);
    glVertex2i(14, 38);
    glEnd();

    glColor3f(0.95f, 0.95f, 0.95f);
    drawString(24, 21, "[W/A/S/D]: Pan | [+/-]: Zoom | [Space]: Pause | [1/2]: Shortest/Longest | [L]: Light | [T]: Texture | [R]: Reset | [ESC]: Exit");

    drawLegend();
    drawSelectedRoutePanel();

    glDisable(GL_BLEND);

    glPopMatrix();
    glMatrixMode(GL_PROJECTION);
    glPopMatrix();
    glMatrixMode(GL_MODELVIEW);
}

void MapRenderer::drawLegend() const {
    int legW = 230;
    int legH = 125;
    int legX = windowWidth - legW - 14;
    int legY = 48;

    glColor4f(0.047f, 0.063f, 0.396f, 0.88f);
    glBegin(GL_QUADS);
    glVertex2i(legX, legY);
    glVertex2i(legX + legW, legY);
    glVertex2i(legX + legW, legY + legH);
    glVertex2i(legX, legY + legH);
    glEnd();

    glColor3f(0.949f, 0.561f, 0.231f);
    glLineWidth(1.4f);
    glBegin(GL_LINE_LOOP);
    glVertex2i(legX, legY);
    glVertex2i(legX + legW, legY);
    glVertex2i(legX + legW, legY + legH);
    glVertex2i(legX, legY + legH);
    glEnd();

    glColor3f(0.949f, 0.561f, 0.231f);
    drawString(legX + 12, legY + legH - 18, "MAP LEGEND & GRAPHICS CO6");

    glLineWidth(4.0f);
    glColor3f(0.949f, 0.561f, 0.231f);
    glBegin(GL_LINES);
    glVertex2i(legX + 14, legY + legH - 36);
    glVertex2i(legX + 44, legY + legH - 36);
    glEnd();
    glColor3f(1.0f, 1.0f, 1.0f);
    drawString(legX + 54, legY + legH - 40, "Active Corridor (Glow/HSV)");

    glLineWidth(2.0f);
    glColor3f(0.40f, 0.48f, 0.58f);
    glBegin(GL_LINES);
    glVertex2i(legX + 14, legY + legH - 54);
    glVertex2i(legX + 44, legY + legH - 54);
    glEnd();
    glColor3f(0.85f, 0.90f, 0.95f);
    drawString(legX + 54, legY + legH - 58, "Network Track (Ballast/Clip)");

    glPointSize(7.0f);
    glBegin(GL_POINTS);
    glColor3f(0.08f, 0.75f, 0.40f); glVertex2i(legX + 22, legY + legH - 74);
    glColor3f(0.92f, 0.22f, 0.22f); glVertex2i(legX + 36, legY + legH - 74);
    glEnd();
    glColor3f(0.85f, 0.90f, 0.95f);
    drawString(legX + 54, legY + legH - 78, "Origin [GRN] / Dest [RED]");

    glBegin(GL_POINTS);
    glColor3f(1.0f, 1.0f, 1.0f); glVertex2i(legX + 22, legY + legH - 94);
    glEnd();
    glColor3f(0.85f, 0.90f, 0.95f);
    drawString(legX + 54, legY + legH - 98, "Station Junction Node");

    char statBuf[64];
    sprintf(statBuf, "Lighting: %s | Tex: %s", lightingEnabled ? "ON" : "OFF", texturesEnabled ? "ON" : "OFF");
    glColor3f(0.75f, 0.85f, 0.95f);
    drawString(legX + 12, legY + 10, statBuf);
}

void MapRenderer::drawSelectedRoutePanel() const {
    int panelW = 230;
    int panelX = windowWidth - panelW - 14;
    int panelBottom = 180;
    int maxTop = windowHeight - 72;
    int maxH = maxTop - panelBottom;
    if (maxH < 90) return;

    int numStations = activeLegCount;
    int headerH = 54;
    int rowH = 22;
    int panelH = 0;

    if (numStations < 2) {
        panelH = 115;
    } else {
        panelH = headerH + numStations * rowH + 12;
        if (panelH > maxH) {
            rowH = (maxH - headerH - 12) / numStations;
            if (rowH < 17) {
                rowH = 17;
                panelH = maxH;
            } else {
                panelH = headerH + numStations * rowH + 12;
            }
        }
    }
    int panelTop = panelBottom + panelH;

    if (texturesEnabled && metalTextureId != 0) {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, metalTextureId);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
        glColor4f(0.047f, 0.063f, 0.396f, 0.90f);
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2i(panelX, panelBottom);
        glTexCoord2f(4.0f, 0.0f); glVertex2i(panelX + panelW, panelBottom);
        glTexCoord2f(4.0f, 4.0f); glVertex2i(panelX + panelW, panelTop);
        glTexCoord2f(0.0f, 4.0f); glVertex2i(panelX, panelTop);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
    } else {
        glColor4f(0.047f, 0.063f, 0.396f, 0.92f);
        glBegin(GL_QUADS);
        glVertex2i(panelX, panelBottom);
        glVertex2i(panelX + panelW, panelBottom);
        glVertex2i(panelX + panelW, panelTop);
        glVertex2i(panelX, panelTop);
        glEnd();
    }

    glColor3f(0.949f, 0.561f, 0.231f);
    glLineWidth(1.4f);
    glBegin(GL_LINE_LOOP);
    glVertex2i(panelX, panelBottom);
    glVertex2i(panelX + panelW, panelBottom);
    glVertex2i(panelX + panelW, panelTop);
    glVertex2i(panelX, panelTop);
    glEnd();

    char buf[128];
    glColor3f(0.949f, 0.561f, 0.231f);
    drawString(panelX + 12, panelTop - 18, "SELECTED ROUTE ITINERARY");

    if (numStations >= 2) {
        glColor3f(0.85f, 0.92f, 1.0f);
        sprintf(buf, "Mode: %s", isShortestActive ? "Shortest (Dijkstra)" : "Longest (Scenic)");
        drawString(panelX + 12, panelTop - 32, buf);

        glColor3f(1.0f, 0.95f, 0.70f);
        sprintf(buf, "%d km | %d stops | %d hops", activeDistance, numStations, numStations - 1);
        drawString(panelX + 12, panelTop - 46, buf);
    } else {
        glColor3f(0.70f, 0.78f, 0.88f);
        drawString(panelX + 12, panelTop - 32, "Mode: Standby");
        drawString(panelX + 12, panelTop - 46, "Awaiting Route Selection");
    }

    glColor4f(0.35f, 0.45f, 0.60f, 0.65f);
    glLineWidth(1.0f);
    glBegin(GL_LINES);
    glVertex2i(panelX + 10, panelTop - 52);
    glVertex2i(panelX + panelW - 10, panelTop - 52);
    glEnd();

    if (numStations < 2) {
        glColor3f(0.75f, 0.82f, 0.92f);
        drawString(panelX + 16, panelTop - 74, "No active route corridor.");
        drawString(panelX + 16, panelTop - 92, "Select route in Java GUI");
        drawString(panelX + 16, panelTop - 106, "or press [1] / [2] to load.");
        return;
    }

    int listTop = panelTop - headerH - 4;
    int spineX = panelX + 20;

    int maxVisible = (listTop - (panelBottom + 8)) / rowH;
    int displayCount = (numStations <= maxVisible) ? numStations : maxVisible;

    int firstY = listTop - 10;
    int lastY = firstY - (displayCount - 1) * rowH;
    glColor4f(0.40f, 0.50f, 0.65f, 0.70f);
    glLineWidth(2.0f);
    glBegin(GL_LINES);
    glVertex2i(spineX, firstY);
    glVertex2i(spineX, lastY);
    glEnd();

    for (int k = 0; k < displayCount; k++) {
        int idxInPath = k;
        if (displayCount < numStations && k == displayCount - 1) {
            idxInPath = numStations - 1;
        }

        int stIdx = activePath[idxInPath];
        if (stIdx < 0 || stIdx >= NUM_STATIONS) continue;

        int itemY = listTop - 10 - k * rowH;

        bool isOrigin = (idxInPath == 0);
        bool isDest = (idxInPath == numStations - 1);
        bool isCurrent = (idxInPath == currentLegIndex);

        if (displayCount < numStations && k == displayCount - 2) {
            glColor3f(0.70f, 0.78f, 0.88f);
            sprintf(buf, "... +%d more stops ...", numStations - displayCount + 1);
            drawString(panelX + 30, itemY - 3, buf);
            glPointSize(4.0f);
            glBegin(GL_POINTS);
            glColor3f(0.70f, 0.78f, 0.88f); glVertex2i(spineX, itemY);
            glEnd();
            continue;
        }

        if (isOrigin) {
            glColor3f(0.08f, 0.75f, 0.40f);
            glPointSize(8.0f);
            glBegin(GL_POINTS);
            glVertex2i(spineX, itemY);
            glEnd();

            glColor3f(0.08f, 0.75f, 0.40f);
            sprintf(buf, "1. %-4s", stations[stIdx].code);
            drawString(panelX + 30, itemY - 3, buf);

            glColor3f(0.85f, 0.95f, 0.85f);
            drawString(panelX + 80, itemY - 3, stations[stIdx].label);
        } else if (isDest) {
            glColor3f(0.92f, 0.22f, 0.22f);
            glPointSize(8.0f);
            glBegin(GL_POINTS);
            glVertex2i(spineX, itemY);
            glEnd();

            glColor3f(0.92f, 0.22f, 0.22f);
            sprintf(buf, "%d. %-4s", idxInPath + 1, stations[stIdx].code);
            drawString(panelX + 30, itemY - 3, buf);

            glColor3f(1.0f, 0.85f, 0.85f);
            drawString(panelX + 80, itemY - 3, stations[stIdx].label);
        } else if (isCurrent) {
            glColor3f(0.98f, 0.80f, 0.15f);
            glPointSize(7.0f);
            glBegin(GL_POINTS);
            glVertex2i(spineX, itemY);
            glEnd();

            glColor3f(1.0f, 0.90f, 0.30f);
            sprintf(buf, "%d. %-4s", idxInPath + 1, stations[stIdx].code);
            drawString(panelX + 30, itemY - 3, buf);

            glColor3f(1.0f, 0.95f, 0.75f);
            drawString(panelX + 80, itemY - 3, stations[stIdx].label);
        } else {
            glColor3f(1.0f, 1.0f, 1.0f);
            glPointSize(5.0f);
            glBegin(GL_POINTS);
            glVertex2i(spineX, itemY);
            glEnd();

            glColor3f(0.82f, 0.88f, 0.96f);
            sprintf(buf, "%d. %-4s", idxInPath + 1, stations[stIdx].code);
            drawString(panelX + 30, itemY - 3, buf);

            glColor3f(0.70f, 0.78f, 0.88f);
            drawString(panelX + 80, itemY - 3, stations[stIdx].label);
        }
    }
}

void MapRenderer::render() const {
    glClearColor(0.933f, 0.953f, 0.973f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    ClipWindow win = calculateVisibleBounds(panX, panY, zoomLevel, aspectRatio);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    glScalef(zoomLevel, zoomLevel, 1.0f);
    glTranslatef(panX, panY, 0.0f);

    drawMapBoundary(win);
    drawDistrictDemarcations(win);
    drawTracks(win);
    drawActiveRoute(win);
    drawStations(win);
    drawTrain(win);

    drawHUD();

    glutSwapBuffers();
}
