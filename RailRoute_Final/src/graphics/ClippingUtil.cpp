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
            break;
        } else {
            int codeOut = (code1 != 0) ? code1 : code2;
            float x = 0.0f;
            float y = 0.0f;

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

ClipWindow calculateVisibleBounds(float panX, float panY, float zoomLevel, float aspect) {
    float yMinBase = -1.05f;
    float yMaxBase = 1.25f;
    float ySpan = yMaxBase - yMinBase;
    float effAspect = (aspect > 0.0f) ? aspect : 1.0f;
    float xSpan = ySpan * effAspect;
    float xMinBase = -xSpan * 0.5f;
    float xMaxBase =  xSpan * 0.5f;

    float xmin = (xMinBase - panX) / zoomLevel;
    float xmax = (xMaxBase - panX) / zoomLevel;
    float ymin = (yMinBase - panY) / zoomLevel;
    float ymax = (yMaxBase - panY) / zoomLevel;
    ClipWindow win = { xmin, ymin, xmax, ymax };
    return win;
}
