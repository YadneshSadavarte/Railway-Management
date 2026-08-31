package models;

/**
 * Represents a reservation ticket booked by a passenger on a specific train.
 * This class links a passenger and a train, containing details such as the 
 * unique ticket ID and the assigned seat number.
 *
 * Designed for 2nd-year B.Tech PSOOP Review 1.
 */
public class Ticket {
    // Private instance variables for encapsulation
    private String ticketId;
    private Passenger passenger;
    private Train train;
    private int seatNumber;

    /**
     * Default constructor initializing fields to default values.
     */
    public Ticket() {
        this.ticketId = "";
        this.passenger = new Passenger();
        this.train = new Train();
        this.seatNumber = 0;
    }

    /**
     * Parameterized constructor to initialize a Ticket with specific values.
     *
     * @param ticketId   Unique identifier of the ticket booking
     * @param passenger  Passenger object associated with the booking
     * @param train      Train object on which the booking is made
     * @param seatNumber Assigned seat number on the train
     */
    public Ticket(String ticketId, Passenger passenger, Train train, int seatNumber) {
        this.ticketId = ticketId;
        this.passenger = passenger;
        this.train = train;
        this.seatNumber = seatNumber;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Overrides the default toString() method to provide a clean, human-readable
     * representation of the Ticket object.
     *
     * @return String representation of Ticket
     */
    @Override
    public String toString() {
        return "Ticket [ID: " + ticketId + 
               ", Passenger: " + (passenger != null ? passenger.getName() : "None") + 
               ", Train: " + (train != null ? train.getName() : "None") + 
               ", Seat #: " + seatNumber + "]";
    }
}
