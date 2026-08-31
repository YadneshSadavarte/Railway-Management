package models;

/**
 * Represents a train within the RailRoute system.
 * This class stores essential details about a train service, including its
 * identification number, name, source, destination, and the current number
 * of available seats for reservation.
 *
 * Designed for 2nd-year B.Tech PSOOP Review 1.
 */
public class Train {
    // Private instance variables for encapsulation
    private String trainNumber;
    private String name;
    private String source;
    private String destination;
    private int availableSeats;

    /**
     * Default constructor initializing fields to default values.
     */
    public Train() {
        this.trainNumber = "";
        this.name = "";
        this.source = "";
        this.destination = "";
        this.availableSeats = 0;
    }

    /**
     * Parameterized constructor to initialize a Train with specific values.
     *
     * @param trainNumber    The train service number (String to handle leading zeros or letters)
     * @param name           Name of the train (e.g., "Deccan Queen")
     * @param source         Starting station of the train's route
     * @param destination    Ending station of the train's route
     * @param availableSeats Available seats for passengers
     */
    public Train(String trainNumber, String name, String source, String destination, int availableSeats) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    /**
     * Overrides the default toString() method to provide a clean, human-readable
     * representation of the Train object.
     *
     * @return String representation of Train
     */
    @Override
    public String toString() {
        return "Train [Number: " + trainNumber + ", Name: " + name + 
               ", Route: " + source + " -> " + destination + 
               ", Available Seats: " + availableSeats + "]";
    }
}
