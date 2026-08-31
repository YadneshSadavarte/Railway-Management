# Architecture.md: RailRoute System

## 1. App Flow and Architecture

The RailRoute system utilizes a **Modular, Layered Architecture** to integrate four distinct academic subjects into a single application. 

### System Flow:
1. **User Interface Layer (Java):** The application starts here. Passengers interact with Java Swing forms to search for trains and input journey details. Operators log in to view the analytics dashboard.
2. **Data & Logic Layer (C++):** When a user requests a route or books a ticket, Java passes the parameters to the C++ backend. C++ uses linked lists and arrays to check seat availability and applies sorting/searching algorithms to find the shortest or fastest route.
3. **Hardware Analytics Layer (80386 Assembly):** If the Operator requests route efficiency data, the system triggers the low-level Assembly module to perform 64-bit arithmetic, calculating the physical distance and energy score.
4. **Visualization Layer (OpenGL):** Once the C++ logic determines the optimal route, the coordinates are sent to the Computer Graphics engine, which draws the track map and animates the train moving from the source to the destination.

---

## 2. Tech Stack

* **Frontend / Core Application:** Java (Core OOP, Java Swing for GUI, Java Collections for temporary states)
* **Backend Data Management:** C++ (Standard Template Library, Pointers, Structs/Classes for Data Structures)
* **Graphics & Rendering:** C / C++ with OpenGL (GLUT / GLEW libraries)
* **Low-Level Computation:** 80386 Assembly Language (Run via a CPU Simulator like DOSBox with MASM/TASM)

---

## 3. Folder and File Structure

To keep the four subjects organized, the project will follow this directory structure:

```text
RailRoute_Project/
│
├── docs/                      # Documentation files
│   ├── PRD.md
│   ├── Architecture.md
│   ├── Rules.md
│   ├── Phases.md
│   └── Design.md
│
├── src/                       # Source Code
│   ├── java/                  # PSOOP - UI and Business Logic
│   │   ├── models/            # Train.java, Passenger.java, etc.
│   │   ├── gui/               # Swing screens (MainFrame.java)
│   │   └── Main.java
│   │
│   ├── cpp/                   # Data Structures & Programming Lab
│   │   ├── data_structures/   # LinkedList.cpp, Queue.cpp
│   │   ├── algorithms/        # Sort.cpp, Search.cpp
│   │   └── core/              # RouteManager.cpp
│   │
│   ├── graphics/              # Computer Graphics Lab
│   │   ├── MapRenderer.cpp    # OpenGL map rendering
│   │   └── Animation.cpp      # Train movement logic
│   │
│   └── assembly/              # COAL
│       ├── distance_calc.asm  # 64-bit addition module
│       └── energy_score.asm   # Multiplication and logic module
│
├── data/                      # Simulated static data (Timetables)
│   └── train_data.csv
│
└── README.md                  # Main entry point instructions
```
