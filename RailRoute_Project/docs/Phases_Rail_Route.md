# Phases.md: Project Development Roadmap

To ensure steady progress and align with the SY B.Tech lab schedule and review deadlines, the RailRoute project is broken down into four distinct development phases. 

## Phase 1: Foundation & Static Data
**Target:** Review 1 (Deadline: August 31, 2026)
**Goal:** Establish the core blueprints and static memory structures.
* **Java (PSOOP):** Create foundational entity classes (`Train`, `Passenger`, `Station`, `Ticket`, `Route`) with basic attributes, constructors, and getters/setters.
* **C++ (DSA & Prog Lab):** Implement 1D arrays for hardcoded train inventories and 2D arrays (adjacency matrices) for station distances.
* **Computer Graphics:** Render the basic railway map using OpenGL primitives (Points and Lines) for stations and tracks.
* **Assembly (COAL):** Design the ALU/Control Unit logic in a CPU simulator and write foundational 64-bit addition for route distances.

## Phase 2: Dynamic Memory & Validation
**Target:** Review 2 (Deadline: October 1, 2026)
**Goal:** Introduce dynamic data management, user validation, and visual movement.
* **Java (PSOOP):** Establish class relationships (Inheritance/Interfaces) and implement custom Exception Handling (`InvalidStationException`).
* **C++ (DSA & Prog Lab):** Replace static arrays with Singly/Circular Linked Lists for reservations, Queues for waiting lists, and Stacks for cancellation history.
* **Computer Graphics:** Implement 2D/3D transformation matrices (Translation, Rotation) to animate the train object moving along tracks.
* **Assembly (COAL):** Develop ALPs for Hex-to-BCD conversion to output human-readable metrics and implement string operations.

## Phase 3: Algorithms & Advanced Visuals
**Target:** Mid-October
**Goal:** Optimize routing logic and improve graphical aesthetics.
* **Java (PSOOP):** Utilize Java Collections (ArrayLists, Maps) for runtime data management prior to GUI integration.
* **C++ (DSA & Prog Lab):** Code Sorting algorithms (Quick/Merge Sort) to rank trains by travel time, and Search algorithms (Binary/Linear) for fast station discovery.
* **Computer Graphics:** Define a world-coordinate "window" over the full station network and map it to the on-screen viewport (window-to-viewport transformation), enabling pan/zoom on the map. Use Cohen-Sutherland clipping against the resulting viewport boundary for map panning, and Bezier curves for smooth track turns. Implement an RGB↔HSV color-model conversion routine to drive the selected-route highlight's brightness/saturation dynamically.
* **Assembly (COAL):** Compute the complex "Energy Score" using 64-bit multiplication via successive addition and shift methods.

## Phase 4: GUI, Integration & Final Polish
**Target:** Review 3 (Deadline: October 23, 2026)
**Goal:** Connect all modules, apply visual polish, and build the user interface.
* **Java (PSOOP):** Develop the passenger booking flow and operator dashboard using Java Swing. Implement File Handling to save/load tickets.
* **C++ (DSA & Prog Lab):** Introduce Hash Tables for instantaneous O(1) lookups mapping Ticket IDs to passenger records.
* **Computer Graphics:** Apply texture mapping to ground terrains and station models for a realistic finish. Implement point-source illumination on station nodes (diffuse intensity falloff) to highlight the active route's source/destination stations.
* **Assembly (COAL):** Finalize and document the integration of the low-level assembly results with the Operator Dashboard metrics. Write a Hardware Architecture Rationale documenting the 80386 registers, addressing modes, and CISC instruction traits used in `distance_calc.asm` and `energy_score.asm` (COA CO1), and cross-reference the standalone Control Unit design (COA Lab Practical No. 2) as the microarchitectural basis executing these ALPs (COA CO4).
