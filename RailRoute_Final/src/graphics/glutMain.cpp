#include <GL/freeglut.h>
#include <cstdlib>
#include "MapRenderer.h"

void displayCallback() {
    MapRenderer::getInstance().render();
}

void reshapeCallback(int w, int h) {
    int winW = (w > 0) ? w : 1;
    int winH = (h > 0) ? h : 1;
    glViewport(0, 0, winW, winH);
    MapRenderer::getInstance().setWindowDimensions(winW, winH);

    float aspect = (float)winW / (float)winH;
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    float yMin = -1.05f;
    float yMax = 1.25f;
    float ySpan = yMax - yMin;
    float xSpan = ySpan * aspect;
    float xMin = -xSpan * 0.5f;
    float xMax =  xSpan * 0.5f;
    gluOrtho2D(xMin, xMax, yMin, yMax);
    glMatrixMode(GL_MODELVIEW);
}

void timerCallback(int val) {
    static int lastTime = 0;
    int currentTime = glutGet(GLUT_ELAPSED_TIME);
    if (lastTime == 0) lastTime = currentTime;
    float deltaSeconds = (float)(currentTime - lastTime) / 1000.0f;
    if (deltaSeconds > 0.05f) deltaSeconds = 0.05f;
    lastTime = currentTime;

    MapRenderer::getInstance().updateAnimation(deltaSeconds);
    glutPostRedisplay();
    glutTimerFunc(16, timerCallback, 0);
}

void keyboardCallback(unsigned char key, int x, int y) {
    MapRenderer& renderer = MapRenderer::getInstance();
    switch (key) {
        case 'w':
        case 'W':
            renderer.pan(0.0f, -0.05f);
            renderer.printClippingStatus();
            break;
        case 's':
        case 'S':
            renderer.pan(0.0f, 0.05f);
            renderer.printClippingStatus();
            break;
        case 'a':
        case 'A':
            renderer.pan(0.05f, 0.0f);
            renderer.printClippingStatus();
            break;
        case 'd':
        case 'D':
            renderer.pan(-0.05f, 0.0f);
            renderer.printClippingStatus();
            break;
        case '+':
        case '=':
            renderer.zoom(1.10f);
            renderer.printClippingStatus();
            break;
        case '-':
        case '_':
            renderer.zoom(0.90f);
            renderer.printClippingStatus();
            break;
        case 32:
            renderer.togglePause();
            break;
        case '1':
            renderer.switchRouteMode(true);
            break;
        case '2':
            renderer.switchRouteMode(false);
            break;
        case 'l':
        case 'L':
            renderer.toggleLighting();
            break;
        case 't':
        case 'T':
            renderer.toggleTextures();
            break;
        case 'r':
        case 'R':
            renderer.resetView();
            break;
        case 27:
            exit(0);
            break;
        default:
            break;
    }
    glutPostRedisplay();
}

void specialCallback(int key, int x, int y) {
    MapRenderer& renderer = MapRenderer::getInstance();
    switch (key) {
        case GLUT_KEY_UP:
            renderer.pan(0.0f, -0.05f);
            renderer.printClippingStatus();
            break;
        case GLUT_KEY_DOWN:
            renderer.pan(0.0f, 0.05f);
            renderer.printClippingStatus();
            break;
        case GLUT_KEY_LEFT:
            renderer.pan(0.05f, 0.0f);
            renderer.printClippingStatus();
            break;
        case GLUT_KEY_RIGHT:
            renderer.pan(-0.05f, 0.0f);
            renderer.printClippingStatus();
            break;
        default:
            break;
    }
    glutPostRedisplay();
}

int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
    glutInitWindowSize(1024, 768);
    glutInitWindowPosition(120, 60);
    glutCreateWindow("RailRoute - 2D Intelligent Route & Train Visualizer (OpenGL FreeGLUT)");

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glShadeModel(GL_SMOOTH);

    const char* customFile = (argc >= 2) ? argv[1] : 0;
    MapRenderer::getInstance().init(customFile);

    glutDisplayFunc(displayCallback);
    glutReshapeFunc(reshapeCallback);
    glutKeyboardFunc(keyboardCallback);
    glutSpecialFunc(specialCallback);
    glutTimerFunc(16, timerCallback, 0);

    glutMainLoop();
    return 0;
}
