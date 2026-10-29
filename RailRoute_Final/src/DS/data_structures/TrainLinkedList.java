package DS.data_structures;

public class TrainLinkedList {
    private static class Node {
        TrainRecord data;
        Node next;

        Node(TrainRecord data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head = null;

    public void clear() {
        head = null;
    }

    public void appendRecord(TrainRecord train) {
        if (train == null) {
            return;
        }
        if (findTrainById(train.trainId) != null) {
            return;
        }
        Node newNode = new Node(train);
        if (head == null) {
            head = newNode;
            return;
        }
        Node curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = newNode;
    }

    public String insertTrainFromGUI(String id, String name, String source, String dest, int seats, double depTime, double fare) {
        if (id == null || id.trim().isEmpty()) {
            return "Error: Train ID cannot be blank.";
        }
        String cleanId = id.trim();
        if (findTrainById(cleanId) != null) {
            return "Error: Train ID '" + cleanId + "' already exists in Singly Linked List.";
        }
        TrainRecord record = new TrainRecord(cleanId, name.trim(), source.trim(), dest.trim(), seats, depTime, fare);
        appendRecord(record);
        return "SUCCESS: Train '" + cleanId + "' (" + name.trim() + ") inserted at tail of Singly Linked List.";
    }

    public String displayAllTrainsAsString() {
        if (head == null) {
            return "Train Singly Linked List is currently empty (head -> null).";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   CO3: TRAIN SINGLY LINKED LIST INVENTORY (DYNAMIC CRUD)               \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("%-8s | %-8s | %-20s | %-12s | %-7s | %-9s\n", "Node", "Train ID", "Train Name", "Route", "Seats", "Base Fare"));
        sb.append("----------------------------------------------------------------------------------------\n");

        Node curr = head;
        int nodeIndex = 1;
        while (curr != null) {
            TrainRecord t = curr.data;
            String route = t.sourceStationId + "->" + t.destinationStationId;
            sb.append(String.format("Node #%-3d | %-8s | %-20s | %-12s | %3d/%3d | Rs. %6.2f\n",
                    nodeIndex, t.trainId, t.trainName, route, t.availableSeats, t.totalSeats, t.baseFare));
            curr = curr.next;
            nodeIndex++;
        }
        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append("Visual Traversal: head -> ");
        curr = head;
        while (curr != null) {
            sb.append("[").append(curr.data.trainId).append("] -> ");
            curr = curr.next;
        }
        sb.append("NULL\n");
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    public TrainRecord findTrainById(String id) {
        if (id == null) {
            return null;
        }
        String target = id.trim();
        Node curr = head;
        while (curr != null) {
            if (curr.data != null && curr.data.trainId.equalsIgnoreCase(target)) {
                return curr.data;
            }
            curr = curr.next;
        }
        return null;
    }

    public String searchTrainAsString(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "Please provide a valid Train ID to search.";
        }
        String target = id.trim();
        Node curr = head;
        int pos = 1;
        while (curr != null) {
            if (curr.data != null && curr.data.trainId.equalsIgnoreCase(target)) {
                TrainRecord t = curr.data;
                return "MATCH FOUND AT NODE #" + pos + ":\n"
                        + " - Train ID:        " + t.trainId + "\n"
                        + " - Train Name:      " + t.trainName + "\n"
                        + " - Route:           " + t.sourceStationId + " -> " + t.destinationStationId + "\n"
                        + " - Available Seats: " + t.availableSeats + " / " + t.totalSeats + "\n"
                        + " - Departure Time:  " + t.departureTime + "\n"
                        + " - Base Fare:       Rs. " + t.baseFare;
            }
            curr = curr.next;
            pos++;
        }
        return "Train ID '" + target + "' not found in Singly Linked List.";
    }

    public String updateTrainFromGUI(String id, String newName, String newSource, String newDest, int newSeats, double newFare) {
        TrainRecord t = findTrainById(id);
        if (t == null) {
            return "Error: Train ID '" + id + "' not found. Update aborted.";
        }
        t.trainName = newName.trim();
        t.sourceStationId = newSource.trim();
        t.destinationStationId = newDest.trim();
        t.totalSeats = newSeats;
        if (t.availableSeats > newSeats) {
            t.availableSeats = newSeats;
        }
        t.baseFare = newFare;
        return "SUCCESS: Train '" + id + "' updated in place in Singly Linked List.";
    }

    public String deleteTrainById(String id) {
        if (head == null || id == null) {
            return "Error: Linked list is empty or invalid ID.";
        }
        String target = id.trim();
        if (head.data != null && head.data.trainId.equalsIgnoreCase(target)) {
            head = head.next;
            return "SUCCESS: Train '" + target + "' deleted from head of Singly Linked List.";
        }
        Node curr = head;
        while (curr.next != null) {
            if (curr.next.data != null && curr.next.data.trainId.equalsIgnoreCase(target)) {
                curr.next = curr.next.next;
                return "SUCCESS: Train '" + target + "' deleted from Singly Linked List.";
            }
            curr = curr.next;
        }
        return "Train ID '" + target + "' not found in Singly Linked List. Deletion failed.";
    }
}
