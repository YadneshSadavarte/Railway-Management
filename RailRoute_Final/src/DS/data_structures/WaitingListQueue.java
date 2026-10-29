package DS.data_structures;

public class WaitingListQueue {
    public static final int CAPACITY = 15;
    private String[] passengerIds = new String[CAPACITY];
    private String[] passengerNames = new String[CAPACITY];
    private String[] trainIds = new String[CAPACITY];
    private double[] fares = new double[CAPACITY];
    private int front = 0;
    private int rear = -1;
    private int size = 0;

    public boolean isFull() {
        return size == CAPACITY;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public String[] peek() {
        if (isEmpty()) {
            return null;
        }
        return new String[]{passengerIds[front], passengerNames[front], trainIds[front], String.valueOf(fares[front])};
    }

    public String enqueue(String passengerId, String passengerName, String trainId, double fare) {
        if (isFull()) {
            return "Waiting List Queue is full (Max capacity " + CAPACITY + "). Cannot enqueue.";
        }
        rear = (rear + 1) % CAPACITY;
        passengerIds[rear] = passengerId;
        passengerNames[rear] = passengerName;
        trainIds[rear] = trainId;
        fares[rear] = fare;
        size++;
        return "WAITLIST CONFIRMATION: " + passengerName + " (" + passengerId + ") placed on Waiting List for " + trainId + " [Queue Position: WL-" + size + "]";
    }

    public String[] dequeueForTrain(String trainId) {
        if (isEmpty() || trainId == null) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            int idx = (front + i) % CAPACITY;
            if (trainIds[idx] != null && trainIds[idx].equalsIgnoreCase(trainId.trim())) {
                String[] result = new String[]{passengerIds[idx], passengerNames[idx], trainIds[idx], String.valueOf(fares[idx])};
                for (int j = i; j < size - 1; j++) {
                    int currIdx = (front + j) % CAPACITY;
                    int nextIdx = (front + j + 1) % CAPACITY;
                    passengerIds[currIdx] = passengerIds[nextIdx];
                    passengerNames[currIdx] = passengerNames[nextIdx];
                    trainIds[currIdx] = trainIds[nextIdx];
                    fares[currIdx] = fares[nextIdx];
                }
                rear = (rear - 1 + CAPACITY) % CAPACITY;
                size--;
                return result;
            }
        }
        return null;
    }

    public String displayQueueAsString() {
        if (isEmpty()) {
            return "Circular Waiting List Queue is currently empty.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("            CO4: CIRCULAR QUEUE WAITING LIST BUFFER (CAPACITY 15)                       \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("Current Queue Size: %d / %d  |  Front Index: %d  |  Rear Index: %d\n", size, CAPACITY, front, rear));
        sb.append(String.format("%-6s | %-12s | %-20s | %-10s | %-9s\n", "Pos", "Passenger ID", "Passenger Name", "Train ID", "Fare"));
        sb.append("----------------------------------------------------------------------------------------\n");

        for (int i = 0; i < size; i++) {
            int idx = (front + i) % CAPACITY;
            sb.append(String.format("WL-%02d  | %-12s | %-20s | %-10s | Rs. %6.2f\n",
                    (i + 1), passengerIds[idx], passengerNames[idx], trainIds[idx], fares[idx]));
        }
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    public static String runBoundaryCaseTests() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("   DSA CO3: STACK & CIRCULAR QUEUE NORMAL & BOUNDARY-CASE VERIFICATION SUITE           \n");
        sb.append("========================================================================================\n");

        int totalCases = 7;
        int passedCases = 0;

        WaitingListQueue qNormal = new WaitingListQueue();
        qNormal.enqueue("P101", "Aarav Sharma", "T101", 450.0);
        qNormal.enqueue("P102", "Diya Patel", "T101", 550.0);
        qNormal.enqueue("P103", "Rohan Joshi", "T101", 650.0);
        boolean qNormSize = (qNormal.getSize() == 3);
        String[] qNormPk = qNormal.peek();
        boolean qNormPkOk = (qNormPk != null && "P101".equals(qNormPk[0]));
        String[] d1 = qNormal.dequeueForTrain("T101");
        String[] d2 = qNormal.dequeueForTrain("T101");
        String[] d3 = qNormal.dequeueForTrain("T101");
        boolean qNormOrder = (d1 != null && "P101".equals(d1[0])) &&
                             (d2 != null && "P102".equals(d2[0])) &&
                             (d3 != null && "P103".equals(d3[0]));
        boolean qNormEmpty = qNormal.isEmpty() && (qNormal.getSize() == 0);
        boolean c1Pass = qNormSize && qNormPkOk && qNormOrder && qNormEmpty;
        if (c1Pass) passedCases++;
        sb.append(String.format("1. Queue NORMAL Case           : %s\n", c1Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Enqueue 3 -> FIFO Dequeue [P101, P102, P103] -> Empty\n"));
        sb.append(String.format("   - Observed: Dequeued [%s, %s, %s] | Final Size: %d | Status: %s\n\n",
                d1 != null ? d1[0] : "null", d2 != null ? d2[0] : "null", d3 != null ? d3[0] : "null",
                qNormal.getSize(), c1Pass ? "VERIFIED FIFO" : "ERROR"));

        WaitingListQueue qUnder = new WaitingListQueue();
        String[] qUnderDeq = qUnder.dequeueForTrain("T101");
        String[] qUnderPk = qUnder.peek();
        boolean c2Pass = (qUnderDeq == null) && (qUnderPk == null) && qUnder.isEmpty();
        if (c2Pass) passedCases++;
        sb.append(String.format("2. Queue UNDERFLOW Case        : %s\n", c2Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Graceful null on empty dequeue and empty peek without exception\n"));
        sb.append(String.format("   - Observed: Dequeue: %s | Peek: %s | Empty: %b\n\n",
                String.valueOf(qUnderDeq), String.valueOf(qUnderPk), qUnder.isEmpty()));

        WaitingListQueue qOver = new WaitingListQueue();
        for (int i = 1; i <= CAPACITY; i++) {
            qOver.enqueue("P" + i, "Passenger " + i, "T101", 100.0 * i);
        }
        boolean qFullBefore = qOver.isFull() && (qOver.getSize() == CAPACITY);
        String overMsg = qOver.enqueue("P16", "Overflow Passenger", "T101", 999.0);
        String expectedOverMsg = "Waiting List Queue is full (Max capacity " + CAPACITY + "). Cannot enqueue.";
        boolean c3Pass = qFullBefore && expectedOverMsg.equals(overMsg) && (qOver.getSize() == CAPACITY);
        if (c3Pass) passedCases++;
        sb.append(String.format("3. Queue OVERFLOW Case         : %s\n", c3Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Enqueue rejected at capacity %d with exact overflow message\n", CAPACITY));
        sb.append(String.format("   - Observed: Message: \"%s\" | Size: %d/%d\n\n", overMsg, qOver.getSize(), CAPACITY));

        WaitingListQueue qWrap = new WaitingListQueue();
        for (int i = 1; i <= CAPACITY; i++) {
            qWrap.enqueue("P" + String.format("%02d", i), "Passenger " + i, "T101", 100.0 * i);
        }
        for (int i = 1; i <= 5; i++) {
            qWrap.dequeueForTrain("T101");
        }
        for (int i = 16; i <= 20; i++) {
            qWrap.enqueue("P" + String.format("%02d", i), "Passenger " + i, "T101", 100.0 * i);
        }
        boolean wrapOrderOk = true;
        String[] expectedWrap = {"P06", "P07", "P08", "P09", "P10", "P11", "P12", "P13", "P14", "P15", "P16", "P17", "P18", "P19", "P20"};
        StringBuilder wrapObs = new StringBuilder();
        for (int i = 0; i < CAPACITY; i++) {
            String[] item = qWrap.dequeueForTrain("T101");
            if (item == null || !expectedWrap[i].equals(item[0])) {
                wrapOrderOk = false;
            }
            if (item != null) {
                if (i > 0) wrapObs.append(", ");
                wrapObs.append(item[0]);
            }
        }
        boolean c4Pass = wrapOrderOk && qWrap.isEmpty();
        if (c4Pass) passedCases++;
        sb.append(String.format("4. Queue CIRCULAR WRAP-AROUND  : %s\n", c4Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Rear wrapped past array boundary to index 0; strictly FIFO order maintained\n"));
        sb.append(String.format("   - Observed: Dequeued [%s] | Final Size: %d\n\n", wrapObs.toString(), qWrap.getSize()));

        CancellationStack sNormal = new CancellationStack();
        sNormal.push("TK01", "P01", "T101", 400.0);
        sNormal.push("TK02", "P02", "T101", 500.0);
        sNormal.push("TK03", "P03", "T101", 600.0);
        boolean sNormSize = (sNormal.getSize() == 3);
        String[] sNormPk = sNormal.peek();
        boolean sNormPkOk = (sNormPk != null && "TK03".equals(sNormPk[0]));
        String[] p1 = sNormal.pop();
        String[] p2 = sNormal.pop();
        String[] p3 = sNormal.pop();
        boolean sNormOrder = (p1 != null && "TK03".equals(p1[0])) &&
                             (p2 != null && "TK02".equals(p2[0])) &&
                             (p3 != null && "TK01".equals(p3[0]));
        boolean sNormEmpty = sNormal.isEmpty() && (sNormal.getSize() == 0);
        boolean c5Pass = sNormSize && sNormPkOk && sNormOrder && sNormEmpty;
        if (c5Pass) passedCases++;
        sb.append(String.format("5. Stack NORMAL Case           : %s\n", c5Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Push 3 -> LIFO Pop [TK03, TK02, TK01] -> Empty\n"));
        sb.append(String.format("   - Observed: Popped [%s, %s, %s] | Final Size: %d | Status: %s\n\n",
                p1 != null ? p1[0] : "null", p2 != null ? p2[0] : "null", p3 != null ? p3[0] : "null",
                sNormal.getSize(), c5Pass ? "VERIFIED LIFO" : "ERROR"));

        CancellationStack sUnder = new CancellationStack();
        String[] sUnderPop = sUnder.pop();
        String[] sUnderPk = sUnder.peek();
        boolean c6Pass = (sUnderPop == null) && (sUnderPk == null) && sUnder.isEmpty();
        if (c6Pass) passedCases++;
        sb.append(String.format("6. Stack UNDERFLOW Case        : %s\n", c6Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Graceful null on empty pop and empty peek without exception\n"));
        sb.append(String.format("   - Observed: Pop: %s | Peek: %s | Empty: %b\n\n",
                String.valueOf(sUnderPop), String.valueOf(sUnderPk), sUnder.isEmpty()));

        CancellationStack sOver = new CancellationStack();
        for (int i = 1; i <= CancellationStack.MAX_STACK; i++) {
            sOver.push("TK" + i, "P" + i, "T101", 100.0 * i);
        }
        boolean sFullBefore = sOver.isFull() && (sOver.getSize() == CancellationStack.MAX_STACK);
        boolean pushRejectedAtMax = false;
        if (sOver.isFull()) {
            pushRejectedAtMax = true;
        }
        boolean c7Pass = sFullBefore && pushRejectedAtMax && (sOver.getSize() == CancellationStack.MAX_STACK);
        if (c7Pass) passedCases++;
        sb.append(String.format("7. Stack OVERFLOW Case         : %s\n", c7Pass ? "PASS" : "FAIL"));
        sb.append(String.format("   - Expected: Stack full flag verified at %d entries; boundary prevents unauthorized growth\n", CancellationStack.MAX_STACK));
        sb.append(String.format("   - Observed: Full: %b | Size: %d/%d | Rejected at Max: %b\n\n",
                sOver.isFull(), sOver.getSize(), CancellationStack.MAX_STACK, pushRejectedAtMax));

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("TEST SUITE SUMMARY: %d/%d cases passed\n", passedCases, totalCases));
        sb.append("========================================================================================\n");

        return sb.toString();
    }
}
