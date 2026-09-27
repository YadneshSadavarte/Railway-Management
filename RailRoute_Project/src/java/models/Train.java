package models;

public class Train {

    private String trainId;
    private String trainName;
    private String sourceStationId;
    private String destinationStationId;
    private int totalSeats;
    private int availableSeats;
    private double departureTime;

    public Train(String trainId, String trainName, String sourceStationId,
                 String destinationStationId, int totalSeats, double departureTime) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.departureTime = departureTime;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
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

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
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

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", trainName='" + trainName + '\'' +
                ", sourceStationId='" + sourceStationId + '\'' +
                ", destinationStationId='" + destinationStationId + '\'' +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                ", departureTime=" + departureTime +
                '}';
    }
}