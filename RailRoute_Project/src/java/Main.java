import models.Operator;
import models.Passenger;
import models.Person;
import models.Route;
import models.Station;
import models.Ticket;
import models.Train;

public class Main {
    public static void main(String[] args) {

        Station pune = new Station("STN001", "Pune Junction", "Pune", 559.0);
        Station mumbai = new Station("STN002", "Mumbai CST", "Mumbai", 8.0);

        Train train = new Train("TRN101", "Deccan Express",
                pune.getStationId(), mumbai.getStationId(), 100, 6.15);

        Passenger passenger = new Passenger("PSG001", "Yadnesh", 19, "Male", "9876543210");

        Route route = new Route("RT001", pune.getStationId(), mumbai.getStationId(), 192.0, 3.25);

        Ticket ticket = new Ticket("TKT001", passenger.getPassengerId(),
                train.getTrainId(), "S1-45", "CONFIRMED", 450.0);

        Operator operator = new Operator("OP001", "Rajesh Sharma", "9123456780", "Route Analytics");

        System.out.println("----- RailRoute Review 1 : Entity Test -----");
        System.out.println(pune);
        System.out.println(mumbai);
        System.out.println(train);
        System.out.println(passenger);
        System.out.println(route);
        System.out.println(ticket);
        System.out.println(operator);

        System.out.println("\n----- RailRoute Review 2 : Polymorphism Demonstration -----");
        Person[] people = { passenger, operator };
        for (Person person : people) {
            System.out.println("Polymorphic call -> getRole(): " + person.getRole()
                    + " | Name: " + person.getName()
                    + " | Person ID: " + person.getPersonId()
                    + " | Contact: " + person.getContactNumber());
        }

        System.out.println("Total Person instances created (Person.getPersonCount()): " + Person.getPersonCount());
    }
}