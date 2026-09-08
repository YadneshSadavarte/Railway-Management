import models.Passenger;
import models.Route;
import models.Station;
import models.Ticket;
import models.Train;

/**
 * Entry point for RailRoute (Review 1).
 * Creates one object of each core entity class and prints them
 * to verify the model layer works end-to-end.
 */
public class Main {
    public static void main(String[] args) {

        // Create a couple of stations
        Station pune = new Station("STN001", "Pune Junction", "Pune", 559.0);
        Station mumbai = new Station("STN002", "Mumbai CST", "Mumbai", 8.0);

        // Create a train running between them
        Train train = new Train("TRN101", "Deccan Express",
                pune.getStationId(), mumbai.getStationId(), 100, 6.15);

        // Create a passenger
        Passenger passenger = new Passenger("PSG001", "Yadnesh", 19, "Male", "9876543210");

        // Create a route between the two stations
        Route route = new Route("RT001", pune.getStationId(), mumbai.getStationId(), 192.0, 3.25);

        // Create a ticket linking passenger + train
        Ticket ticket = new Ticket("TKT001", passenger.getPassengerId(),
                train.getTrainId(), "S1-45", "CONFIRMED", 450.0);

        // Print everything to verify the model layer
        System.out.println("----- RailRoute Review 1 : Entity Test -----");
        System.out.println(pune);
        System.out.println(mumbai);
        System.out.println(train);
        System.out.println(passenger);
        System.out.println(route);
        System.out.println(ticket);
    }
}