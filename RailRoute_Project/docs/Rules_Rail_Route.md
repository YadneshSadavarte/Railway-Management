# Rules.md: Development Guidelines & Boundaries

## 1. What To Do (Core Directives)
* **Adhere to the Syllabus:** Only implement concepts taught in the SY B.Tech Semester III curriculum. 
* **Progressive Development:** Build the project week-by-week, synchronizing with the lab schedule and upcoming review deadlines.
* **Simulate Data:** Use hardcoded, predefined static data (arrays or simple CSV files) for trains, stations, distances, and elevations.
* **Memory Management:** Ensure all dynamic memory allocated in C++ (e.g., for Linked Lists) is properly deallocated to prevent memory leaks.
* **OOP Principles:** Strictly follow Encapsulation, Inheritance, and Polymorphism in the Java application layer.

## 2. What To Avoid (Scope Constraints)
* **NO Real-World Integrations:** Do not use live railway APIs (like IRCTC), real-time GPS tracking, or live payment gateways.
* **NO Over-Engineering:** Avoid Machine Learning, AI chatbots, complex 3D physics engines, or full-scale cloud backend databases (e.g., AWS, Firebase).
* **NO Advanced Frameworks:** Do not use web frameworks (Spring Boot, React) or mobile app frameworks. Stick strictly to desktop applications using Java Swing.
* **NO Pre-built Data Structures for Core Logic:** In C++, manually implement core structures (Linked Lists, Queues, Hash Tables) as taught in the DSA syllabus rather than relying entirely on the STL for graded components.

## 3. Approved Libraries
* **Java (PSOOP):** 
  * `javax.swing.*` and `java.awt.*` (for GUI)
  * `java.util.*` (for Collections like ArrayLists)
  * `java.io.*` (for basic File Handling)
* **C++ (Data Structures & Programming Lab):** 
  * `<iostream>`, `<string>`, `<fstream>`
* **Computer Graphics:** 
  * `<GL/glut.h>` or `<GL/freeglut.h>` (Standard OpenGL primitives)
* **Assembly (COAL):** 
  * Standard 80386 MASM/TASM instructions on a CPU simulator (e.g., DOSBox).

## 4. Error Handling
* **Java:** Implement custom exception classes (e.g., `InvalidStationException`, `NoSeatsAvailableException`) to gracefully handle user input errors and booking failures without crashing the application.
* **C++:** Strictly manage null pointers. Validate all Queue and Stack boundaries (prevent overflow/underflow) and handle edge cases in Linked List traversals to avoid segmentation faults.

## 5. Boundaries of AI Usage (Exam Reform Compliance)
* **Ethical Usage:** AI tools (like ChatGPT or Gemini) should support learning and planning, not replace original logic building.
* **Transparency:** Any AI-assisted code generation, debugging, or scaffolding must be explicitly documented in the final project report.
* **Validation:** All AI-generated outputs must be manually reviewed, refined, and tested to ensure they are technically correct, defendable in the ESE viva, and strictly aligned with the SY B.Tech syllabus.
