#include "MapRenderer.h"
#include <GL/glut.h>
#include <iostream>

MapRenderer mapRenderer;

void display() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();

    mapRenderer.render();

    glFlush();
}

void keyboard(unsigned char key, int x, int y) {
    const float panStep = 0.05f;
    const float zoomInFactor = 1.1f;
    const float zoomOutFactor = 0.9f;

    switch (key) {
        case 'w':
        case 'W':
            mapRenderer.pan(0.0f, panStep);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case 's':
        case 'S':
            mapRenderer.pan(0.0f, -panStep);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case 'a':
        case 'A':
            mapRenderer.pan(-panStep, 0.0f);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case 'd':
        case 'D':
            mapRenderer.pan(panStep, 0.0f);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case '+':
        case '=':
            mapRenderer.zoom(zoomInFactor);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case '-':
        case '_':
            mapRenderer.zoom(zoomOutFactor);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case 'r':
        case 'R':
            mapRenderer.resetView();
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case 27:
            exit(0);
            break;
        default:
            break;
    }
}

void specialKeys(int key, int x, int y) {
    const float panStep = 0.05f;

    switch (key) {
        case GLUT_KEY_UP:
            mapRenderer.pan(0.0f, panStep);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case GLUT_KEY_DOWN:
            mapRenderer.pan(0.0f, -panStep);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case GLUT_KEY_LEFT:
            mapRenderer.pan(-panStep, 0.0f);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        case GLUT_KEY_RIGHT:
            mapRenderer.pan(panStep, 0.0f);
            mapRenderer.printClippingStatus();
            glutPostRedisplay();
            break;
        default:
            break;
    }
}

void initOpenGL() {
    glClearColor(0.945f, 0.961f, 0.973f, 1.0f);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1.0, 1.0, -1.0, 1.0);
    glMatrixMode(GL_MODELVIEW);
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
    glutKeyboardFunc(keyboard);
    glutSpecialFunc(specialKeys);

    std::cout << "===================================================================\n";
    std::cout << "  RailRoute Computer Graphics (Review 2 / Task 2)\n";
    std::cout << "  CO3: Geometric Transformations (2D Pan & Zoom)\n";
    std::cout << "  CO5: Viewport Line Clipping (Cohen-Sutherland Algorithm)\n";
    std::cout << "===================================================================\n";
    std::cout << "Controls:\n";
    std::cout << "  [Arrow Keys] or [W/A/S/D] : Pan map (Translate)\n";
    std::cout << "  [+] / [-]                : Zoom in / Zoom out (Scale: 0.5x - 3.0x)\n";
    std::cout << "  [R]                      : Reset pan & zoom to default\n";
    std::cout << "  [ESC]                    : Exit\n";

    mapRenderer.printClippingStatus();

    glutMainLoop();

    return 0;
}