# Design.md: Integrated Project: RailRoute System - Design Guide

## 1. Introduction and Design Language
**RailRoute** is an integrated software simulation of a railway network, designed to provide Passenger and Operator experiences. This design guide aims to establish a clean, functional, and modern visual language inspired by the trusted and trustworthy aesthetic of the Indian Railways (specifically, the IRCTC booking page theme).

The core philosophy is trustworthiness, modern efficiency, and clear data visualization, mirroring the reliable presence of the Vande Bharat train.

---

## 2. Color Palette
The color palette is restricted to a clean set of trustworthy blues and pure whites, with a single high-impact accent color.

| Color | Hex | Purpose |
| :--- | :--- | :--- |
| **Primary** | `#0C1065` | Header background, main headers, key navigation, primary button text. A strong, professional dark blue. |
| **Secondary** | `#FFFFFF` | Main content background, form containers, cards, and secondary body text. Pure White for clean readability. |
| **Accent Orange** | `#F28F3B` | All primary call-to-action (CTA) buttons (e.g., "Search Trains," "Book Ticket") and selected route highlights on the map. Inspired by the IRCTC CTA orange. |
| **Subtle Gray 1** | `#F1F5F8` | Very light gray-blue. Used for subtle background fills (like navigation menu items or data table headers). |
| **Subtle Gray 2** | `#E6E9EE` | Divider lines, borders, and input field background. Ensures visual separation on the secondary white. |
| **Body Text** | `#333333` | Clear, dark gray for high readability in body text and form labels against white backgrounds. |

---

## 3. Typography
Typography must be clear, professional, and easy to read. Sans-serif fonts are required to match the digital interface feel. The Vande Bharat logo provides a good reference for a strong, bold font for key headers.

*   **Font Family:** Sans-serif (e.g., Roboto, Montserrat, or Open Sans for a modern feel).
*   **Font Weights:** Bold (for Titles), Medium (for Sub-headers), Regular (for Body Text).
*   **Font Hierarchy:**
    *   **Main Title (Vande Bharat Inspired Boldness):** Large (e.g., 24px+), Bold, Primary Dark Blue. Use for Main Page/Dashboard titles.
    *   **Header (H1/H2):** Large (e.g., 20px+), Bold, Primary Dark Blue. For module sections (Passenger, Operator).
    *   **Sub-header (H3):** Medium (e.g., 18px), Medium weight, Body Text Gray. For table titles or widget headers.
    *   **Body Text:** Regular (e.g., 14px), Dark Gray. For general content and descriptions.
    *   **Input Labels / Field Labels:** Regular, slightly smaller (e.g., 12px), Dark Gray. Clear and distinct.

---

## 4. Component Design
UI components are designed for clarity and ease of use, with clear borders and professional alignment.

### A. Header (Navigation)
*   Background: Light gray (`#F1F5F8`) or subtle gray divider. Primary blue text (`#0C1065`).
*   Navigation links and menus align horizontally. Use clear text.

### B. Buttons
*   **Primary CTA (Search Trains, Book Ticket):** Rectangular with clear border radius. Background Accent Orange (`#F28F3B`), White Text (`#FFFFFF`).
*   **Secondary/Text Button (PNR Status, charts/vacancy):** Unfilled, Primary Blue Text (`#0C1065`). Clearly clickable. Use clear icon + text.

### C. Input Fields (e.g., Book Ticket Widget)
*   Containers: White (`#FFFFFF`) with subtle gray (`#E6E9EE`) borders. Clear labels.
*   Background: White or very subtle gray fill. Dark Gray text (`#333333`).
*   Active state: Blue border highlight on focus. Swap icon between inputs. Clear dropdown and date picker components.

### D. Data Tables (e.g., Train Search Results, Route Comparison)
*   **Structure:** Modern grid with clean alignment and subtle borders.
*   **Headers:** Subtle gray (`#F1F5F8`) background, Primary Blue text (`#0C1065`).
*   **Cells:** Alternating light/white backgrounds, Clear dark body text. Action buttons clearly placed.

### E. Analytics Dashboard (Operator Module - COA)
*   Charts (e.g., Elevation, Energy Score): Bar charts use `#0C1065`. Line charts use `#0C1065` for the line with `F28F3B` accent points. Clear data labels in blue. White card containers for widgets.

---

## 5. Visualizer Module (Computer Graphics Lab - OpenGL)
The visual map visualization is designed to be clean, modern, and trustworthy, avoiding unnecessary complexity. It should resemble a modern node-and-edge diagram.

*   **Stations (Nodes):** Clear circles with a subtle border radius. Background Primary Blue (`#0C1065`).
*   **Tracks (Edges):** Thin, clean lines in Primary Blue (`#0C1065`). Use modern lines, not complex 3D tracks. Curves are smooth Bezier curves.
*   **Map Background:** Clean light gray.
*   **Selected Route:** Highlight the entire path (nodes and edges) with a thick, glowing Accent Orange (`#F28F3B`) line. The glow's brightness/saturation is computed by converting the base Accent Orange to HSV, modulating the V/S channels, and converting back to RGB for rendering — demonstrating color-model conversion rather than a static hex swap.
*   **Map Window & Viewport:** The full station network is defined in a world-coordinate "window," mapped to the on-screen OpenGL viewport via a standard window-to-viewport transformation. This supports pan/zoom on the map and defines the boundary against which Cohen-Sutherland clipping trims off-screen track segments.
*   **Station Illumination:** Source and destination stations on the active route are rendered with a simple point-source illumination effect — a diffuse brightness falloff from the node center outward — rather than a flat highlight fill.
*   **Train Animation:** A simple, modern icon of a modern train (Vande Bharat-inspired profile) moves smoothly along the selected track line. No complex 3D trains.
*   **Operator Analytics Tooltips:** Small, clean card tooltips appear over station nodes when hovered, showing ID and Elevation in blue text.

---

## 6. Layout and Grids
*   Use a modern grid system for professional alignment (e.g., a standard 12-column grid).
*   Employ responsive design principles to adapt to different screen sizes.
*   Use standard padding and margins to ensure clear separation of elements and high readability. Use consistent spacing units.

---

## 7. AI Usage Documentation

### A. AI Tool & Purpose
Large Language Models were used during the early project planning phases (generation of the PRD, Architecture, and weekly To-Do lists) to convert broad syllabus requirements into detailed and actionable development roadmaps.

### B. Prompt & Output Transparency
Prompts were structured to define the required academic scope and specific deliverables for each integrated subject, with the AI generating the base system architecture outlines, module definitions, and detailed step-by-step task breakdown strategies.

### C. Manual Refinement & Ownership
All AI-generated outputs, including this design guide, were critically evaluated and manually filtered to strip out overly complex, real-world features to ensure that the final product remains technically defendable and strictly aligned with the SY B.Tech syllabus capabilities. The final design language was manually synthesized by translating the user's specific theme and color constraints into this structured guide, with the AI serving only to facilitate document generation.
