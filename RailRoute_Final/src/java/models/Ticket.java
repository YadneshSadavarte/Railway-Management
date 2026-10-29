package models;

public class Ticket {
    private static int idCounter = 0;
    private String ticketId;
    private String passengerId;
    private String trainId;
    private String fromStation;
    private String toStation;
    private String routeChoice;
    private String seatNumber;
    private String status;
    private double fare;

    public Ticket(String passengerId, String trainId, double fare) {
        this.ticketId = String.format("TKT%03d", ++idCounter);
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.fare = fare;
        this.fromStation = "";
        this.toStation = "";
        this.routeChoice = "SHORTEST";
        this.status = "CONFIRMED";
        this.seatNumber = "S1-1";
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

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getRouteChoice() {
        return routeChoice;
    }

    public void setRouteChoice(String routeChoice) {
        this.routeChoice = routeChoice;
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

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    @Override
    public String toString() {
        return "Ticket[" + ticketId + ", Passenger: " + passengerId + ", Train: " + trainId + ", From: " + fromStation + ", To: " + toStation + ", Route: " + routeChoice + ", Seat: " + seatNumber + ", Status: " + status + ", Fare: Rs." + fare + "]";
    }
}
