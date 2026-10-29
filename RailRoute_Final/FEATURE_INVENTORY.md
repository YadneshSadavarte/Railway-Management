# RailRoute Feature Inventory

This document provides a comprehensive inventory of all architectural components, public signatures, GUI controls, file and database interactions, user-visible strings, and C++ rendering routines in the RailRoute application.

---

## 1. Java & DS Public/Protected Class and Method Signatures

| Class / Interface | Signature | Purpose | Verified |
|---|---|---|:---:|
| `Main` | `public static void main(String[] args)` | Application launcher | [x] |
| `Bookable` | `boolean bookSeats(int count)` | Booking contract | [x] |
| `Bookable` | `void cancelSeats(int count)` | Cancellation contract | [x] |
| `Bookable` | `int getAvailableSeats()` | Seat query contract | [x] |
| `Person` | `public Person(String name, String contactNumber)` | Abstract person constructor | [x] |
| `Person` | `public abstract String getRole()` | Abstract role identifier | [x] |
| `Person` | `public String getPersonId()` | Person ID getter | [x] |
| `Person` | `public String getName()` / `public void setName(String name)` | Name getter/setter | [x] |
| `Person` | `public String getContactNumber()` / `public void setContactNumber(String contactNumber)` | Contact getter/setter | [x] |
| `Person` | `public static <T extends Person> List<T> filterPeople(List<T> list, Predicate<T> predicate)` | Lambda-based collection filter | [x] |
| `Passenger` | `public Passenger(String name, int age, String contactNumber, String gender)` | Full passenger constructor | [x] |
| `Passenger` | `public Passenger(String name, int age)` | Chained passenger constructor | [x] |
| `Passenger` | `public String getRole()` | Overridden role ("Passenger") | [x] |
| `Passenger` | `public String getPassengerId()` / `public void setPassengerId(String passengerId)` | Passenger ID getter/setter | [x] |
| `Passenger` | `public int getAge()` / `public void setAge(int age)` | Age getter/setter with try-catch | [x] |
| `Passenger` | `public String getGender()` / `public void setGender(String gender)` | Gender getter/setter | [x] |
| `Passenger` | `public static int getIdCounter()` / `public static void setIdCounter(int counter)` | Static ID counter accessors | [x] |
| `Passenger` | `public String toString()` | String representation | [x] |
| `Operator` | `public Operator(String name, String contactNumber, String department)` | Operator constructor | [x] |
| `Operator` | `public String getRole()` | Overridden role ("Operator") | [x] |
| `Operator` | `public String getOperatorId()` / `public void setOperatorId(String operatorId)` | Operator ID getter/setter | [x] |
| `Operator` | `public String getDepartment()` / `public void setDepartment(String department)` | Department getter/setter | [x] |
| `Operator` | `public static int getIdCounter()` / `public static void setIdCounter(int counter)` | Static ID counter accessors | [x] |
| `Operator` | `public String toString()` | String representation | [x] |
| `Station` | `public Station(String stationId, String stationCode, String name, String district, int elevationMeters, double openglX, double openglY)` | Station constructor | [x] |
| `Station` | `public String getStationId()` / `public void setStationId(String stationId)` | Station ID getter/setter | [x] |
| `Station` | `public String getStationCode()` / `public void setStationCode(String stationCode)` | Station code getter/setter | [x] |
| `Station` | `public String getName()` / `public void setName(String name)` | Station name getter/setter | [x] |
| `Station` | `public String getDistrict()` / `public void setDistrict(String district)` | District getter/setter | [x] |
| `Station` | `public int getElevationMeters()` / `public void setElevationMeters(int elevationMeters)` | Elevation getter/setter | [x] |
| `Station` | `public double getOpenglX()` / `public void setOpenglX(double openglX)` | OpenGL X coordinate getter/setter | [x] |
| `Station` | `public double getOpenglY()` / `public void setOpenglY(double openglY)` | OpenGL Y coordinate getter/setter | [x] |
| `Station` | `public String toString()` | String representation | [x] |
| `Route` | `public Route(String routeId, String sourceStationId, String destinationStationId, int distanceKm, double travelTimeHours, double elevationChangeMeters)` | Route constructor | [x] |
| `Route` | `public String getRouteId()` / `public void setRouteId(String routeId)` | Route ID getter/setter | [x] |
| `Route` | `public String getSourceStationId()` / `public void setSourceStationId(String sourceStationId)` | Source getter/setter | [x] |
| `Route` | `public String getDestinationStationId()` / `public void setDestinationStationId(String destinationStationId)` | Destination getter/setter | [x] |
| `Route` | `public int getDistanceKm()` / `public void setDistanceKm(int distanceKm)` | Distance getter/setter | [x] |
| `Route` | `public double getTravelTimeHours()` / `public void setTravelTimeHours(double travelTimeHours)` | Travel time getter/setter | [x] |
| `Route` | `public double getElevationChangeMeters()` / `public void setElevationChangeMeters(double elevationChangeMeters)` | Elevation change getter/setter | [x] |
| `Route` | `public String toString()` | String representation | [x] |
| `Train` | `public Train(String trainId, String trainNumber, String trainName, String sourceStationId, String destinationStationId, int totalSeats, int availableSeats, double departureTime, double baseFare)` | Train constructor | [x] |
| `Train` | `public boolean bookSeats(int count)` | Bookable interface implementation | [x] |
| `Train` | `public void cancelSeats(int count)` | Bookable interface implementation | [x] |
| `Train` | `public String getTrainId()` / `public void setTrainId(String trainId)` | Train ID getter/setter | [x] |
| `Train` | `public String getTrainNumber()` / `public void setTrainNumber(String trainNumber)` | Train number getter/setter | [x] |
| `Train` | `public String getTrainName()` / `public void setTrainName(String trainName)` | Train name getter/setter | [x] |
| `Train` | `public String getSourceStationId()` / `public void setSourceStationId(String sourceStationId)` | Source station ID getter/setter | [x] |
| `Train` | `public String getDestinationStationId()` / `public void setDestinationStationId(String destinationStationId)` | Destination station ID getter/setter | [x] |
| `Train` | `public int getTotalSeats()` / `public void setTotalSeats(int totalSeats)` | Total seats getter/setter | [x] |
| `Train` | `public int getAvailableSeats()` / `public void setAvailableSeats(int availableSeats)` | Available seats getter/setter | [x] |
| `Train` | `public double getDepartureTime()` / `public void setDepartureTime(double departureTime)` | Departure time getter/setter | [x] |
| `Train` | `public double getBaseFare()` / `public void setBaseFare(double baseFare)` | Base fare getter/setter | [x] |
| `Train` | `public String toString()` | String representation | [x] |
| `Ticket` | `public Ticket(String ticketId, String passengerId, String trainId, String sourceStationId, String destinationStationId, int seatCount, double totalFare, String status)` | Ticket constructor | [x] |
| `Ticket` | `public String getTicketId()` / `public void setTicketId(String ticketId)` | Ticket ID getter/setter | [x] |
| `Ticket` | `public String getPassengerId()` / `public void setPassengerId(String passengerId)` | Passenger ID getter/setter | [x] |
| `Ticket` | `public String getTrainId()` / `public void setTrainId(String trainId)` | Train ID getter/setter | [x] |
| `Ticket` | `public String getSourceStationId()` / `public void setSourceStationId(String sourceStationId)` | Source getter/setter | [x] |
| `Ticket` | `public String getDestinationStationId()` / `public void setDestinationStationId(String destinationStationId)` | Destination getter/setter | [x] |
| `Ticket` | `public int getSeatCount()` / `public void setSeatCount(int seatCount)` | Seat count getter/setter | [x] |
| `Ticket` | `public double getTotalFare()` / `public void setTotalFare(double totalFare)` | Total fare getter/setter | [x] |
| `Ticket` | `public String getStatus()` / `public void setStatus(String status)` | Status getter/setter | [x] |
| `Ticket` | `public String getSeatNumber()` / `public void setSeatNumber(String seatNumber)` | Seat number getter/setter | [x] |
| `Ticket` | `public String toString()` | String representation | [x] |
| `DatabaseInitializer` | `public static Connection getConnection() throws SQLException` | SQLite JDBC connection factory | [x] |
| `DatabaseInitializer` | `public static void initializeDatabase()` | DDL schema creation and seeding | [x] |
| `DatabaseManager` | `public static Connection getConnection() throws SQLException` | Connection factory | [x] |
| `DatabaseManager` | `public static void initializeDatabase()` | Schema initialization delegate | [x] |
| `DatabaseManager` | `public static void savePassenger(Passenger p)` | Passenger insert/update | [x] |
| `DatabaseManager` | `public static void saveTicket(Ticket t)` | Ticket insert | [x] |
| `DatabaseManager` | `public static void updateTrainSeats(String trainId, int availableSeats)` | Train seat count updater | [x] |
| `DatabaseManager` | `public static void syncTrainsToDS(TrainArray trainArray, TrainLinkedList trainList)` | Synchronize SQL trains into DSA collections | [x] |
| `DatabaseManager` | `public static void saveOrUpdateTrainInSQL(String id, String name, String src, String dst, int totalSeats, int availSeats, double depTime, double baseFare)` | Train upsert | [x] |
| `DatabaseManager` | `public static void deleteTrainFromSQL(String id)` | Train deletion | [x] |
| `DatabaseManager` | `public static void updateTicketStatusInSQL(String ticketId, String newStatus)` | Ticket status updater | [x] |
| `DatabaseManager` | `public static List<Train> loadTrainsFromSQL()` | Query all trains | [x] |
| `DatabaseManager` | `public static List<Station> loadStationsFromSQL()` | Query all stations | [x] |
| `DatabaseManager` | `public static String getDatabaseSummary()` | Aggregate count summary string | [x] |
| `RouteManager` | `public RouteManager()` / `public void loadDefaultNetwork()` | Network graph constructor and builder | [x] |
| `RouteManager` | `public int getStationIndex(String input)` | Station index lookup | [x] |
| `RouteManager` | `public SegmentTrain[] findTrainsForSegment(String fromInput, String toInput, String travelClass)` | Query trains serving corridor segment | [x] |
| `RouteManager` | `public OperationalCorridor[] findOperationalRoutesBetween(String fromInput, String toInput, String travelClass)` | Find paths and assign trains | [x] |
| `RouteManager` | `public int getOperationalRouteCorridorCount(String fromInput, String toInput)` | Count operational corridors | [x] |
| `RouteManager` | `public String compareRoutesAndFares(String fromInput, String toInput, String travelClass)` | Comprehensive comparative fare report | [x] |
| `RouteManager` | `public RouteDetail getRouteDetail(String fromInput, String toInput, boolean shortest)` | Optimal/scenic path extraction | [x] |
| `RouteManager` | `public String exportSelectedRouteForOpenGL(String from, String to, String travelClass, boolean useLongest)` | Export route coordinates to file | [x] |
| `RouteManager` | `public int getStationCount()` | Station count accessor | [x] |
| `RouteManager` | `public String getStationCode(int index)` | Station code accessor | [x] |
| `RouteManager` | `public String getStationName(int index)` | Station name accessor | [x] |
| `RouteManager` | `public String getStationId(int index)` | Station ID accessor | [x] |
| `RouteManager` | `public double getStationElevation(int index)` | Station elevation accessor | [x] |
| `RouteManager` | `public double getStationOpenglX(int index)` | OpenGL X accessor | [x] |
| `RouteManager` | `public double getStationOpenglY(int index)` | OpenGL Y accessor | [x] |
| `RouteManager` | `public String getDistanceMatrixAsString()` | Formatted 32x32 adjacency matrix | [x] |
| `COAProcessorEngine` | `public static String run16BitTask1Trace()` | 16-bit 8086 register trace | [x] |
| `COAProcessorEngine` | `public static String run64BitLiveRouteTrace(RouteManager routeManager, String fromStation, String toStation)` | 64-bit ALU live route trace | [x] |
| `COAProcessorEngine` | `public static String runCo3AddShiftStringTrace(RouteManager routeManager, String fromStation, String toStation)` | CO3 Add-and-shift & string trace overload | [x] |
| `COAProcessorEngine` | `public static String runCo3AddShiftStringTrace(long distanceMeters, long elevationFactor, String tagString)` | Core CO3 execution trace | [x] |
| `COAProcessorEngine` | `public static String readAssemblySourceFiles()` | Formatted assembly source viewer | [x] |
| `TrainRecord` | `public TrainRecord()` / constructors | Custom flat data structure model | [x] |
| `TrainRecord` | `public TrainRecord copy()` / `public String toString()` | Cloner and stringifier | [x] |
| `TrainArray` | `public void clear()` | Reset train array | [x] |
| `TrainArray` | `public boolean addRecord(TrainRecord record)` | Insert record into array | [x] |
| `TrainArray` | `public int getTrainCount()` / `public TrainRecord getTrainAt(int index)` | Array accessors | [x] |
| `TrainArray` | `public int linearSearchById(String trainId)` | Linear search algorithm | [x] |
| `TrainArray` | `public TrainRecord binarySearchById(String trainId)` | Binary search algorithm | [x] |
| `TrainArray` | `public String getQuickSortedByFareAsString()` | Custom QuickSort implementation | [x] |
| `TrainArray` | `public String getAllArrayTrainsAsString()` | Formatted tabular printout | [x] |
| `TrainLinkedList` | `public void clear()` / `public void appendRecord(TrainRecord train)` | List management | [x] |
| `TrainLinkedList` | `public String insertTrainFromGUI(String id, String name, String source, String dest, int seats, double depTime, double fare)` | GUI insert adapter | [x] |
| `TrainLinkedList` | `public String displayAllTrainsAsString()` | Linked list formatted dump | [x] |
| `TrainLinkedList` | `public TrainRecord findTrainById(String id)` | Node search by ID | [x] |
| `TrainLinkedList` | `public String searchTrainAsString(String id)` | Search result reporter | [x] |
| `TrainLinkedList` | `public String updateTrainFromGUI(String id, String newName, String newSource, String newDest, int newSeats, double newFare)` | Update node | [x] |
| `TrainLinkedList` | `public String deleteTrainById(String id)` | Node deletion | [x] |
| `WaitingListQueue` | `public boolean isFull()` / `public boolean isEmpty()` / `public int getSize()` | State queries | [x] |
| `WaitingListQueue` | `public String[] peek()` | Inspect front element | [x] |
| `WaitingListQueue` | `public String enqueue(String passengerId, String passengerName, String trainId, double fare)` | Circular queue enqueue | [x] |
| `WaitingListQueue` | `public String[] dequeueForTrain(String trainId)` | Circular queue FIFO dequeue | [x] |
| `WaitingListQueue` | `public String displayQueueAsString()` | Formatted queue dump | [x] |
| `WaitingListQueue` | `public static String runBoundaryCaseTests()` | CO3 normal & boundary test suite | [x] |
| `CancellationStack` | `public boolean isFull()` / `public boolean isEmpty()` / `public int getSize()` | State queries | [x] |
| `CancellationStack` | `public String[] peek()` | Inspect top element | [x] |
| `CancellationStack` | `public void push(String ticketId, String passengerId, String trainId, double fare)` | LIFO push operation | [x] |
| `CancellationStack` | `public String[] pop()` | LIFO pop operation | [x] |
| `CancellationStack` | `public String displayStackAsString()` | Formatted stack dump | [x] |
| `TicketHashTable` | `public void put(String ticketId, String summary)` | Separate chaining hash table insert | [x] |
| `TicketHashTable` | `public String get(String ticketId)` | Hash table key lookup | [x] |
| `TicketHashTable` | `public void remove(String ticketId)` | Hash table key removal | [x] |
| `TicketHashTable` | `public String displayHashTableAsString()` | Formatted bucket dump | [x] |

---

## 2. GUI Tabs, Controls, Actions, and Listeners

### Global Navigation & Header
- **Nav Button 1 (`navBtn1`):** "1. Route Discovery & Booking" -> switches to Tab 1 (`tab1`).
- **Nav Button 2 (`navBtn2`):** "2. Operations & Inventory" -> switches to Tab 2 (`tab2`).
- **Nav Button 3 (`navBtn3`):** "3. 2D Live Map Visualizer" -> switches to Tab 3 (`tab3`).
- **Nav Button 4 (`navBtn4`):** "4. COA Processor & Energy" -> switches to Tab 4 (`tab4`).
- **Top Header Button (`btnConsole`):** "Output Console" -> toggles output popup overlay modal.
- **Top Header Button (`btnHelp`):** "Keyboard & Help" -> displays shortcuts modal.
- **Top Header Button (`btnExit`):** "Exit App" -> prompts and exits application.

### Tab 1: Route Discovery & Booking
- **ComboBoxes:** `cmbFrom`, `cmbTo` (origin and destination station dropdowns), `cmbTravelClass` (Sleeper, Chair Car, 3A).
- **TextFields:** `txtPassName` (passenger name), `txtPassAge` (age), `txtPassContact` (contact number), `txtSeatsCount` (seat quantity).
- **ComboBox:** `cmbGender` (Male, Female, Other).
- **Button:** `btnSearchRoutes` ("\uD83D\uDD0D  Find Routes & Trains") -> executes `findRoutesAndTrains()`, populates corridor cards.
- **Button:** `btnCompareFares` ("\uD83D\uDCCA  Compare All Routes & Fares") -> executes `compareRoutesAndFares()`, outputs comparative breakdown.
- **Dynamic Corridor Booking Buttons:** Generated for each available operational corridor -> executes `executeCorridorBooking(...)`.

### Tab 2: Operations & Inventory
- **Header Action:** Cancellation and Refund Buffer.
- **TextField:** `txtCancelTicketId` -> ticket cancellation input.
- **Button:** `btnExecuteCancel` ("Execute Cancellation & Waitlist Promotion") -> executes `cancelTicketAndPromoteWaitlist()`.
- **CRUD Operations for Train Inventory:**
  - `txtTId`, `txtTName`, `txtTSrc`, `txtTDst`, `txtTSeats`, `txtTDep`, `txtTFare`.
  - `btnAddTrain` ("Add Train") -> `addTrainDS()`
  - `btnUpdateTrain` ("Update Train") -> `updateTrainDS()`
  - `btnDeleteTrain` ("Delete Train") -> `deleteTrainDS()`
  - `btnBinarySearch` ("Binary Search") -> `binarySearchDS()`
- **Data Structure Verification Grid (`dsGrid`):**
  - `btnDisplayLL` ("Linked List (CO3)") -> `displayLinkedListDS()`
  - `btnShowArray` ("1D Array (CO2)") -> `show1DArrayDS()`
  - `btnQuickSort` ("QuickSort Fares (CO5)") -> `quickSortDS()`
  - `btnAdjMatrix` ("Adjacency Matrix (CO2)") -> `showAdjacencyMatrix()`
  - `btnQueueStack` ("Queue & Stack (CO4)") -> `showQueueAndStack()`
  - `btnBoundaryTest` ("Test CO3 Stack/Queue Boundary Cases") -> `testStackQueueBoundaryCases()`
  - `btnHashLookup` ("Hash Table (CO6)") -> `showHashTableLookup()`

### Tab 3: 2D Live Map Visualizer
- **ComboBoxes:** `cmbTab3From`, `cmbTab3To`, `cmbTab3Class`.
- **RadioButtons:** `rbTab3Shortest`, `rbTab3Longest`.
- **Button:** `btnSyncRoute` ("\u21BB  1. Sync Route Coordinates to OpenGL (active_route.txt)") -> `syncRouteToOpenGL()`.
- **Button:** `btnLaunchOpenGL` ("\u25B6  2. Launch Full 2D OpenGL Maharashtra Railway Visualizer") -> `launchOpenGLVisualizer()`.

### Tab 4: COA Processor & Energy
- **ComboBoxes:** `cmbTab4From`, `cmbTab4To`.
- **Button:** `btnRun64Bit` ("\u25B6  1. Run 64-Bit Live Route & IMUL Energy Trace") -> `run64BitLiveTrace()`.
- **Button:** `btnRunCo3Trace` ("\u25B6  Run CO3: Add-and-Shift + 64-Bit String Trace") -> `runCo3AddShiftStringTrace()`.
- **Button:** `btnRun16Bit` ("\u2699  2. Run 16-Bit 8086 Processor Fetch-Decode-Execute Trace") -> `run16BitTask1Trace()`.
- **Button:** `btnRunAsmExe` ("\u25B6  3. Execute Native 64-Bit Assembly Binary (distance_calc.exe)") -> `executeNativeAssemblyExe()`.
- **Button:** `btnViewAsm` ("\uD83D\uDCC4  4. Inspect Raw Assembly Source Files (.asm)") -> `viewAssemblySourceFiles()`.

---

## 3. Files, Process Launches, and Database Schema

### File I/O
- `data/railroute.db`: SQLite database file (read/write).
- `data/active_route.txt`: OpenGL route sync file (written by Java, read by C++ FreeGLUT).
- `src/COA/coa_simulator/Task1_ProcessorModeling.asm`: Assembly source read by `COAProcessorEngine.readAssemblySourceFiles()`.
- `src/COA/assembly/distance_calc.asm`: Assembly source read by `COAProcessorEngine.readAssemblySourceFiles()`.

### Subprocess / ProcessBuilder Launches
- `build/output/distance_calc.exe` (or `distance_calc.exe` fallback): Launched by `executeNativeAssemblyExe()`.
- `build/output/railroute_visualizer.exe` (or `railroute_visualizer.exe` fallback): Launched by `launchOpenGLVisualizer()`.

### Database Schema (SQLite)
- **Table `stations`:** `station_id` (TEXT PK), `station_code` (TEXT), `name` (TEXT), `district` (TEXT), `elevation_meters` (INTEGER), `opengl_x` (REAL), `opengl_y` (REAL).
- **Table `routes`:** `route_id` (TEXT PK), `source_station_id` (TEXT), `destination_station_id` (TEXT), `distance_km` (INTEGER), `travel_time_hours` (REAL), `elevation_change_meters` (REAL).
- **Table `trains`:** `train_id` (TEXT PK), `train_number` (TEXT), `train_name` (TEXT), `source_station_id` (TEXT), `destination_station_id` (TEXT), `total_seats` (INTEGER), `available_seats` (INTEGER), `departure_time` (REAL), `base_fare` (REAL).
- **Table `passengers`:** `passenger_id` (TEXT PK), `name` (TEXT), `age` (INTEGER), `contact_number` (TEXT), `gender` (TEXT).
- **Table `tickets`:** `ticket_id` (TEXT PK), `passenger_id` (TEXT), `train_id` (TEXT), `source_station_id` (TEXT), `destination_station_id` (TEXT), `seat_count` (INTEGER), `total_fare` (REAL), `status` (TEXT).

---

## 4. User-Visible Output Strings

- **COA 16-Bit Trace:** Headers and cycle logs (`COA CO1 & CO2: 16-BIT 8086 MICROPROCESSOR PIPELINE...`).
- **COA 64-Bit Live Trace:** Register dumps and flag comparisons (`COA CO2, CO3 & CO4: 64-BIT WIN64 REGISTER...`).
- **COA CO3 Trace:** Cycle-by-cycle add-and-shift, successive addition, and LODSB/STOSB conversion logs.
- **DSA Boundary Test Suite:** 7 case pass/fail evaluations and final summary (`TEST SUITE SUMMARY: 7/7 cases passed`).
- **Route Comparison Report:** Tabular comparison of shortest vs alternative routes, distances, times, and fares.
- **Data Structure Tabular Dumps:** Formatted headers, dividers (`===`, `---`), and formatted columns for TrainArray, TrainLinkedList, TicketHashTable, WaitingListQueue, CancellationStack.
- **Assembly Native Output:** Sections 1 through 5 output from `distance_calc.exe`.

---

## 5. C++ Graphics Routines (`src/graphics/`)

| Function | File | Visual Primitive / Purpose | Verified |
|---|---|---|:---:|
| `cohenSutherlandClip` | `ClippingUtil.cpp` | Cohen-Sutherland line clipping algorithm with bitwise outcodes | [x] |
| `computeOutCode` | `ClippingUtil.cpp` | Computes outcodes for point relative to clip window | [x] |
| `calculateVisibleBounds` | `ClippingUtil.cpp` | Computes orthographic visible boundaries from pan, zoom, and aspect ratio | [x] |
| `initTextures` | `MapRenderer.cpp` | Generates procedural 128x128 mipmapped ballast and brushed stainless steel textures | [x] |
| `cleanupTextures` | `MapRenderer.cpp` | Deletes OpenGL texture resources on shutdown | [x] |
| `setupSceneLighting` / `disableSceneLighting` | `MapRenderer.cpp` | Configures OpenGL fixed-function lights (GL_LIGHT0 sun, GL_LIGHT1 train point-source) and material specular sheen | [x] |
| `drawMapBoundary` | `MapRenderer.cpp` | Draws Maharashtra geographical boundary polygon, outline, and coastline | [x] |
| `drawDistrictDemarcations` | `MapRenderer.cpp` | Draws stippled demarcation lines separating rail zones | [x] |
| `drawDoubleRailSegment` | `MapRenderer.cpp` | Draws textured ballast shoulder quad, sleepers, and dual steel rails | [x] |
| `drawTracks` | `MapRenderer.cpp` | Iterates tracks, applies Cohen-Sutherland clipping, renders track segments | [x] |
| `drawActiveRoute` | `MapRenderer.cpp` | Draws modulated textured ballast strip and triple-layered glowing route lines | [x] |
| `drawStationGlow` | `MapRenderer.cpp` | Smooth radial quadratic falloff halo using non-additive alpha blending | [x] |
| `getStationLabelLayout` | `MapRenderer.cpp` | Calculates collision-avoidance label offsets and pill bounds across 5 geographic clusters | [x] |
| `drawPill` | `MapRenderer.cpp` | Renders translucent label pill backings with border outlines for maximum readability | [x] |
| `drawStations` | `MapRenderer.cpp` | 3-pass rendering: Pass 1 glow halos, Pass 2 sharp junction discs, Pass 3 pills and text | [x] |
| `drawTrain` / `drawTrainCar` | `MapRenderer.cpp` | Renders 3-car Vande Bharat consist with metallic texture body, aerodynamic nose, headlight beam, and windows | [x] |
| `drawHUD` | `MapRenderer.cpp` | Renders docked top telemetry banner and bottom keyboard control bar | [x] |
| `drawLegend` | `MapRenderer.cpp` | Renders bottom-right interactive map legend explaining corridor, tracks, stations, and train | [x] |
| `drawSelectedRoutePanel` | `MapRenderer.cpp` | Renders right-docked Selected Route Itinerary panel with journey timeline, origin/dest tags, and telemetry | [x] |
| `drawString` | `MapRenderer.cpp` | Renders bitmap text using `glutBitmapCharacter` | [x] |
| `rgbToHsv` / `hsvToRgb` | `MapRenderer.cpp` | Converts RGB color models for dynamic animated HSV pulse | [x] |
| `toggleLighting` / `toggleTextures` | `MapRenderer.cpp` | Real-time interactive toggles for Task 3 faculty evaluation | [x] |
| `render` | `MapRenderer.cpp` | Main rendering coordinator (clear, transforms, scene primitives, swap) | [x] |
