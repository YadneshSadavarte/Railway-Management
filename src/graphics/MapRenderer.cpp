#include <GL/glut.h>
#include <iostream>
#include <string>

/**
 * RailRoute Network Mapping & Visualizer - Phase 1: Basic Network Rendering
 * 
 * This program sets up a 2D OpenGL viewport using GLUT and renders a static map 
 * of the railway network. It uses GL_POINTS to represent stations, GL_LINES to
 * represent the tracks connecting them, and GLUT bitmap strings to draw text labels
 * on the screen.
 * 
 * Designed for 2nd-year B.Tech Computer Graphics Lab Review 1.
 */

// Define window size variables
const int WINDOW_WIDTH = 800;
const int WINDOW_HEIGHT = 600;

// Struct to represent a station node on our 2D screen coordinates
struct StationMarker {
    std::string name;
    std::string code;
    int x;
    int y;
};

// Define 5 static stations mapped to coordinate positions (schematic geography)
const int NUM_STATIONS = 5;
StationMarker stations[NUM_STATIONS] = {
    {"Pune Junction", "PNE", 200, 150},
    {"Lonavala", "LNL", 300, 250},
    {"Mumbai CSMT", "CSTM", 150, 400},
    {"Nashik Road", "NK", 450, 480},
    {"Solapur Junction", "SUR", 600, 100}
};

/**
 * Helper function to draw text on the OpenGL window using GLUT bitmap fonts.
 * 
 * @param x The X-coordinate for starting the text drawing
 * @param y The Y-coordinate for starting the text drawing
 * @param text The text string to render
 */
void renderText(float x, float y, const std::string& text) {
    glRasterPos2f(x, y); // Set raster position for bitmap character rendering
    for (char c : text) {
        glutBitmapCharacter(GLUT_BITMAP_HELVETICA_12, c); // Draw each character
    }
}

/**
 * OpenGL initialization routine to configure clearing color and viewport projection.
 */
void init() {
    // Set background color to light gray (RGB: 0.9, 0.9, 0.9)
    glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
    
    // Set up 2D orthographic projection matching our window dimensions (800x600 pixels)
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(0.0, WINDOW_WIDTH, 0.0, WINDOW_HEIGHT);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
}

/**
 * Core Display Callback function triggered whenever the OpenGL window needs rendering.
 */
void display() {
    // Clear the screen buffer
    glClear(GL_COLOR_BUFFER_BIT);
    
    // -------------------------------------------------------------------------
    // 1. DRAW RAILWAY TRACKS (GL_LINES Primitive)
    // -------------------------------------------------------------------------
    glLineWidth(3.5f);           // Set line thickness for tracks
    glColor3f(0.3f, 0.3f, 0.3f); // Dark gray tracks
    glBegin(GL_LINES);
        // Track Segment: Pune to Lonavala
        glVertex2i(stations[0].x, stations[0].y);
        glVertex2i(stations[1].x, stations[1].y);
        
        // Track Segment: Lonavala to Mumbai
        glVertex2i(stations[1].x, stations[1].y);
        glVertex2i(stations[2].x, stations[2].y);
        
        // Track Segment: Mumbai to Nashik
        glVertex2i(stations[2].x, stations[2].y);
        glVertex2i(stations[3].x, stations[3].y);
        
        // Track Segment: Pune to Solapur
        glVertex2i(stations[0].x, stations[0].y);
        glVertex2i(stations[4].x, stations[4].y);
    glEnd();
    
    // -------------------------------------------------------------------------
    // 2. DRAW STATION NODES (GL_POINTS Primitive)
    // -------------------------------------------------------------------------
    glPointSize(12.0f);          // Set size of station points for visibility
    glBegin(GL_POINTS);
        // Pune Junction - Blue Marker (Junction)
        glColor3f(0.0f, 0.0f, 0.8f);
        glVertex2i(stations[0].x, stations[0].y);
        
        // Lonavala - Orange Marker (Transit Station)
        glColor3f(0.8f, 0.4f, 0.0f);
        glVertex2i(stations[1].x, stations[1].y);
        
        // Mumbai CSMT - Red Marker (Terminal Station)
        glColor3f(0.8f, 0.0f, 0.0f);
        glVertex2i(stations[2].x, stations[2].y);
        
        // Nashik Road - Red Marker (Terminal Station)
        glColor3f(0.8f, 0.0f, 0.0f);
        glVertex2i(stations[3].x, stations[3].y);
        
        // Solapur Junction - Blue Marker (Junction)
        glColor3f(0.0f, 0.0f, 0.8f);
        glVertex2i(stations[4].x, stations[4].y);
    glEnd();
    
    // -------------------------------------------------------------------------
    // 3. DRAW TEXT LABELS & LEGEND (GLUT Bitmap Text)
    // -------------------------------------------------------------------------
    glColor3f(0.0f, 0.0f, 0.0f); // Reset color to black for labels
    for (int i = 0; i < NUM_STATIONS; ++i) {
        // Offset text labels slightly to the right and top of the station nodes
        renderText(stations[i].x + 15, stations[i].y + 5, stations[i].name + " (" + stations[i].code + ")");
    }
    
    // Screen Title
    glColor3f(0.1f, 0.5f, 0.1f); // Dark green header
    renderText(220, WINDOW_HEIGHT - 40, "RAILROUTE MAP VISUALIZER - REVIEW 1 STATIC NETWORK");
    
    // Legend indicators
    glColor3f(0.4f, 0.4f, 0.4f);  // Muted gray
    renderText(20, 20, "Legend: Red = Terminals  |  Blue = Junctions  |  Orange = Transit Stations");
    
    // Flush the rendering commands
    glFlush();
}

/**
 * Main execution entry point.
 */
int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    
    // Configure window dimensions & screen placement
    glutInitWindowSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("RailRoute Visual Network Map");
    
    // Initialize coordinate matrices and clear colors
    init();
    
    // Link the display callback function
    glutDisplayFunc(display);
    
    std::cout << "==========================================================\n";
    std::cout << "   RAILROUTE GRAPHICS VIEWPORT INITIALIZED SUCCESSFULLY   \n";
    std::cout << "==========================================================\n";
    std::cout << "Rendering 5 stations and connecting track edges in 2D...\n";
    
    // Enter the infinite GLUT event processing loop
    glutMainLoop();
    return 0;
}
