package DS.data_structures;

public class CancellationStack {
    public static final int MAX_STACK = 25;
    private String[] ticketIds = new String[MAX_STACK];
    private String[] passengerIds = new String[MAX_STACK];
    private String[] trainIds = new String[MAX_STACK];
    private double[] fares = new double[MAX_STACK];
    private int top = -1;

    public boolean isFull() {
        return top == MAX_STACK - 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int getSize() {
        return top + 1;
    }

    public String[] peek() {
        if (isEmpty()) {
            return null;
        }
        return new String[]{ticketIds[top], passengerIds[top], trainIds[top], String.valueOf(fares[top])};
    }

    public void push(String ticketId, String passengerId, String trainId, double fare) {
        if (isFull()) {
            for (int i = 0; i < MAX_STACK - 1; i++) {
                ticketIds[i] = ticketIds[i + 1];
                passengerIds[i] = passengerIds[i + 1];
                trainIds[i] = trainIds[i + 1];
                fares[i] = fares[i + 1];
            }
            top--;
        }
        top++;
        ticketIds[top] = ticketId;
        passengerIds[top] = passengerId;
        trainIds[top] = trainId;
        fares[top] = fare;
    }

    public String[] pop() {
        if (isEmpty()) {
            return null;
        }
        String[] item = new String[]{ticketIds[top], passengerIds[top], trainIds[top], String.valueOf(fares[top])};
        top--;
        return item;
    }

    public String displayStackAsString() {
        if (isEmpty()) {
            return "Cancellation LIFO Stack is currently empty.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("            CO4: LIFO CANCELLATION REFUND BUFFER STACK (CAPACITY 25)                    \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("Current Stack Size: %d / %d  |  Top Pointer Index: %d\n", getSize(), MAX_STACK, top));
        sb.append(String.format("%-6s | %-12s | %-12s | %-10s | %-9s\n", "Pos", "Ticket ID", "Passenger ID", "Train ID", "Fare"));
        sb.append("----------------------------------------------------------------------------------------\n");

        for (int i = top; i >= 0; i--) {
            String marker = (i == top) ? "<- TOP" : "";
            sb.append(String.format("[%02d]   | %-12s | %-12s | %-10s | Rs. %6.2f %s\n",
                    i, ticketIds[i], passengerIds[i], trainIds[i], fares[i], marker));
        }
        sb.append("========================================================================================\n");
        return sb.toString();
    }
}
