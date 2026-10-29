# RailRoute — Computer Graphics (Task 3) Course Outcome Mapping & Evaluation Guide

**Academic Session:** 2026–2027  
**Course:** Computer Graphics & Laboratory (CGL)  
**Task Focus:** Task 3 — 2D Geometric Modeling, Texture Mapping, Lighting, Cohen-Sutherland Clipping & Pipeline  

---

## 1. Executive Summary & Source Mapping

The RailRoute OpenGL module (`src/graphics/`) delivers a high-performance, interactive 2D/2.5D railway network visualizer for the state of Maharashtra, modeling 32 verified station nodes, 46 track corridors, and operational Vande Bharat train telemetry.

| Rubric Concept | Primary Implementation File | Key Functions / Symbols | Evaluation Demonstration Key |
|---|---|---|:---:|
| **Texture Mapping (CO6)** | `MapRenderer.cpp` | `initTextures()`, `setupMipmappedTexture()`, `ballastTextureId`, `metalTextureId`, `drawDoubleRailSegment()` | Press `[T]` to toggle textures on/off in real time (visible ballast shoulders & metallic sheen) |
| **Physical Fixed-Function Lighting (CO6)** | `MapRenderer.cpp` | `setupSceneLighting()`, `disableSceneLighting()`, `GL_LIGHT0`, `GL_LIGHT1`, `glMaterialfv` | Press `[L]` to toggle lighting on/off in real time |
| **Refined Station Illumination (CO6)** | `MapRenderer.cpp` | `drawStationGlow()`, 3-pass `drawStations()`, `GL_ONE_MINUS_SRC_ALPHA` | Smooth non-additive radial glow without washing out node cores or text |
| **Label Collision Avoidance & Layout (CO1, UI)** | `MapRenderer.cpp` | `getStationLabelLayout()`, `drawPill()`, multi-quadrant offsets | Distinct label placement for Mumbai, Thane, Nashik, Aurangabad clusters |
| **Selected Route Itinerary Panel (UI/UX)** | `MapRenderer.cpp` | `drawSelectedRoutePanel()` | Right-docked itinerary timeline displaying journey sequence, origin/dest tags, distance & hops |
| **RGB <-> HSV Color Space (CO6)** | `MapRenderer.cpp` | `rgbToHsv()`, `hsvToRgb()`, `pulsePhase` | Active route pulses with animated value/saturation wave |
| **Cohen-Sutherland Line Clipping (CO5)** | `ClippingUtil.cpp`, `MapRenderer.cpp` | `computeOutCode()`, `cohenSutherlandClip()`, `printClippingStatus()` | Pan/zoom with `[W/A/S/D]` & `[+/-]` to view CLI clipping stream |
| **2D Transformations & Safe Padding (CO2, CO4)**| `glutMain.cpp`, `ClippingUtil.cpp`, `MapRenderer.cpp` | `pan()`, `zoom()`, `resetView()`, `reshapeCallback()`, `gluOrtho2D` | ~60px safe padding below top HUD; aspect-ratio preservation; `[R]` resets view |
| **Geometric Primitives & Models (CO1, CO3)**| `MapRenderer.cpp` | `drawMapBoundary()`, `drawDoubleRailSegment()`, `drawTrainCar()` | Textured ballast, wooden sleepers, dual rails, 3-car Vande Bharat |
| **Train Kinematics & Animation (CO3)** | `MapRenderer.cpp`, `glutMain.cpp` | `updateAnimation()`, `deltaSeconds`, `trainSpeed = 0.008f` | Press `[Space]` to pause/resume; train runs at original configured speed (110 km/h) |

---

## 2. In-Depth Technical Implementation of Task 3 Requirements

### A. Texture Mapping (`CO6`)
* **128x128 Procedural Ballast Gravel Texture:**
  - Generated algorithmically using dual-frequency pseudo-random noise combined with an 8x8 angular gravel stone grid.
  - Generates realistic stone fleck highlights (`#D6D3D1`), dark granite crevices (`#44403C`), and edge bevel shadows.
  - Uploaded via `gluBuild2DMipmaps` with `GL_LINEAR_MIPMAP_LINEAR` minification filtering to eliminate aliasing artifacts when zoomed out.
  - Mapped onto track shoulders using parameterized texture coordinates `(uLen, 1.0f)` based on physical segment distance.
* **128x128 Brushed Stainless Steel Metallic Texture:**
  - Generated algorithmically with fine horizontal anisotropic brush lines and a vertical cylindrical specular gradient (`sin(yNorm * PI)`).
  - Applied to the Vande Bharat locomotive nose cone, coach flanks, and telemetry HUD panel.
* **Demonstration for Faculty:**
  - Press `[T]` during execution. The visualizer instantly toggles between textured mode and untextured flat-shading mode, proving active texture binding and shader modulation (`GL_MODULATE`).

### B. Lighting and Illumination (`CO6`)
* **Dual Lighting Architecture:**
  1. **OpenGL Fixed-Function Hardware Lighting (`GL_LIGHTING`):**
     - `GL_LIGHT0`: Directional Daylight Sun source (`(0.45, 0.65, 1.20, 0.0)`), supplying diffuse (`0.82, 0.85, 0.92`) and specular reflection (`0.65, 0.65, 0.75`).
     - `GL_LIGHT1`: Positional Point Light mounted at the Vande Bharat locomotive headlight (`(tx, ty, 0.06, 1.0)`). Features realistic physical attenuation:
       $$\text{Attenuation} = \frac{1}{1.0 + 1.5d + 3.8d^2}$$
     - Materials: Configured with `glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, ...)` and `GL_SHININESS = 64.0f` to cast specular highlights on steel rails and train roof panels.
     - Primitives provide 3D surface normals (`glNormal3f`) for bevel and curvature calculations.
  2. **Refined Multi-Pass Station Illumination & Radial Glow:**
     - Point-source radial illumination calculated at active stations with smooth quadratic falloff:
       $$\alpha(r) = 0.28 \cdot \left(1.0 - \left(\frac{r}{R}\right)^2\right)$$
     - Rendered via concentric fans with standard non-additive blending (`glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)`).
     - **3-Pass Depth Separation:**
       - **Pass 1:** Radial illumination glow halos.
       - **Pass 2:** Sharp station junction discs (outer navy ring $r=0.0135$, inner core $r=0.0085$).
       - **Pass 3:** Translucent label backing pills (`drawPill`) and high-contrast typography.
     - Prevents center-pixel blowout and keeps station nodes and labels 100% sharp and readable.
     - Distinct color signatures: Emerald Green (`#14C366`) for Origin, Ruby Red (`#EB3838`) for Destination, Golden Amber (`#FACC26`) for Current Stop, and Vibrant Orange (`#F28F3B`) for Active Corridor.
  3. **RGB <-> HSV Color Conversion:**
     - Mathematical conversion of the `#F28F3B` accent color into Hue ($26^\circ$), Saturation ($0.75$), and Value ($0.95$).
     - Modulates Value with a continuous sinusoidal wave ($V(t) = V_0 + 0.18 \sin(2 \omega t)$) and converts back to RGB per frame for glowing active corridors.
* **Demonstration for Faculty:**
  - Press `[L]` during execution. The visualizer toggles fixed-function lighting and specular highlights on/off in real time.

### C. Cohen-Sutherland 2D Line Clipping (`CO5`)
* **Bitwise Outcode Evaluation:**
  - Outcodes `INSIDE = 0`, `LEFT = 1`, `RIGHT = 2`, `BOTTOM = 4`, `TOP = 8`.
  - Visible bounds dynamically computed based on `panX`, `panY`, `zoomLevel`, and `aspectRatio`.
* **Mathematical Boundary Trimming:**
  - Iteratively clips segments crossing `xmin`, `xmax`, `ymin`, or `ymax` using parametric intersection:
    $$x = x_1 + (x_2 - x_1) \frac{y_{\text{bound}} - y_1}{y_2 - y_1}, \quad y = y_1 + (y_2 - y_1) \frac{x_{\text{bound}} - x_1}{x_2 - x_1}$$
* **Demonstration for Faculty:**
  - Pan the map using `[W/A/S/D]` or zoom with `[+]`/`[-]`. The terminal stream immediately outputs the live track classification (`ACCEPTED`, `CLIPPED`, or `REJECTED`) along with outcodes and intersection counts.

### D. Window-to-Viewport, Safe Padding & Aspect-Ratio Preservation (`CO2, CO4`)
* Dynamic aspect-ratio compensation with top HUD safe padding in `reshapeCallback`:
  ```cpp
  float yMin = -1.05f;
  float yMax =  1.25f;
  float xSpan = (yMax - yMin) * aspect;
  float xMin = -xSpan * 0.5f;
  float xMax =  xSpan * 0.5f;
  gluOrtho2D(xMin, xMax, yMin, yMax);
  ```
* Allocates ~60px of safe visual clearance between the top HUD bar and northern border stations (Nandurbar, Gondia), preventing any overlap during window resizing or maximization.

---

## 3. Keyboard & Interaction Controls Reference

| Key | Action | Course Outcome |
|---|---|:---:|
| `[W]` / `[Up]` | Pan Map Up | CO2 / CO4 |
| `[S]` / `[Down]` | Pan Map Down | CO2 / CO4 |
| `[A]` / `[Left]` | Pan Map Left | CO2 / CO4 |
| `[D]` / `[Right]` | Pan Map Right | CO2 / CO4 |
| `[+]` / `[=]` | Zoom In (up to 4.5x) | CO2 / CO4 |
| `[-]` / `[_]` | Zoom Out (down to 0.5x) | CO2 / CO4 |
| `[Space]` | Pause / Resume Train Animation | Animation |
| `[1]` | Select Shortest Operational Route (Dijkstra) | Graph Routing |
| `[2]` | Select Alternative / Longest Route (Scenic) | Graph Routing |
| `[L]` | Toggle OpenGL Lighting On/Off | **CO6 (Lighting)** |
| `[T]` | Toggle Procedural Textures On/Off | **CO6 (Texture)** |
| `[R]` | Reset Viewport, Zoom & Train Telemetry | CO2 / CO4 |
| `[ESC]` | Exit Visualizer | System |

---

## 4. Build and Execution Instructions

### A. Build All Modules
In PowerShell from `RailRoute_Final`:
```powershell
powershell -ExecutionPolicy Bypass -File .\build.ps1
```

### B. Launch Standalone OpenGL Visualizer
```powershell
.\build\output\railroute_visualizer.exe data/active_route.txt
```

### C. Launch Complete Integrated Application (Java Swing + OpenGL)
```powershell
.\run.bat
```
*(Or in PowerShell: `java --enable-native-access=ALL-UNNAMED -cp "build/output/java;lib/sqlite-jdbc.jar" Main`)*
- In the GUI, navigate to **Tab 3 ("2D Live Map Visualizer")**.
- Click **"1. Sync Route Coordinates to OpenGL"**, then **"2. Launch Full 2D OpenGL Maharashtra Railway Visualizer"**.
