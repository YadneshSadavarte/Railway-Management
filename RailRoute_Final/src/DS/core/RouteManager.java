package DS.core;

import DS.data_structures.TrainRecord;
import database.DatabaseInitializer;
import database.DatabaseManager;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import models.Train;

public class RouteManager {
    public static final int MAX_STATIONS = 35;
    public static final int NO_DIRECT_ROUTE = -1;

    private static class EdgeNode {
        int toIndex;
        int distanceKm;
        double travelTimeHr;
        EdgeNode next;

        EdgeNode(int toIndex, int distanceKm, double travelTimeHr, EdgeNode next) {
            this.toIndex = toIndex;
            this.distanceKm = distanceKm;
            this.travelTimeHr = travelTimeHr;
            this.next = next;
        }
    }

    public static class RouteDetail {
        public int[] pathIndices;
        public int pathLength;
        public int totalDistanceKm;
        public double travelTimeHr;
        public double maxElevationMeters;
        public int[] legDistancesKm;
    }

    public static class SegmentTrain {
        public String trainId;
        public String trainNumber;
        public String trainName;
        public double departureTime;
        public int segmentDistanceKm;
        public double segmentTravelTimeHr;
        public double fare;
        public int availableSeats;
        public int totalSeats;
        public String stopsSummary;
        public String displayLabel;
    }

    public static class OperationalCorridor {
        public int[] pathIndices;
        public int pathLength;
        public int distanceKm;
        public double travelTimeHr;
        public double maxElevationMeters;
        public String[] trainNumbers = new String[20];
        public String[] trainNames = new String[20];
        public int trainCount = 0;
        public double fare;
        public String pathString;
    }

    private String[] stationIds = new String[MAX_STATIONS];
    private String[] irctcCodes = new String[MAX_STATIONS];
    private String[] stationNames = new String[MAX_STATIONS];
    private double[] elevations = new double[MAX_STATIONS];
    private double[] openglX = new double[MAX_STATIONS];
    private double[] openglY = new double[MAX_STATIONS];
    private EdgeNode[] adjacencyList = new EdgeNode[MAX_STATIONS];
    private int stationCount = 0;

    private TrainRecord[] operationalTrains = new TrainRecord[60];
    private int trainCount = 0;



    public RouteManager() {
        loadDefaultNetwork();
    }

    public void loadDefaultNetwork() {
        stationCount = 0;
        trainCount = 0;
        for (int i = 0; i < MAX_STATIONS; i++) {
            adjacencyList[i] = null;
        }

        addStation("MH-001", "NDB",  "Nandurbar", 500.0, -0.496, 0.784);
        addStation("MH-002", "DHI",  "Dhule", 500.0, -0.383, 0.641);
        addStation("MH-003", "JL",   "Jalgaon Junction", 500.0, -0.212, 0.679);
        addStation("MH-004", "AK",   "Akola Junction", 500.0, 0.098, 0.593);
        addStation("MH-005", "AMI",  "Amravati", 500.0, 0.260, 0.654);
        addStation("MH-006", "NGP",  "Nagpur Junction", 310.0, 0.546, 0.719);
        addStation("MH-007", "BRD",  "Bhandara Road", 500.0, 0.667, 0.744);
        addStation("MH-008", "G",    "Gondia Junction", 500.0, 0.783, 0.810);
        addStation("MH-009", "NK",   "Nashik Road", 565.0, -0.583, 0.366);
        addStation("MH-010", "PLG",  "Palghar", 500.0, -0.813, 0.293);
        addStation("MH-011", "TNA",  "Thane", 9.0, -0.769, 0.142);
        addStation("MH-012", "BA",   "Bandra Terminus", 12.0, -0.798, 0.104);
        addStation("MH-013", "CSMT", "Mumbai CSMT", 8.0, -0.799, 0.070);
        addStation("MH-014", "PUNE", "Pune Junction", 559.0, -0.576, -0.050);
        addStation("MH-015", "ANG",  "Ahilyanagar", 500.0, -0.393, 0.110);
        addStation("MH-016", "BEED", "Beed", 500.0, -0.176, 0.080);
        addStation("MH-017", "AWB",  "Chhatrapati Sambhajinagar", 500.0, -0.267, 0.340);
        addStation("MH-018", "J",    "Jalna", 500.0, -0.141, 0.331);
        addStation("MH-019", "WHM",  "Washim", 500.0, 0.129, 0.411);
        addStation("MH-020", "HNL",  "Hingoli Deccan", 500.0, 0.128, 0.301);
        addStation("MH-021", "WR",   "Wardha Junction", 500.0, 0.440, 0.596);
        addStation("MH-022", "CD",   "Chandrapur", 500.0, 0.592, 0.369);
        addStation("MH-023", "PBN",  "Parbhani Junction", 500.0, 0.048, 0.163);
        addStation("MH-024", "NED",  "Huzur Sahib Nanded", 500.0, 0.164, 0.135);
        addStation("MH-025", "LUR",  "Latur", 500.0, 0.001, -0.079);
        addStation("MH-026", "UMD",  "Dharashiv (Usmanabad)", 500.0, -0.113, -0.132);
        addStation("MH-027", "SUR",  "Solapur Junction", 457.0, -0.141, -0.304);
        addStation("MH-028", "STR",  "Satara", 500.0, -0.535, -0.297);
        addStation("MH-029", "RN",   "Ratnagiri", 500.0, -0.687, -0.497);
        addStation("MH-030", "SLI",  "Sangli", 500.0, -0.422, -0.540);
        addStation("MH-031", "KOP",  "Kolhapur SCSMT", 500.0, -0.498, -0.585);
        addStation("MH-032", "KUDL", "Kudal", 500.0, -0.618, -0.786);

        loadTrainsFromStatic();
        buildTracksFromTrains();
        syncWithDatabase();
    }

    private void loadTrainsFromStatic() {
        trainCount = 0;
        for (String[] row : DatabaseInitializer.TRAINS_DATA) {
            if (trainCount < operationalTrains.length) {
                operationalTrains[trainCount++] = new TrainRecord(
                    row[0], row[1], row[2], row[3], row[4],
                    Integer.parseInt(row[5]), Integer.parseInt(row[6]),
                    Double.parseDouble(row[7]), Double.parseDouble(row[8]),
                    row[9], row[10], row[11], row[12]
                );
            }
        }
    }

    private void buildTracksFromTrains() {
        for (int t = 0; t < trainCount; t++) {
            TrainRecord tr = operationalTrains[t];
            String[] ids = tr.routeStationIds.split("\\|");
            String[] dists = tr.cumulativeDistancesKm.split("\\|");
            String[] times = tr.cumulativeTimesHr.split("\\|");
            for (int i = 0; i < ids.length - 1; i++) {
                int u = getStationIndex(ids[i]);
                int v = getStationIndex(ids[i + 1]);
                if (u != -1 && v != -1 && u != v) {
                    int dist = Math.abs(Integer.parseInt(dists[i + 1]) - Integer.parseInt(dists[i]));
                    double time = Math.abs(Double.parseDouble(times[i + 1]) - Double.parseDouble(times[i]));
                    if (dist == 0) dist = 50;
                    if (time <= 0.0) time = 1.0;
                    addEdge(u, v, dist, time);
                    addEdge(v, u, dist, time);
                }
            }
        }
    }

    private void syncWithDatabase() {
        try {
            List<Train> dbTrains = DatabaseManager.loadTrainsFromSQL();
            if (dbTrains != null && !dbTrains.isEmpty()) {
                for (Train t : dbTrains) {
                    for (int i = 0; i < trainCount; i++) {
                        if (operationalTrains[i].trainId.equalsIgnoreCase(t.getTrainId())) {
                            operationalTrains[i].availableSeats = t.getAvailableSeats();
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void addStation(String id, String code, String name, double elevation, double x, double y) {
        if (stationCount >= MAX_STATIONS) return;
        stationIds[stationCount] = id;
        irctcCodes[stationCount] = code;
        stationNames[stationCount] = name;
        elevations[stationCount] = elevation;
        openglX[stationCount] = x;
        openglY[stationCount] = y;
        stationCount++;
    }

    private void addEdge(int from, int to, int distKm, double timeHr) {
        if (from < 0 || from >= stationCount || to < 0 || to >= stationCount) return;
        EdgeNode curr = adjacencyList[from];
        while (curr != null) {
            if (curr.toIndex == to) {
                if (distKm < curr.distanceKm) {
                    curr.distanceKm = distKm;
                    curr.travelTimeHr = timeHr;
                }
                return;
            }
            curr = curr.next;
        }
        adjacencyList[from] = new EdgeNode(to, distKm, timeHr, adjacencyList[from]);
    }

    public int getStationIndex(String input) {
        if (input == null || input.trim().isEmpty()) return -1;
        String clean = input.trim();
        for (int i = 0; i < stationCount; i++) {
            if (stationIds[i].equalsIgnoreCase(clean) || irctcCodes[i].equalsIgnoreCase(clean) || stationNames[i].equalsIgnoreCase(clean)) {
                return i;
            }
        }
        for (int i = 0; i < stationCount; i++) {
            if (clean.startsWith(stationIds[i]) || clean.contains(irctcCodes[i])) {
                return i;
            }
        }
        return -1;
    }

    private double[] getClassRates(String travelClass) {
        if (travelClass != null) {
            if (travelClass.contains("CC") || travelClass.toLowerCase().contains("chair")) {
                return new double[]{1.75, 60.0};
            } else if (travelClass.contains("3A") || travelClass.toLowerCase().contains("tier")) {
                return new double[]{2.40, 80.0};
            }
        }
        return new double[]{1.0, 40.0};
    }

    private int[] findStationPositions(String[] ids, String[] codes, String fromId, String fromCode, String toId, String toCode) {
        int posFrom = -1;
        int posTo = -1;
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equalsIgnoreCase(fromId) || (i < codes.length && codes[i].equalsIgnoreCase(fromCode))) {
                posFrom = i;
            }
            if (ids[i].equalsIgnoreCase(toId) || (i < codes.length && codes[i].equalsIgnoreCase(toCode))) {
                posTo = i;
            }
        }
        return new int[]{posFrom, posTo};
    }

    public SegmentTrain[] findTrainsForSegment(String fromInput, String toInput, String travelClass) {
        int u = getStationIndex(fromInput);
        int v = getStationIndex(toInput);
        if (u == -1 || v == -1 || u == v) {
            return new SegmentTrain[0];
        }

        String targetFromId = stationIds[u];
        String targetToId = stationIds[v];
        String targetFromCode = irctcCodes[u];
        String targetToCode = irctcCodes[v];

        SegmentTrain[] temp = new SegmentTrain[60];
        int count = 0;

        double[] rates = getClassRates(travelClass);
        double classMultiplier = rates[0];
        double fixedSurcharge = rates[1];

        for (int t = 0; t < trainCount; t++) {
            TrainRecord tr = operationalTrains[t];
            String[] ids = tr.routeStationIds.split("\\|");
            String[] codes = tr.routeStationCodes.split("\\|");
            String[] dists = tr.cumulativeDistancesKm.split("\\|");
            String[] times = tr.cumulativeTimesHr.split("\\|");

            int[] pos = findStationPositions(ids, codes, targetFromId, targetFromCode, targetToId, targetToCode);
            int posFrom = pos[0];
            int posTo = pos[1];

            if (posFrom != -1 && posTo != -1 && posFrom < posTo) {
                int segDist = Math.abs(Integer.parseInt(dists[posTo]) - Integer.parseInt(dists[posFrom]));
                double segTime = Math.abs(Double.parseDouble(times[posTo]) - Double.parseDouble(times[posFrom]));
                double calculatedFare = (segDist * tr.baseFare * classMultiplier) + fixedSurcharge;

                StringBuilder stopSb = new StringBuilder();
                for (int s = posFrom; s <= posTo; s++) {
                    stopSb.append(s < codes.length ? codes[s] : ids[s]);
                    if (s < posTo) stopSb.append(" -> ");
                }

                SegmentTrain st = new SegmentTrain();
                st.trainId = tr.trainId;
                st.trainNumber = tr.trainNumber;
                st.trainName = tr.trainName;
                st.departureTime = tr.departureTime;
                st.segmentDistanceKm = segDist;
                st.segmentTravelTimeHr = segTime;
                st.fare = calculatedFare;
                st.availableSeats = tr.availableSeats;
                st.totalSeats = tr.totalSeats;
                st.stopsSummary = stopSb.toString();
                st.displayLabel = tr.trainId + " - " + tr.trainNumber + " " + tr.trainName
                        + " (" + segDist + " km | " + String.format("%.1f", segTime) + " hr | Dep: " + String.format("%.2f", tr.departureTime)
                        + " | Rs." + String.format("%.2f", calculatedFare) + " | Seats: " + tr.availableSeats + "/" + tr.totalSeats + ")";

                temp[count++] = st;
            }
        }

        SegmentTrain[] result = new SegmentTrain[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    public OperationalCorridor[] findOperationalRoutesBetween(String fromInput, String toInput, String travelClass) {
        int u = getStationIndex(fromInput);
        int v = getStationIndex(toInput);
        if (u == -1 || v == -1 || u == v) {
            return new OperationalCorridor[0];
        }

        String targetFromId = stationIds[u];
        String targetToId = stationIds[v];
        String targetFromCode = irctcCodes[u];
        String targetToCode = irctcCodes[v];

        OperationalCorridor[] tempCorridors = new OperationalCorridor[10];
        int corridorCount = 0;

        double[] rates = getClassRates(travelClass);
        double classMultiplier = rates[0];
        double fixedSurcharge = rates[1];

        for (int t = 0; t < trainCount; t++) {
            TrainRecord tr = operationalTrains[t];
            String[] ids = tr.routeStationIds.split("\\|");
            String[] codes = tr.routeStationCodes.split("\\|");
            String[] dists = tr.cumulativeDistancesKm.split("\\|");
            String[] times = tr.cumulativeTimesHr.split("\\|");

            int[] pos = findStationPositions(ids, codes, targetFromId, targetFromCode, targetToId, targetToCode);
            int posFrom = pos[0];
            int posTo = pos[1];

            if (posFrom != -1 && posTo != -1 && posFrom < posTo) {
                int pathLen = posTo - posFrom + 1;
                int[] pathIdx = new int[pathLen];
                StringBuilder pathSb = new StringBuilder();

                for (int p = 0; p < pathLen; p++) {
                    int stIdx = getStationIndex(ids[posFrom + p]);
                    pathIdx[p] = stIdx;
                    pathSb.append(irctcCodes[stIdx]);
                    if (p < pathLen - 1) pathSb.append(" -> ");
                }

                String pathSig = pathSb.toString();
                int existingIdx = -1;
                for (int c = 0; c < corridorCount; c++) {
                    if (tempCorridors[c].pathString.equals(pathSig)) {
                        existingIdx = c;
                        break;
                    }
                }

                int segDist = Math.abs(Integer.parseInt(dists[posTo]) - Integer.parseInt(dists[posFrom]));
                double segTime = Math.abs(Double.parseDouble(times[posTo]) - Double.parseDouble(times[posFrom]));
                double calculatedFare = (segDist * tr.baseFare * classMultiplier) + fixedSurcharge;

                if (existingIdx != -1) {
                    OperationalCorridor ec = tempCorridors[existingIdx];
                    if (ec.trainCount < ec.trainNumbers.length) {
                        ec.trainNumbers[ec.trainCount] = tr.trainNumber;
                        ec.trainNames[ec.trainCount] = tr.trainName;
                        ec.trainCount++;
                    }
                } else {
                    if (corridorCount < tempCorridors.length) {
                        OperationalCorridor nc = new OperationalCorridor();
                        nc.pathIndices = pathIdx;
                        nc.pathLength = pathLen;
                        nc.distanceKm = segDist;
                        nc.travelTimeHr = segTime;
                        nc.maxElevationMeters = getMaxElevation(pathIdx, pathLen);
                        nc.fare = calculatedFare;
                        nc.pathString = pathSig;
                        nc.trainNumbers[0] = tr.trainNumber;
                        nc.trainNames[0] = tr.trainName;
                        nc.trainCount = 1;
                        tempCorridors[corridorCount++] = nc;
                    }
                }
            }
        }

        for (int i = 0; i < corridorCount - 1; i++) {
            for (int j = 0; j < corridorCount - i - 1; j++) {
                if (tempCorridors[j].distanceKm > tempCorridors[j + 1].distanceKm) {
                    OperationalCorridor swap = tempCorridors[j];
                    tempCorridors[j] = tempCorridors[j + 1];
                    tempCorridors[j + 1] = swap;
                }
            }
        }

        OperationalCorridor[] result = new OperationalCorridor[corridorCount];
        System.arraycopy(tempCorridors, 0, result, 0, corridorCount);
        return result;
    }

    public int getOperationalRouteCorridorCount(String fromInput, String toInput) {
        OperationalCorridor[] corridors = findOperationalRoutesBetween(fromInput, toInput, "SL");
        return corridors.length;
    }

    private void appendCorridorDetails(StringBuilder sb, String header, OperationalCorridor c, String elevDelta) {
        sb.append(header).append("\n");
        sb.append("   Path: ").append(formatDetailedPath(c.pathIndices, c.pathLength)).append("\n");
        sb.append(String.format("   Distance:     %d km (%d meters)\n", c.distanceKm, c.distanceKm * 1000));
        sb.append(String.format("   Travel Time:  %.1f hours\n", c.travelTimeHr));
        sb.append(String.format("   Max Altitude: %.1f m%s\n", c.maxElevationMeters, elevDelta));
        sb.append(String.format("   Segment Fare: Rs. %.2f\n", c.fare));
        sb.append("   Operating Services:\n");
        for (int i = 0; i < c.trainCount; i++) {
            sb.append("     - ").append(c.trainNumbers[i]).append(" ").append(c.trainNames[i]).append("\n");
        }
        sb.append("\n");
    }

    public String compareRoutesAndFares(String fromInput, String toInput, String travelClass) {
        int startIdx = getStationIndex(fromInput);
        int endIdx = getStationIndex(toInput);

        if (startIdx == -1 || endIdx == -1) {
            return "Error: Could not identify valid start or end station indices from inputs.";
        }
        if (startIdx == endIdx) {
            return "Origin and Destination stations are identical (" + irctcCodes[startIdx] + "). No travel required.";
        }

        OperationalCorridor[] corridors = findOperationalRoutesBetween(fromInput, toInput, travelClass);

        String classLabel = "Sleeper Class (SL)";
        if (travelClass != null) {
            if (travelClass.contains("CC") || travelClass.toLowerCase().contains("chair")) {
                classLabel = "AC Chair Car (CC)";
            } else if (travelClass.contains("3A") || travelClass.toLowerCase().contains("tier")) {
                classLabel = "AC 3-Tier (3A)";
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("   REAL OPERATIONAL RAILWAY CORRIDOR & FARE COMPARATOR (INDIAN RAILWAYS TIMETABLE)      \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("Origin Station:      %-24s (%s, %.1f m)\n", stationNames[startIdx], irctcCodes[startIdx], elevations[startIdx]));
        sb.append(String.format("Destination Station: %-24s (%s, %.1f m)\n", stationNames[endIdx], irctcCodes[endIdx], elevations[endIdx]));
        sb.append(String.format("Travel Class:        %s\n", classLabel));
        sb.append("----------------------------------------------------------------------------------------\n\n");

        if (corridors.length >= 2) {
            OperationalCorridor shortest = corridors[0];
            OperationalCorridor longest = corridors[corridors.length - 1];

            exportActiveRouteToFileWithMode(shortest.pathIndices, shortest.pathLength,
                                            longest.pathIndices, longest.pathLength, "SHORTEST");

            appendCorridorDetails(sb, "1. SHORTEST OPERATIONAL ROUTE (OPTIMAL DIRECT CORRIDOR):", shortest,
                    String.format(" (Elevation Delta: %.1f m)", Math.abs(elevations[endIdx] - elevations[startIdx])));

            if (corridors.length >= 3) {
                OperationalCorridor mid = corridors[1];
                sb.append("2. INTERMEDIATE ALTERNATIVE ROUTE:\n");
                sb.append("   Path: ").append(formatDetailedPath(mid.pathIndices, mid.pathLength)).append("\n");
                sb.append(String.format("   Distance:     %d km | Travel Time: %.1f hrs | Fare: Rs. %.2f\n", mid.distanceKm, mid.travelTimeHr, mid.fare));
                sb.append("   Operating Services: ").append(mid.trainNumbers[0]).append(" ").append(mid.trainNames[0]).append("\n\n");
            }

            appendCorridorDetails(sb, String.format("%d. LONGEST OPERATIONAL ROUTE (ALTERNATIVE SCENIC CORRIDOR):", (corridors.length >= 3 ? 3 : 2)), longest, "");

            int distSavings = longest.distanceKm - shortest.distanceKm;
            double fareSavings = longest.fare - shortest.fare;
            double timeSavings = longest.travelTimeHr - shortest.travelTimeHr;

            sb.append("========================================================================================\n");
            sb.append("                    NET PASSENGER SAVINGS & OPTIMIZATION METRICS                        \n");
            sb.append("========================================================================================\n");
            sb.append(String.format("Distance Saved:  %d km (Reduced by %.1f%%)\n", distSavings, ((double) distSavings / longest.distanceKm * 100.0)));
            sb.append(String.format("Time Saved:      %.1f hours\n", timeSavings));
            sb.append(String.format("Fare Saved:      Rs. %.2f per passenger\n", fareSavings));
            sb.append("OpenGL Telemetry: Both corridors synchronized to 'data/active_route.txt' for 2D animation.\n");
            sb.append("========================================================================================\n");
        } else if (corridors.length == 1) {
            OperationalCorridor single = corridors[0];
            exportActiveRouteToFileWithMode(single.pathIndices, single.pathLength,
                                            single.pathIndices, single.pathLength, "SINGLE_ROUTE");

            sb.append("STATUS: ONLY 1 OPERATIONAL RAILWAY ROUTE AVAILABLE IN PRACTICE\n");
            sb.append("Notice: All passenger trains serving this corridor follow this single dedicated route.\n\n");
            appendCorridorDetails(sb, "1. DEDICATED OPERATIONAL ROUTE:", single, "");
            sb.append("========================================================================================\n");
            sb.append("Observation: Zero alternative detour routes exist in real operational passenger schedules.\n");
            sb.append("OpenGL Telemetry: Single dedicated route exported to 'data/active_route.txt'.\n");
            sb.append("========================================================================================\n");
        } else {
            int[] distHolder = new int[1];
            int[] dijkstraPath = dijkstraShortestPath(startIdx, endIdx, distHolder);
            if (dijkstraPath != null && distHolder[0] != -1) {
                exportActiveRouteToFileWithMode(dijkstraPath, dijkstraPath.length,
                                                dijkstraPath, dijkstraPath.length, "SINGLE_ROUTE");

                double rate = (travelClass != null && travelClass.contains("CC")) ? 3.20 : (travelClass != null && travelClass.contains("3A") ? 4.50 : 1.80);
                double calcFare = (distHolder[0] * rate) + 40.0;
                double travelTime = computePathTime(dijkstraPath, dijkstraPath.length);

                sb.append("STATUS: 1 INTERCHANGE ROUTE AVAILABLE (NO DIRECT SINGLE TRAIN)\n");
                sb.append("Notice: No direct scheduled train runs this entire pair. Showing connecting track path:\n\n");
                sb.append("1. CONNECTING TRACK NETWORK PATH:\n");
                sb.append("   Path: ").append(formatDetailedPath(dijkstraPath, dijkstraPath.length)).append("\n");
                sb.append(String.format("   Distance:     %d km (%d meters)\n", distHolder[0], distHolder[0] * 1000));
                sb.append(String.format("   Travel Time:  %.1f hours\n", travelTime));
                sb.append(String.format("   Estimated Fare: Rs. %.2f\n", calcFare));
                sb.append("\n========================================================================================\n");
            } else {
                sb.append("STATUS: NO CONNECTING RAILWAY TRACK ROUTE LOCATED BETWEEN THESE STATIONS.\n");
            }
        }

        return sb.toString();
    }

    private RouteDetail buildRouteDetail(int[] path, int len, int totalDist, double travelTime, double maxElev) {
        RouteDetail rd = new RouteDetail();
        rd.pathLength = len;
        rd.pathIndices = new int[len];
        System.arraycopy(path, 0, rd.pathIndices, 0, len);
        rd.totalDistanceKm = totalDist;
        rd.travelTimeHr = travelTime;
        rd.maxElevationMeters = maxElev;
        if (len > 1) {
            rd.legDistancesKm = new int[len - 1];
            for (int i = 0; i < len - 1; i++) {
                rd.legDistancesKm[i] = getDirectDistance(rd.pathIndices[i], rd.pathIndices[i + 1]);
            }
        } else {
            rd.legDistancesKm = new int[0];
        }
        return rd;
    }

    public RouteDetail getRouteDetail(String fromInput, String toInput, boolean shortest) {
        OperationalCorridor[] corridors = findOperationalRoutesBetween(fromInput, toInput, "SL");
        if (corridors != null && corridors.length > 0) {
            OperationalCorridor selected = shortest ? corridors[0] : (corridors.length >= 2 ? corridors[corridors.length - 1] : corridors[0]);
            return buildRouteDetail(selected.pathIndices, selected.pathLength, selected.distanceKm, selected.travelTimeHr, selected.maxElevationMeters);
        }

        int startIdx = getStationIndex(fromInput);
        int endIdx = getStationIndex(toInput);
        if (startIdx == -1 || endIdx == -1 || startIdx == endIdx) return null;

        int[] distHolder = new int[1];
        int[] path = dijkstraShortestPath(startIdx, endIdx, distHolder);
        if (path == null || distHolder[0] == -1) return null;

        return buildRouteDetail(path, path.length, distHolder[0], computePathTime(path, path.length), getMaxElevation(path, path.length));
    }

    private static final String OPENGL_BANNER =
            "========================================================================================\n" +
            "        OPENGL 2D MAP & ANIMATED TRAIN ACTIVE ROUTE SYNCHRONIZED                        \n" +
            "========================================================================================\n";

    public String exportSelectedRouteForOpenGL(String from, String to, String travelClass, boolean useLongest) {
        int startIdx = getStationIndex(from);
        int endIdx = getStationIndex(to);
        if (startIdx == -1 || endIdx == -1) {
            return "Error: Could not identify valid stations for export.";
        }
        if (startIdx == endIdx) {
            return "Error: Origin and destination stations cannot be identical.";
        }

        OperationalCorridor[] corridors = findOperationalRoutesBetween(from, to, travelClass);
        if (corridors != null && corridors.length >= 2) {
            OperationalCorridor shortest = corridors[0];
            OperationalCorridor longest = corridors[corridors.length - 1];
            exportActiveRouteToFileWithMode(shortest.pathIndices, shortest.pathLength,
                                            longest.pathIndices, longest.pathLength,
                                            useLongest ? "LONGEST" : "SHORTEST");

            OperationalCorridor active = useLongest ? longest : shortest;
            StringBuilder sb = new StringBuilder();
            sb.append(OPENGL_BANNER);
            sb.append("Active Display Mode: ").append(useLongest ? "LONGEST OPERATIONAL ROUTE" : "SHORTEST OPERATIONAL ROUTE").append("\n");
            sb.append("Corridor Sequence:   ").append(active.pathString).append("\n");
            sb.append(String.format("Total Path Distance: %d km (%d meters)\n", active.distanceKm, active.distanceKm * 1000));
            sb.append(String.format("Travel Time:         %.1f hours\n", active.travelTimeHr));
            sb.append("Operating Train:     ").append(active.trainNumbers[0]).append(" ").append(active.trainNames[0]).append("\n");
            sb.append("Export Target:       Written to 'data/active_route.txt'\n");
            sb.append("Key Controls:        Press [1] in OpenGL for Shortest, [2] for Longest\n");
            sb.append("========================================================================================\n");
            return sb.toString();
        } else if (corridors != null && corridors.length == 1) {
            OperationalCorridor single = corridors[0];
            exportActiveRouteToFileWithMode(single.pathIndices, single.pathLength,
                                            single.pathIndices, single.pathLength, "SINGLE_ROUTE");

            StringBuilder sb = new StringBuilder();
            sb.append(OPENGL_BANNER);
            sb.append("Active Display Mode: SINGLE DEDICATED OPERATIONAL ROUTE\n");
            sb.append("Corridor Sequence:   ").append(single.pathString).append("\n");
            sb.append(String.format("Total Path Distance: %d km (%d meters)\n", single.distanceKm, single.distanceKm * 1000));
            sb.append("Operating Train:     ").append(single.trainNumbers[0]).append(" ").append(single.trainNames[0]).append("\n");
            sb.append("Export Target:       Written to 'data/active_route.txt'\n");
            sb.append("========================================================================================\n");
            return sb.toString();
        } else {
            int[] distHolder = new int[1];
            int[] dijkstraPath = dijkstraShortestPath(startIdx, endIdx, distHolder);
            if (dijkstraPath != null && distHolder[0] != -1) {
                exportActiveRouteToFileWithMode(dijkstraPath, dijkstraPath.length,
                                                dijkstraPath, dijkstraPath.length, "SINGLE_ROUTE");
                return "SUCCESS: Interchange network path written to 'data/active_route.txt'.";
            }
            return "Error: No track path found between selected stations (stations are disconnected).";
        }
    }

    private void writeRouteToPrintWriter(PrintWriter pw, String label, int[] path, int len) {
        pw.println(label + " " + len);
        int cumDist = 0;
        for (int i = 0; i < len; i++) {
            int idx = path[i];
            if (i > 0) {
                cumDist += getDirectDistance(path[i - 1], idx);
            }
            pw.println(openglX[idx] + " " + openglY[idx] + " " + irctcCodes[idx] + " " + cumDist);
        }
    }

    private void exportActiveRouteToFileWithMode(int[] shortestPath, int sLen, int[] longestPath, int lLen, String activeMode) {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            File activeFile = new File(dataDir, "active_route.txt");
            try (PrintWriter pw = new PrintWriter(new FileWriter(activeFile))) {
                pw.println("ACTIVE_MODE " + activeMode);
                writeRouteToPrintWriter(pw, "SHORTEST_ROUTE", shortestPath, sLen);
                writeRouteToPrintWriter(pw, "LONGEST_ROUTE", longestPath, lLen);
                writeRouteToPrintWriter(pw, "SINGLE_ROUTE", shortestPath, sLen);
            }
            File buildOutData = new File("build/output/data");
            if (buildOutData.getParentFile() != null && buildOutData.getParentFile().exists()) {
                if (!buildOutData.exists()) {
                    buildOutData.mkdirs();
                }
                try (PrintWriter pw2 = new PrintWriter(new FileWriter(new File(buildOutData, "active_route.txt")))) {
                    pw2.println("ACTIVE_MODE " + activeMode);
                    writeRouteToPrintWriter(pw2, "SHORTEST_ROUTE", shortestPath, sLen);
                    writeRouteToPrintWriter(pw2, "LONGEST_ROUTE", longestPath, lLen);
                    writeRouteToPrintWriter(pw2, "SINGLE_ROUTE", shortestPath, sLen);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] dijkstraShortestPath(int src, int dest, int[] distHolder) {
        int[] dist = new int[stationCount];
        int[] prev = new int[stationCount];
        boolean[] visited = new boolean[stationCount];

        for (int i = 0; i < stationCount; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }

        dist[src] = 0;

        for (int i = 0; i < stationCount; i++) {
            int u = -1;
            int minDist = Integer.MAX_VALUE;
            for (int j = 0; j < stationCount; j++) {
                if (!visited[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    u = j;
                }
            }

            if (u == -1 || u == dest) break;
            visited[u] = true;

            EdgeNode curr = adjacencyList[u];
            while (curr != null) {
                int v = curr.toIndex;
                if (!visited[v] && dist[u] != Integer.MAX_VALUE && dist[u] + curr.distanceKm < dist[v]) {
                    dist[v] = dist[u] + curr.distanceKm;
                    prev[v] = u;
                }
                curr = curr.next;
            }
        }

        if (dist[dest] == Integer.MAX_VALUE) {
            distHolder[0] = -1;
            return null;
        }

        distHolder[0] = dist[dest];

        int count = 0;
        int curr = dest;
        while (curr != -1) {
            count++;
            curr = prev[curr];
        }

        int[] path = new int[count];
        curr = dest;
        for (int i = count - 1; i >= 0; i--) {
            path[i] = curr;
            curr = prev[curr];
        }
        return path;
    }

    private int getDirectDistance(int u, int v) {
        if (u == v) return 0;
        EdgeNode curr = adjacencyList[u];
        while (curr != null) {
            if (curr.toIndex == v) {
                return curr.distanceKm;
            }
            curr = curr.next;
        }
        int dx = (int)(Math.abs(openglX[u] - openglX[v]) * 400.0);
        int dy = (int)(Math.abs(openglY[u] - openglY[v]) * 400.0);
        int est = (int) Math.sqrt(dx * dx + dy * dy);
        return (est > 0) ? est : 40;
    }

    private double computePathTime(int[] path, int len) {
        double total = 0.0;
        for (int i = 0; i < len - 1; i++) {
            int u = path[i];
            int v = path[i + 1];
            EdgeNode curr = adjacencyList[u];
            boolean found = false;
            while (curr != null) {
                if (curr.toIndex == v) {
                    total += curr.travelTimeHr;
                    found = true;
                    break;
                }
                curr = curr.next;
            }
            if (!found) {
                total += (getDirectDistance(u, v) / 75.0);
            }
        }
        return total;
    }

    private double getMaxElevation(int[] path, int len) {
        double maxElev = 0.0;
        for (int i = 0; i < len; i++) {
            if (elevations[path[i]] > maxElev) {
                maxElev = elevations[path[i]];
            }
        }
        return maxElev;
    }

    private String formatDetailedPath(int[] path, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(irctcCodes[path[i]]);
            if (i < len - 1) {
                int d = getDirectDistance(path[i], path[i + 1]);
                sb.append(" --(").append(d).append("km)--> ");
            }
        }
        return sb.toString();
    }

    public int getStationCount() {
        return stationCount;
    }

    public String getStationCode(int index) {
        if (index >= 0 && index < stationCount) return irctcCodes[index];
        return "";
    }

    public String getStationName(int index) {
        if (index >= 0 && index < stationCount) return stationNames[index];
        return "";
    }

    public String getStationId(int index) {
        if (index >= 0 && index < stationCount) return stationIds[index];
        return "";
    }

    public double getStationElevation(int index) {
        if (index >= 0 && index < stationCount) return elevations[index];
        return 0.0;
    }

    public double getStationOpenglX(int index) {
        if (index >= 0 && index < stationCount) return openglX[index];
        return 0.0;
    }

    public double getStationOpenglY(int index) {
        if (index >= 0 && index < stationCount) return openglY[index];
        return 0.0;
    }

    public String getDistanceMatrixAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("         CO2: 32x32 MAHARASHTRA DIRECT TRACK DISTANCE ADJACENCY MATRIX (IN KM)          \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("%-6s", "STN"));
        for (int j = 0; j < stationCount; j++) {
            sb.append(String.format(" | %-4s", irctcCodes[j]));
        }
        sb.append("\n----------------------------------------------------------------------------------------\n");

        for (int i = 0; i < stationCount; i++) {
            sb.append(String.format("%-6s", irctcCodes[i]));
            for (int j = 0; j < stationCount; j++) {
                int dist = NO_DIRECT_ROUTE;
                if (i == j) {
                    dist = 0;
                } else {
                    EdgeNode curr = adjacencyList[i];
                    while (curr != null) {
                        if (curr.toIndex == j) {
                            dist = curr.distanceKm;
                            break;
                        }
                        curr = curr.next;
                    }
                }
                if (i == j) {
                    sb.append(" |    0");
                } else if (dist == NO_DIRECT_ROUTE) {
                    sb.append(" |   --");
                } else {
                    sb.append(String.format(" | %4d", dist));
                }
            }
            sb.append("\n");
        }
        sb.append("========================================================================================\n");
        return sb.toString();
    }
}
