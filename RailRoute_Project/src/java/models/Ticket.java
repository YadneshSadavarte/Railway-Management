package models;

/**
 * Represents a booking ticket linking a Passenger to a Train journey.
 * Part of RailRoute PR 1/2/3 - Core Entity Modeling (Review 1).
 */
public class Ticket {

    // Instance variables
    private String ticketId;
    private String passengerId;
    private String trainId;
    private String seatNumber;
    private String status; // e.g. "CONFIRMED", "WAITING", "CANCELLED"
    private double fare;

    // Constructor
    public Ticket(String ticketId, String passengerId, String trainId,
                  String seatNumber, String status, double fare) {
        this.ticketId = ticketId;
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.seatNumber = seatNumber;
        this.status = status;
        this.fare = fare;
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    // For quick console testing/debugging in Review 1
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", trainId='" + trainId + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", status='" + status + '\'' +
                ", fare=" + fare +
                '}';
    }
}