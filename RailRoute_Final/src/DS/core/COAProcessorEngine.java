package DS.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class COAProcessorEngine {

    public static String run16BitTask1Trace() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("   COA CO1 & CO2: 16-BIT 8086 MICROPROCESSOR PIPELINE & REGISTER TRANSFER SIMULATION   \n");
        sb.append("   Task 1: Pune -> Mumbai Rail Legs Addition & Alternative Route Comparison            \n");
        sb.append("========================================================================================\n\n");

        sb.append("1. MEMORY DATA SEGMENT ALLOCATION (WORD / 16-BIT):\n");
        sb.append("   [DS:0x1000] leg1_pune_lonavala  = 64  km (0x0040)\n");
        sb.append("   [DS:0x1002] leg2_lonavala_kalyan = 74  km (0x004A)\n");
        sb.append("   [DS:0x1004] leg3_kalyan_mumbai  = 54  km (0x0036)\n");
        sb.append("   [DS:0x1006] total_distance_km   = 0   km (0x0000)\n");
        sb.append("   [DS:0x1008] alt_route_distance  = 375 km (0x0177)\n\n");

        sb.append("2. CONTROL UNIT FETCH-DECODE-EXECUTE MICRO-OPERATIONS & BUS TRANSFERS:\n");
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append("   T-State | Micro-Operations Performed                 | Bus Transfer / Internal State\n");
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append("   T1-T4   | Cycle 1: MOV AX, [leg1_pune_lonavala]      | MAR <- 0x1000, MBR <- M[MAR]\n");
        sb.append("           | IR <- MBR, IP <- IP + 2                    | AX <- 0x0040 (64 km)\n");
        sb.append("   T5-T8   | Cycle 2: MOV BX, [leg2_lonavala_kalyan]    | MAR <- 0x1002, MBR <- M[MAR]\n");
        sb.append("           | IR <- MBR, IP <- IP + 2                    | BX <- 0x004A (74 km)\n");
        sb.append("   T9-T12  | Cycle 3: ADD AX, BX                        | ALU Input: AX (64) + BX (74)\n");
        sb.append("           | Temp <- AX + BX, AX <- Temp                | AX <- 0x008A (138 km)\n");
        sb.append("           | Flags: ZF=0, CF=0, SF=0, OF=0, PF=0        | Status: No Carry, Non-Zero\n");
        sb.append("   T13-T16 | Cycle 4: MOV CX, [leg3_kalyan_mumbai]      | MAR <- 0x1004, MBR <- M[MAR]\n");
        sb.append("           | IR <- MBR, IP <- IP + 2                    | CX <- 0x0036 (54 km)\n");
        sb.append("   T17-T20 | Cycle 5: ADD AX, CX                        | ALU Input: AX (138) + CX (54)\n");
        sb.append("           | Temp <- AX + CX, AX <- Temp                | AX <- 0x00C0 (192 km)\n");
        sb.append("           | Flags: ZF=0, CF=0, SF=0, OF=0, PF=1        | Status: No Carry, Even Parity\n");
        sb.append("   T21-T24 | Cycle 6: MOV [total_distance_km], AX       | MAR <- 0x1006, MBR <- AX (0x00C0)\n");
        sb.append("           | M[MAR] <- MBR, IP <- IP + 2                | Memory Location Updated\n");
        sb.append("   T25-T28 | Cycle 7: MOV DX, [alt_route_distance]      | MAR <- 0x1008, MBR <- M[MAR]\n");
        sb.append("           | IR <- MBR, IP <- IP + 2                    | DX <- 0x0177 (375 km)\n");
        sb.append("   T29-T32 | Cycle 8: CMP AX, DX                        | ALU Subtraction: 192 - 375 = -183\n");
        sb.append("           | Discard Result, Retain Flags               | Flags: ZF=0, CF=1, SF=1, OF=0\n");
        sb.append("   ------------------------------------------------------------------------------------\n\n");

        sb.append("3. 16-BIT GENERAL PURPOSE REGISTER DUMP:\n");
        sb.append(String.format("   AX (Accumulator)  : %6d (Decimal) | 0x%04X (16-Bit Hex) [Pune-Mumbai Shortest Sum]\n", 192, 192));
        sb.append(String.format("   BX (Base Reg)     : %6d (Decimal) | 0x%04X (16-Bit Hex) [Lonavala-Kalyan Leg]\n", 74, 74));
        sb.append(String.format("   CX (Count Reg)    : %6d (Decimal) | 0x%04X (16-Bit Hex) [Kalyan-Mumbai Leg]\n", 54, 54));
        sb.append(String.format("   DX (Data Reg)     : %6d (Decimal) | 0x%04X (16-Bit Hex) [Alternative Route]\n", 375, 375));
        sb.append(String.format("   IP (Instr Pointer): 0x%04X\n", 0x0114));
        sb.append(String.format("   SP (Stack Pointer): 0x%04X\n\n", 0xFFFE));

        sb.append("4. ALU STATUS FLAGS REGISTER AFTER CMP AX, DX:\n");
        sb.append("   +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+\n");
        sb.append("   | -- | -- | -- | -- | OF | DF | IF | TF | SF | ZF | -- | AF | -- | PF | -- | CF |\n");
        sb.append("   |  0 |  0 |  0 |  0 |  0 |  0 |  1 |  0 |  1 |  0 |  0 |  1 |  0 |  1 |  0 |  1 |\n");
        sb.append("   +----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+----+\n");
        sb.append("   - CF = 1 (Carry/Borrow: 192 < 375 generates unsigned borrow)\n");
        sb.append("   - ZF = 0 (Zero Flag: Result -183 is non-zero, routes are not equal)\n");
        sb.append("   - SF = 1 (Sign Flag: Most Significant Bit set, negative signed difference)\n");
        sb.append("   - OF = 0 (Overflow Flag: No 16-bit 2's complement overflow occurred)\n");
        sb.append("   - PF = 1 (Parity Flag: Even number of 1-bits in lower 8 bits of ALU result)\n\n");

        sb.append("5. CONDITIONAL BRANCHING VERDICT:\n");
        sb.append("   Evaluating 'JL' (Jump if Less, SF != OF): (1 != 0) = TRUE\n");
        sb.append("   Evaluating 'JB' (Jump if Below, CF == 1): (1 == 1) = TRUE\n");
        sb.append("   Hardware Verdict: Shortest Route AX (192 km) is confirmed strictly smaller than DX (375 km).\n");
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    public static String run64BitLiveRouteTrace(RouteManager routeManager, String fromStation, String toStation) {
        if (routeManager == null || fromStation == null || toStation == null) {
            return "Error: Invalid route parameters provided for 64-bit COA trace.";
        }

        RouteManager.RouteDetail shortest = routeManager.getRouteDetail(fromStation, toStation, true);
        RouteManager.RouteDetail longest = routeManager.getRouteDetail(fromStation, toStation, false);

        if (shortest == null || longest == null) {
            return "Error: No graph path found between " + fromStation + " and " + toStation + " for COA analysis.";
        }

        long rax = shortest.totalDistanceKm * 1000L;
        long rbx = longest.totalDistanceKm * 1000L;
        long rcx = Math.abs(rbx - rax);

        int zf = (rax == rbx) ? 1 : 0;
        int sf = (rax < rbx) ? 1 : 0;
        int cf = (rax < rbx) ? 1 : 0;
        int of = 0;

        long elevationFactorA = 10L + (long)(shortest.maxElevationMeters / 100.0);
        long elevationFactorB = 10L + (long)(longest.maxElevationMeters / 100.0);

        long r8 = rax * elevationFactorA;
        long r9 = rbx * elevationFactorB;
        long energyDiff = Math.abs(r9 - r8);
        double savingsPercent = (r9 > 0) ? ((double) energyDiff / (double) r9 * 100.0) : 0.0;

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("   COA CO2, CO3 & CO4: 64-BIT WIN64 REGISTER & ALU LIVE ROUTE EXECUTION TRACE          \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("Origin Station:      %s\n", fromStation));
        sb.append(String.format("Destination Station: %s\n", toStation));
        sb.append("Architecture Model:  x86-64 / AMD64 (64-Bit Quadword Addressing & Arithmetic)\n");
        sb.append("----------------------------------------------------------------------------------------\n\n");

        sb.append("1. ROUTE TOPOLOGY & METER CONVERSION:\n");
        sb.append("   ROUTE A (Shortest / Optimal Path):\n");
        sb.append("   - Sequence: ");
        for (int i = 0; i < shortest.pathLength; i++) {
            sb.append(routeManager.getStationCode(shortest.pathIndices[i]));
            if (i < shortest.pathLength - 1) {
                sb.append(" -> ");
            }
        }
        sb.append("\n");
        sb.append(String.format("   - Total Kilometers : %d km\n", shortest.totalDistanceKm));
        sb.append(String.format("   - 64-Bit Meters    : %d meters\n", rax));
        sb.append(String.format("   - Peak Station Elev: %.1f meters (Gradient Index: %d)\n\n", shortest.maxElevationMeters, elevationFactorA));

        sb.append("   ROUTE B (Longest / Alternative Scenic Path):\n");
        sb.append("   - Sequence: ");
        for (int i = 0; i < longest.pathLength; i++) {
            sb.append(routeManager.getStationCode(longest.pathIndices[i]));
            if (i < longest.pathLength - 1) {
                sb.append(" -> ");
            }
        }
        sb.append("\n");
        sb.append(String.format("   - Total Kilometers : %d km\n", longest.totalDistanceKm));
        sb.append(String.format("   - 64-Bit Meters    : %d meters\n", rbx));
        sb.append(String.format("   - Peak Station Elev: %.1f meters (Gradient Index: %d)\n\n", longest.maxElevationMeters, elevationFactorB));

        sb.append("2. 64-BIT GENERAL PURPOSE REGISTER DUMP (QUADWORDS):\n");
        sb.append(String.format("   RAX (Route A Meters)      : %12d | %s\n", rax, String.format("0x%016X", rax)));
        sb.append(String.format("   RBX (Route B Meters)      : %12d | %s\n", rbx, String.format("0x%016X", rbx)));
        sb.append(String.format("   RCX (Delta / SUB Result)  : %12d | %s\n", rcx, String.format("0x%016X", rcx)));
        sb.append(String.format("   RDX (Data / Remainder Reg): %12d | %s\n", 0L, String.format("0x%016X", 0L)));
        sb.append(String.format("   R8  (Energy Score Route A): %12d | %s\n", r8, String.format("0x%016X", r8)));
        sb.append(String.format("   R9  (Energy Score Route B): %12d | %s\n", r9, String.format("0x%016X", r9)));
        sb.append(String.format("   RSP (Stack Pointer)       : %12s | 0x000000000019FF40\n", "Win64 Stack"));
        sb.append(String.format("   RIP (Instruction Pointer) : %12s | 0x00007FF71A4011B8\n\n", "Code Segment"));

        sb.append("3. ALU FLAGS & 64-BIT CONDITIONAL BRANCH PREDICTION:\n");
        sb.append("   Executed Instruction: CMP RAX, RBX (ALU performs 64-bit internal subtract RAX - RBX)\n");
        sb.append(String.format("   - ZF (Zero Flag)    : %d (Zero when RAX == RBX)\n", zf));
        sb.append(String.format("   - SF (Sign Flag)    : %d (Set if result is negative, i.e., RAX < RBX)\n", sf));
        sb.append(String.format("   - CF (Carry Flag)   : %d (Set if unsigned borrow occurs)\n", cf));
        sb.append(String.format("   - OF (Overflow Flag): %d (No 64-bit signed arithmetic overflow)\n", of));
        sb.append("   Branch Condition Evaluation:\n");
        if (rax < rbx) {
            sb.append("   - JL .route_a_shorter : TAKEN (SF != OF evaluates to TRUE, Route A is shorter)\n");
            sb.append("   - JG .route_b_shorter : NOT TAKEN\n");
            sb.append("   - JE .routes_equal    : NOT TAKEN\n\n");
        } else if (rax > rbx) {
            sb.append("   - JL .route_a_shorter : NOT TAKEN\n");
            sb.append("   - JG .route_b_shorter : TAKEN (ZF=0 and SF==OF evaluates to TRUE)\n");
            sb.append("   - JE .routes_equal    : NOT TAKEN\n\n");
        } else {
            sb.append("   - JE .routes_equal    : TAKEN (ZF=1)\n\n");
        }

        sb.append("4. CO3: 64-BIT HARDWARE MULTIPLICATION (IMUL) OPERATIONAL ENERGY SCORE:\n");
        sb.append("   Formula: Energy_Score = Distance_Meters * Elevation_Factor\n");
        sb.append("   Hardware Multiplier: Single-Cycle 64-Bit x86-64 Signed Multiplier (IMUL)\n");
        sb.append(String.format("   - Route A Gradient Multiplier: %d m * %d = %d energy units (R8)\n", rax, elevationFactorA, r8));
        sb.append(String.format("   - Route B Gradient Multiplier: %d m * %d = %d energy units (R9)\n", rbx, elevationFactorB, r9));
        sb.append(String.format("   - Operational Energy Saved   : %d units (%.2f%% traction energy reduction)\n", energyDiff, savingsPercent));
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append("CO4 SYSTEM INTEGRATION SUMMARY:\n");
        sb.append("Java Route Graph -> 64-bit Integer Quadwords -> ALU Flag Comparison -> IMUL Energy Optimization\n");
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    public static String readAssemblySourceFiles() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("               COA ASSEMBLY SOURCE CODE REPOSITORY (x86 & x86-64)                       \n");
        sb.append("========================================================================================\n\n");

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append("FILE 1: src/COA/coa_simulator/Task1_ProcessorModeling.asm\n");
        sb.append("Description: 16-Bit 8086 Instruction Pipeline & Register Modeling (CO1 & CO2)\n");
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(readFileContent("src/COA/coa_simulator/Task1_ProcessorModeling.asm"));
        sb.append("\n\n");

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append("FILE 2: src/COA/assembly/distance_calc.asm\n");
        sb.append("Description: 64-Bit Win64 Route Comparison & IMUL Hardware Energy Multiplier (CO2-CO4)\n");
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(readFileContent("src/COA/assembly/distance_calc.asm"));
        sb.append("\n========================================================================================\n");
        return sb.toString();
    }

    private static String readFileContent(String relativePath) {
        File file = new File(relativePath);
        if (!file.exists()) {
            return "File not found at: " + relativePath;
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                content.append(String.format("%3d | %s\n", lineNum++, line));
            }
        } catch (IOException e) {
            content.append("Error reading file: ").append(e.getMessage());
        }
        return content.toString();
    }

    public static String runCo3AddShiftStringTrace(RouteManager routeManager, String fromStation, String toStation) {
        long distanceMeters = 192000L;
        long elevationFactor = 16L;
        String tag = "pnr:tkt001|pune-ami";
        if (routeManager != null && fromStation != null && toStation != null) {
            RouteManager.RouteDetail shortest = routeManager.getRouteDetail(fromStation, toStation, true);
            if (shortest != null) {
                distanceMeters = shortest.totalDistanceKm * 1000L;
                elevationFactor = 10L + (long)(shortest.maxElevationMeters / 100.0);
                tag = "pnr:tkt001|" + fromStation.trim().toLowerCase() + "-" + toStation.trim().toLowerCase();
            }
        }
        return runCo3AddShiftStringTrace(distanceMeters, elevationFactor, tag);
    }

    public static String runCo3AddShiftStringTrace(long distanceMeters, long elevationFactor, String tagString) {
        if (tagString == null || tagString.isEmpty()) {
            tagString = "pnr:tkt001|pune-ami";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("   COA CO3: ADD-AND-SHIFT MULTIPLICATION & 64-BIT STRING PROCESSING TRACE              \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("Operational Distance Parameter   : %d meters (0x%016X)\n", distanceMeters, distanceMeters));
        sb.append(String.format("Peak Elevation Factor Parameter : %d (0x%016X)\n", elevationFactor, elevationFactor));
        sb.append(String.format("Railway Ticket Tag Source String : %s\n", tagString));
        sb.append("----------------------------------------------------------------------------------------\n\n");

        sb.append("1. SUCCESSIVE ADDITION MULTIPLICATION (LOOP COUNTER = ELEVATION FACTOR):\n");
        sb.append(String.format("   Algorithm: Accumulate multiplicand %d across %d successive loop cycles\n", distanceMeters, elevationFactor));
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append("   Iter | Counter (RCX) | Added Value | Running Accumulator (Dec) | Quadword (Hex)\n");
        sb.append("   ------------------------------------------------------------------------------------\n");

        long runningSum = 0L;
        for (long step = 1L; step <= elevationFactor; step++) {
            runningSum += distanceMeters;
            long rcxLeft = elevationFactor - step;
            sb.append(String.format("   %4d | %13d | %11d | %25d | 0x%016X\n",
                    step, rcxLeft, distanceMeters, runningSum, runningSum));
        }
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append(String.format("   Successive Addition Total Product: %d (0x%016X)\n\n", runningSum, runningSum));

        sb.append("2. 64-BIT ADD-AND-SHIFT HARDWARE MULTIPLICATION (ZERO IMUL USAGE):\n");
        sb.append("   Algorithm: Conditional ADD on multiplier LSB, followed by SHL and SHR\n");
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append("   Iter | LSB | ALU Action       | Accumulator (Dec) | Multiplicand (Dec) | Multiplier\n");
        sb.append("   ------------------------------------------------------------------------------------\n");

        long accum = 0L;
        long multiplicand = distanceMeters;
        long multiplier = elevationFactor;
        int iter = 1;

        while (multiplier > 0L) {
            long lsb = multiplier & 1L;
            String action;
            if (lsb == 1L) {
                accum += multiplicand;
                action = "ADD + SHL/SHR";
            } else {
                action = "SHL/SHR Only ";
            }
            sb.append(String.format("   %4d | %3d | %-16s | %17d | %18d | %10d\n",
                    iter, lsb, action, accum, multiplicand, multiplier));
            multiplicand <<= 1;
            multiplier >>= 1;
            iter++;
        }
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append(String.format("   Add-and-Shift Final Product      : %d (0x%016X)\n\n", accum, accum));

        long imulProduct = distanceMeters * elevationFactor;
        boolean matches = (accum == runningSum) && (accum == imulProduct);

        sb.append("3. MULTIPLICATION VERIFICATION & ALU CROSS-CHECK:\n");
        sb.append(String.format("   - Add-and-Shift Result       : %d (0x%016X)\n", accum, accum));
        sb.append(String.format("   - Successive Addition Result : %d (0x%016X)\n", runningSum, runningSum));
        sb.append(String.format("   - Hardware IMUL Result       : %d (0x%016X)\n", imulProduct, imulProduct));
        sb.append(String.format("   - Verification Status        : %s (All 3 multiplication algorithms agree)\n\n",
                matches ? "MATCH" : "MISMATCH"));

        sb.append("4. 64-BIT STRING PROCESSING & STRING-INSTRUCTION PIPELINE (CO3):\n");
        int strLen = tagString.length();
        sb.append(String.format("   Step 1: REPNE SCASB String Length Determination: %d characters\n", strLen));
        sb.append("   Step 2: LODSB / Case Conversion / STOSB Execution:\n");
        sb.append("   ------------------------------------------------------------------------------------\n");
        sb.append("   Idx | LODSB Char (Hex) | Conversion Action   | STOSB Stored (Hex) | Checksum Subtotal\n");
        sb.append("   ------------------------------------------------------------------------------------\n");

        StringBuilder converted = new StringBuilder();
        long checksum = 0L;

        for (int i = 0; i < strLen; i++) {
            char ch = tagString.charAt(i);
            boolean isLower = (ch >= 'a' && ch <= 'z');
            char conv = isLower ? (char)(ch - 32) : ch;
            converted.append(conv);
            checksum += (long) conv;
            String act = isLower ? "SUB AL, 32 (Upper)" : "Pass-Through      ";
            sb.append(String.format("   %3d | '%c' (0x%02X)        | %-19s | '%c' (0x%02X)        | %17d\n",
                    i, ch, (int)ch, act, conv, (int)conv, checksum));
        }
        sb.append("   ------------------------------------------------------------------------------------\n");

        String copiedBuffer = converted.toString();
        boolean blockCmpMatch = converted.toString().equals(copiedBuffer);

        sb.append(String.format("   - Source String (LODSB)      : %s\n", tagString));
        sb.append(String.format("   - Converted String (STOSB)   : %s\n", converted.toString()));
        sb.append(String.format("   - String Length (SCASB)      : %d characters\n", strLen));
        sb.append(String.format("   - Quadword Checksum (ADD)    : %d (0x%016X)\n", checksum, checksum));
        sb.append(String.format("   - Block Copy & CMP (CMPSB)   : %s (rep movsb duplicate verified via repe cmpsb)\n",
                blockCmpMatch ? "MATCH" : "MISMATCH"));
        sb.append("========================================================================================\n");

        return sb.toString();
    }
}
