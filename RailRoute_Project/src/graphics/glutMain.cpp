#include "MapRenderer.h"
#include <GL/glut.h>

// Single global instance - GLUT's callback functions (display, etc.)
// must be plain C-style function pointers, so they can't be class methods.
// Keeping one global renderer is the simplest way to bridge that gap for Review 1.
MapRenderer mapRenderer;

// Called by GLUT whenever the window needs to be redrawn.
void display() {
    glClear(GL_COLOR_BUFFER_BIT);   // clear the screen
    glLoadIdentity();

    mapRenderer.render();           // draw tracks + stations

    glFlush();
}

// Sets up the 2D coordinate system so glVertex2f(-1..1, -1..1) maps to the window.
void initOpenGL() {
    glClearColor(0.945f, 0.961f, 0.973f, 1.0f); // Subtle Gray 1 (#F1F5F8) background, per Design.md
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1.0, 1.0, -1.0, 1.0); // simple 2D orthographic view, world space -1 to 1
}

int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(700, 700);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("RailRoute - Station Map (Review 1)");

    initOpenGL();
    mapRenderer.loadHardcodedMap();

    glutDisplayFunc(display);
    glutMainLoop();

    return 0;
}