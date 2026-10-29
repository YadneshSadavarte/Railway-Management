package DS.data_structures;

public class TrainRecord {
    public String trainId;
    public String trainNumber;
    public String trainName;
    public String sourceStationId;
    public String destinationStationId;
    public int totalSeats;
    public int availableSeats;
    public double departureTime;
    public double baseFare;
    public String routeStationIds;
    public String routeStationCodes;
    public String cumulativeDistancesKm;
    public String cumulativeTimesHr;

    public TrainRecord() {
        this("", "", "", "", "", 0, 0, 0.0, 0.0, "", "", "", "");
    }

    public TrainRecord(String trainId, String trainName, String sourceStationId, String destinationStationId, int totalSeats, double departureTime, double baseFare) {
        this(trainId, "10000", trainName, sourceStationId, destinationStationId, totalSeats, totalSeats, departureTime, baseFare, sourceStationId + "|" + destinationStationId, "", "0|100", "0.0|2.0");
    }

    public TrainRecord(String trainId, String trainNumber, String trainName, String sourceStationId, String destinationStationId, int totalSeats, int availableSeats, double departureTime, double baseFare, String routeStationIds, String routeStationCodes, String cumulativeDistancesKm, String cumulativeTimesHr) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.departureTime = departureTime;
        this.baseFare = baseFare;
        this.routeStationIds = routeStationIds;
        this.routeStationCodes = routeStationCodes;
        this.cumulativeDistancesKm = cumulativeDistancesKm;
        this.cumulativeTimesHr = cumulativeTimesHr;
    }

    public TrainRecord copy() {
        return new TrainRecord(this.trainId, this.trainNumber, this.trainName, this.sourceStationId, this.destinationStationId, this.totalSeats, this.availableSeats, this.departureTime, this.baseFare, this.routeStationIds, this.routeStationCodes, this.cumulativeDistancesKm, this.cumulativeTimesHr);
    }

    @Override
    public String toString() {
        return trainId + " - " + trainNumber + " | " + trainName + " | " + sourceStationId + " -> " + destinationStationId + " | Seats: " + availableSeats + "/" + totalSeats + " | Rate: Rs." + baseFare + "/km";
    }
}
