package DS.data_structures;

public class TrainArray {
    public static final int MAX_TRAINS = 60;
    private TrainRecord[] trains = new TrainRecord[MAX_TRAINS];
    private int trainCount = 0;

    public void clear() {
        for (int i = 0; i < trainCount; i++) {
            trains[i] = null;
        }
        trainCount = 0;
    }

    public boolean addRecord(TrainRecord record) {
        if (trainCount >= MAX_TRAINS || record == null) {
            return false;
        }
        trains[trainCount++] = record;
        return true;
    }

    public int getTrainCount() {
        return trainCount;
    }

    public TrainRecord getTrainAt(int index) {
        if (index >= 0 && index < trainCount) {
            return trains[index];
        }
        return null;
    }

    public int linearSearchById(String trainId) {
        if (trainId == null) {
            return -1;
        }
        for (int i = 0; i < trainCount; i++) {
            if (trains[i] != null && trains[i].trainId.equalsIgnoreCase(trainId.trim())) {
                return i;
            }
        }
        return -1;
    }

    public TrainRecord binarySearchById(String trainId) {
        if (trainId == null || trainCount == 0) {
            return null;
        }
        TrainRecord[] sorted = new TrainRecord[trainCount];
        System.arraycopy(trains, 0, sorted, 0, trainCount);

        for (int i = 1; i < trainCount; i++) {
            TrainRecord key = sorted[i];
            int j = i - 1;
            while (j >= 0 && sorted[j].trainId.compareToIgnoreCase(key.trainId) > 0) {
                sorted[j + 1] = sorted[j];
                j--;
            }
            sorted[j + 1] = key;
        }

        int low = 0;
        int high = trainCount - 1;
        String target = trainId.trim();

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = sorted[mid].trainId.compareToIgnoreCase(target);
            if (cmp == 0) {
                return sorted[mid];
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public String getQuickSortedByFareAsString() {
        if (trainCount == 0) {
            return "No train records found in 1D TrainArray.";
        }

        TrainRecord[] temp = new TrainRecord[trainCount];
        for (int i = 0; i < trainCount; i++) {
            temp[i] = trains[i].copy();
        }

        quickSortByFare(temp, 0, trainCount - 1);

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   CO5: QUICKSORT ON 1D ARRAY BY FARE (ASCENDING)                       \n");
        sb.append("========================================================================================\n");
        appendTrainTable(sb, temp);
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    private void quickSortByFare(TrainRecord[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSortByFare(arr, low, pi - 1);
            quickSortByFare(arr, pi + 1, high);
        }
    }

    private int partition(TrainRecord[] arr, int low, int high) {
        double pivot = arr[high].baseFare;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].baseFare <= pivot) {
                i++;
                TrainRecord swap = arr[i];
                arr[i] = arr[j];
                arr[j] = swap;
            }
        }
        TrainRecord swap = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = swap;
        return i + 1;
    }

    public String getAllArrayTrainsAsString() {
        if (trainCount == 0) {
            return "1D TrainArray is currently empty.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   CO1 & CO2: 1D TRAIN ARRAY INVENTORY (FIXED CAPACITY 60)              \n");
        sb.append("========================================================================================\n");
        sb.append(String.format("Total Active Records: %d / %d\n", trainCount, MAX_TRAINS));
        appendTrainTable(sb, trains);
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    private void appendTrainTable(StringBuilder sb, TrainRecord[] arr) {
        sb.append(String.format("%-6s | %-8s | %-24s | %-12s | %-7s | %-9s\n", "Index", "Train ID", "Train Name", "Route", "Seats", "Rate/Fare"));
        sb.append("----------------------------------------------------------------------------------------\n");
        for (int i = 0; i < trainCount; i++) {
            TrainRecord t = arr[i];
            String route = t.sourceStationId + "->" + t.destinationStationId;
            sb.append(String.format("[%02d]   | %-8s | %-24s | %-12s | %3d/%3d | Rs. %6.2f\n",
                    i, t.trainId, t.trainName, route, t.availableSeats, t.totalSeats, t.baseFare));
        }
    }
}
