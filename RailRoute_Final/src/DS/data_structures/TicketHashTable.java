package DS.data_structures;

public class TicketHashTable {
    private static final int TABLE_SIZE = 13;

    private static class HashNode {
        String ticketId;
        String summary;
        HashNode next;

        HashNode(String ticketId, String summary, HashNode next) {
            this.ticketId = ticketId;
            this.summary = summary;
            this.next = next;
        }
    }

    private HashNode[] buckets = new HashNode[TABLE_SIZE];

    private int hash(String key) {
        if (key == null) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum = (sum * 31 + key.charAt(i));
        }
        return Math.abs(sum) % TABLE_SIZE;
    }

    public void put(String ticketId, String summary) {
        if (ticketId == null || summary == null) {
            return;
        }
        String cleanId = ticketId.trim();
        int b = hash(cleanId);
        HashNode curr = buckets[b];
        while (curr != null) {
            if (curr.ticketId.equalsIgnoreCase(cleanId)) {
                curr.summary = summary;
                return;
            }
            curr = curr.next;
        }
        buckets[b] = new HashNode(cleanId, summary, buckets[b]);
    }

    public String get(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            return "Please enter a valid Ticket ID.";
        }
        String cleanId = ticketId.trim();
        int b = hash(cleanId);
        HashNode curr = buckets[b];
        int depth = 0;
        while (curr != null) {
            depth++;
            if (curr.ticketId.equalsIgnoreCase(cleanId)) {
                return "O(1) HASH TABLE LOOKUP SUCCESS:\n"
                        + " - Hash Bucket:      [" + b + "] of " + TABLE_SIZE + "\n"
                        + " - Collision Depth:  Node #" + depth + " in chain\n"
                        + " - Ticket ID:        " + curr.ticketId + "\n"
                        + " - Summary:          " + curr.summary;
            }
            curr = curr.next;
        }
        return "Ticket ID '" + cleanId + "' not found in Hash Table (inspected bucket [" + b + "]).";
    }

    public void remove(String ticketId) {
        if (ticketId == null) {
            return;
        }
        String cleanId = ticketId.trim();
        int b = hash(cleanId);
        HashNode curr = buckets[b];
        HashNode prev = null;
        while (curr != null) {
            if (curr.ticketId.equalsIgnoreCase(cleanId)) {
                if (prev == null) {
                    buckets[b] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }

    public String displayHashTableAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("          CO6: TICKET HASH TABLE (SEPARATE CHAINING, TABLE SIZE 13)                    \n");
        sb.append("========================================================================================\n");

        int totalEntries = 0;
        for (int i = 0; i < TABLE_SIZE; i++) {
            sb.append(String.format("Bucket [%02d]: ", i));
            HashNode curr = buckets[i];
            if (curr == null) {
                sb.append("EMPTY\n");
            } else {
                while (curr != null) {
                    totalEntries++;
                    sb.append("[").append(curr.ticketId).append("] -> ");
                    curr = curr.next;
                }
                sb.append("NULL\n");
            }
        }
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("Total Stored Tickets: %d | Prime Table Modulo: %d | Load Factor: %.2f\n",
                totalEntries, TABLE_SIZE, (double) totalEntries / TABLE_SIZE));
        sb.append("========================================================================================\n");
        return sb.toString();
    }
}
