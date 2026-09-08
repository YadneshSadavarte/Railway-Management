# CO_Mapping.md: Course Outcome Traceability

This document maps every Course Outcome (CO) of the four integrated Sem III subjects to a specific feature or deliverable in RailRoute, so coverage can be verified at a glance and defended in the ESE viva.

---

## 1. Data Structures — 2310212T

| CO | Outcome | RailRoute Feature |
| :--- | :--- | :--- |
| CO1 | Explain working of linear data structures | Core data model for trains/reservations built on arrays, linked lists, stacks, queues |
| CO2 | Apply Arrays for developing applications | Phase 1: 1D arrays for train inventory, 2D adjacency matrix for station distances |
| CO3 | Choose suitable type of linked list | Phase 2: Singly/Circular linked lists for passenger reservation records |
| CO4 | Make use of Stack and Queue | Phase 2: Stack for cancellation history, Queue for waiting list |
| CO5 | Apply searching and sorting | Phase 3: Quick/Merge Sort for route ranking, Binary/Linear Search for station lookup |
| CO6 | Implement hashing | Phase 4: Hash table mapping Ticket ID to passenger record for O(1) lookup |

## 2. Programming Laboratory — 2310214L

Same CO set and mapping as DSA (2310212T) above, applied at the practical/implementation level.

## 3. Computer Graphics — 2310218T/L

| CO | Outcome | RailRoute Feature |
| :--- | :--- | :--- |
| CO1 | Apply OpenGL primitives | Phase 1: `GL_POINTS`/`GL_LINES` for station nodes and track segments |
| CO2 | Solve real-time problems using geometric transformations | Phase 2: Translation/rotation matrices animating the train along the track |
| CO3 | Develop 2D/3D objects using various algorithms | Bresenham's line algorithm for track rendering |
| CO4 | Apply projection and viewing methods | Phase 3: Window-to-viewport transformation mapping the full network's world coordinates to the screen, supporting pan/zoom |
| CO5 | Implement clipping algorithms | Phase 3: Cohen-Sutherland clipping applied against the viewport boundary for map panning |
| CO6 | Design texture, light, and color using objects | Phase 3: RGB↔HSV color-model conversion driving the selected-route highlight. Phase 4: Point-source illumination on active-route station nodes; texture mapping on terrain/station models |

## 4. Computer Organization & Architecture — 2310221T/L

| CO | Outcome | RailRoute Feature |
| :--- | :--- | :--- |
| CO1 | Explicate 80386 architecture and instruction set | Phase 4: Hardware Architecture Rationale documenting registers, addressing modes, data types, and CISC traits used in `distance_calc.asm` and `energy_score.asm` |
| CO2 | Develop ALPs using 80386 instruction set | Phase 1–2: 64-bit addition (distance calc), Hex-to-BCD conversion |
| CO3 | Illustrate arithmetic operations via computer arithmetic algorithms | Phase 3: 64-bit multiplication via successive addition and shift method (Energy Score) |
| CO4 | Demonstrate control signal generation, control unit organization, and instruction pipelining | Satisfied via the standalone COA Lab Practical No. 2 (Control Unit design in CPU simulator), cross-referenced in Phase 4 documentation as the microarchitectural basis executing the project's ALPs — not a RailRoute application feature |

## 5. Problem Solving Using OOP (Java) — 2310262L

| CO | Outcome | RailRoute Feature |
| :--- | :--- | :--- |
| CO1 | Develop solutions using OOP | `Train`, `Passenger`, `Station`, `Ticket`, `Route` entity classes with encapsulation |
| CO2 | Choose appropriate programming solution to reduce complexity | Phase 2: Inheritance/interfaces for class relationships |
| CO3 | Apply critical thinking and programming skills | Booking flow logic, custom exception handling (`InvalidStationException`) |
| CO4 | Utilize logic-building traits for real-life problems | End-to-end reservation, cancellation, and waitlist-promotion logic |

---

## Notes on Partial/Indirect Coverage

* **COA CO4** is the only CO not implemented as a RailRoute feature. It is addressed through a standalone lab practical (Control Unit design using a CPU simulator) required independently by the COA Lab syllabus, and is only cross-referenced — not re-implemented — in the project documentation.
* **COA CO1** is satisfied through documentation (architecture rationale) rather than a runtime feature, since it is an explanatory/analytical outcome rather than an implementation outcome.
