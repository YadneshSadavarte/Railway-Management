import models.*;

/**
 * Main execution class for the RailRoute Railway Reservation & Intelligent Route Visualizer.
 * 
 * This class serves as the entry point for the Review 1 submission. It sets up 
 * static baseline data using basic arrays (complying with the no-collections constraint)
 * and verifies that all entity models (Station, Train, Passenger, Route, Ticket) are 
 * correctly constructed, encapsulated, and accessible.
 * 
 * Designed for 2nd-year B.Tech PSOOP Review 1.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("      RAILROUTE SYSTEM - PHASE 1: CORE ENTITY MODELING     ");
        System.out.println("==========================================================\n");

        // 1. Instantiate Station objects (Predefined Baseline Stations)
        System.out.println("--- 1. Instantiating Stations ---");
        Station pune = new Station("PNE", "Pune Junction", 560);
        Station lonavala = new Station("LNL", "Lonavala", 624);
        Station mumbai = new Station("CSTM", "Mumbai CSMT", 14);

        // Store in a standard array
        Station[] stations = new Station[] { pune, lonavala, mumbai };
        for (Station s : stations) {
            System.out.println(s.toString());
        }
        System.out.println();

        // 2. Instantiate Route objects connecting these stations
        System.out.println("--- 2. Instantiating Routes ---");
        Route route1 = new Route(pune, lonavala, 64.0, 1.25);
        Route route2 = new Route(lonavala, mumbai, 64.0, 1.75);

        // Store in a standard array
        Route[] routes = new Route[] { route1, route2 };
        for (Route r : routes) {
            System.out.println(r.toString());
        }
        System.out.println();

        // 3. Instantiate Train objects
        System.out.println("--- 3. Instantiating Trains ---");
        Train deccanQueen = new Train("12124", "Deccan Queen", "Pune Junction", "Mumbai CSMT", 120);
        Train sinhagadExp = new Train("11010", "Sinhagad Express", "Pune Junction", "Mumbai CSMT", 80);

        // Store in a standard array
        Train[] trains = new Train[] { deccanQueen, sinhagadExp };
        for (Train t : trains) {
            System.out.println(t.toString());
        }
        System.out.println();

        // 4. Instantiate Passenger objects
        System.out.println("--- 4. Instantiating Passengers ---");
        Passenger passenger1 = new Passenger("P101", "Rahul Sharma", 20, "Male");
        Passenger passenger2 = new Passenger("P102", "Priya Patel", 21, "Female");

        Passenger[] passengers = new Passenger[] { passenger1, passenger2 };
        for (Passenger p : passengers) {
            System.out.println(p.toString());
        }
        System.out.println();

        // 5. Instantiate Ticket objects mapping bookings
        System.out.println("--- 5. Instantiating Tickets (Bookings) ---");
        Ticket ticket1 = new Ticket("TKT-0001", passenger1, deccanQueen, 45);
        Ticket ticket2 = new Ticket("TKT-0002", passenger2, sinhagadExp, 12);

        Ticket[] tickets = new Ticket[] { ticket1, ticket2 };
        for (Ticket tk : tickets) {
            System.out.println(tk.toString());
        }
        System.out.println();

        // 6. Demonstrate Getter Methods (Proving Encapsulation works)
        System.out.println("--- 6. Verifying Individual Getter Methods ---");
        System.out.println("Ticket 1 Passenger Name: " + ticket1.getPassenger().getName());
        System.out.println("Ticket 1 Train Number: " + ticket1.getTrain().getTrainNumber());
        System.out.println("Ticket 1 Seat Number: " + ticket1.getSeatNumber());
        System.out.println("Route 1 Distance in Km: " + route1.getDistanceInKm() + " km");
        System.out.println("Station 1 Elevation: " + pune.getElevationInMeters() + " meters");
        System.out.println();

        // 7. Demonstrate Setter Methods & Re-Verify (State Modification)
        System.out.println("--- 7. Verifying Setter Methods (State Changes) ---");
        System.out.println("Initial Available Seats for " + deccanQueen.getName() + ": " + deccanQueen.getAvailableSeats());
        
        // Simulating booking seat 45 by decrementing seats using getter and setter
        int updatedSeats = deccanQueen.getAvailableSeats() - 1;
        deccanQueen.setAvailableSeats(updatedSeats);
        System.out.println("Updated Available Seats for " + deccanQueen.getName() + ": " + deccanQueen.getAvailableSeats());

        // Update passenger age
        System.out.println("Initial Passenger 1 Age: " + passenger1.getAge());
        passenger1.setAge(21); // Birthday simulation
        System.out.println("Updated Passenger 1 Age: " + passenger1.getAge());
        System.out.println();

        System.out.println("==========================================================");
        System.out.println("        END OF PHASE 1 CORE ENTITY MODELING DEMO          ");
        System.out.println("==========================================================");
    }
}
