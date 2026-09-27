#include "ClippingUtil.h"

int computeOutCode(float x, float y, const ClipWindow& win) {
    int code = INSIDE;

    if (x < win.xmin) {
        code |= LEFT;
    } else if (x > win.xmax) {
        code |= RIGHT;
    }

    if (y < win.ymin) {
        code |= BOTTOM;
    } else if (y > win.ymax) {
        code |= TOP;
    }

    return code;
}

bool cohenSutherlandClip(float& x1, float& y1, float& x2, float& y2, const ClipWindow& win) {
    int code1 = computeOutCode(x1, y1, win);
    int code2 = computeOutCode(x2, y2, win);
    bool accept = false;

    while (true) {
        if ((code1 | code2) == 0) {
            accept = true;
            break;
        } else if ((code1 & code2) != 0) {
            accept = false;
            break;
        } else {
            float x = 0.0f;
            float y = 0.0f;

            int codeOut = (code1 != 0) ? code1 : code2;

            if (codeOut & TOP) {
                x = x1 + (x2 - x1) * (win.ymax - y1) / (y2 - y1);
                y = win.ymax;
            } else if (codeOut & BOTTOM) {
                x = x1 + (x2 - x1) * (win.ymin - y1) / (y2 - y1);
                y = win.ymin;
            } else if (codeOut & RIGHT) {
                y = y1 + (y2 - y1) * (win.xmax - x1) / (x2 - x1);
                x = win.xmax;
            } else if (codeOut & LEFT) {
                y = y1 + (y2 - y1) * (win.xmin - x1) / (x2 - x1);
                x = win.xmin;
            }

            if (codeOut == code1) {
                x1 = x;
                y1 = y;
                code1 = computeOutCode(x1, y1, win);
            } else {
                x2 = x;
                y2 = y;
                code2 = computeOutCode(x2, y2, win);
            }
        }
    }

    return accept;
}

ClipWindow calculateVisibleBounds(float panX, float panY, float zoomLevel) {
    ClipWindow win;
    win.xmin = (-1.0f - panX) / zoomLevel;
    win.xmax = ( 1.0f - panX) / zoomLevel;
    win.ymin = (-1.0f - panY) / zoomLevel;
    win.ymax = ( 1.0f - panY) / zoomLevel;
    return win;
}
