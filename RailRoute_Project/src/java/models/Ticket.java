package models;

public class Ticket {

    private String ticketId;
    private String passengerId;
    private String trainId;
    private String seatNumber;
    private String status;
    private double fare;

    public Ticket(String ticketId, String passengerId, String trainId,
                  String seatNumber, String status, double fare) {
        this.ticketId = ticketId;
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.seatNumber = seatNumber;
        this.status = status;
        this.fare = fare;
    }

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