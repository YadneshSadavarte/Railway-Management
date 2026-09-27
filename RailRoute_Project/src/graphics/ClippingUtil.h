#ifndef CLIPPING_UTIL_H
#define CLIPPING_UTIL_H

const int INSIDE = 0;
const int LEFT   = 1;
const int RIGHT  = 2;
const int BOTTOM = 4;
const int TOP    = 8;

struct ClipWindow {
    float xmin;
    float ymin;
    float xmax;
    float ymax;
};

int computeOutCode(float x, float y, const ClipWindow& win);

bool cohenSutherlandClip(float& x1, float& y1, float& x2, float& y2, const ClipWindow& win);

ClipWindow calculateVisibleBounds(float panX, float panY, float zoomLevel);

#endif
