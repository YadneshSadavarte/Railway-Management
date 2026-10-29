package models;

public class Train implements Bookable {
    private static int idCounter = 100;
    private String trainId;
    private String trainNumber;
    private String trainName;
    private String sourceStationId;
    private String destinationStationId;
    private int totalSeats;
    private int availableSeats;
    private double departureTime;
    private double baseFare;
    private String routeStationIds;
    private String routeStationCodes;
    private String cumulativeDistancesKm;
    private String cumulativeTimesHr;

    public Train(String trainName, String sourceStationId, String destinationStationId) {
        this.trainId = "TRN" + (++idCounter);
        this.trainNumber = String.valueOf(10000 + idCounter);
        this.trainName = trainName;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.totalSeats = 5;
        this.availableSeats = 5;
        this.departureTime = 8.00;
        this.baseFare = 1.80;
        this.routeStationIds = sourceStationId + "|" + destinationStationId;
        this.routeStationCodes = "";
        this.cumulativeDistancesKm = "0|100";
        this.cumulativeTimesHr = "0.0|2.0";
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    @Override
    public boolean isAvailable() {
        return availableSeats > 0;
    }

    @Override
    public void reserve() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }

    public void releaseSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
        }
    }

    @Override
    public String getBookableId() {
        return trainId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getSourceStationId() {
        return sourceStationId;
    }

    public void setSourceStationId(String sourceStationId) {
        this.sourceStationId = sourceStationId;
    }

    public String getDestinationStationId() {
        return destinationStationId;
    }

    public void setDestinationStationId(String destinationStationId) {
        this.destinationStationId = destinationStationId;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public String getRouteStationIds() {
        return routeStationIds;
    }

    public void setRouteStationIds(String routeStationIds) {
        this.routeStationIds = routeStationIds;
    }

    public String getRouteStationCodes() {
        return routeStationCodes;
    }

    public void setRouteStationCodes(String routeStationCodes) {
        this.routeStationCodes = routeStationCodes;
    }

    public String getCumulativeDistancesKm() {
        return cumulativeDistancesKm;
    }

    public void setCumulativeDistancesKm(String cumulativeDistancesKm) {
        this.cumulativeDistancesKm = cumulativeDistancesKm;
    }

    public String getCumulativeTimesHr() {
        return cumulativeTimesHr;
    }

    public void setCumulativeTimesHr(String cumulativeTimesHr) {
        this.cumulativeTimesHr = cumulativeTimesHr;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    @Override
    public String toString() {
        return trainId + " - " + trainNumber + " " + trainName + " (" + sourceStationId + " -> " + destinationStationId + ", Seats: " + availableSeats + "/" + totalSeats + ", Rate: Rs." + baseFare + "/km)";
    }
}
