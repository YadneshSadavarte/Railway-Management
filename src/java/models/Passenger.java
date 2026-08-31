package models;

/**
 * Represents a passenger within the RailRoute system.
 * This class stores profile information of passengers who make bookings on trains,
 * including their unique passenger ID, name, age, and gender.
 *
 * Designed for 2nd-year B.Tech PSOOP Review 1.
 */
public class Passenger {
    // Private instance variables for encapsulation
    private String passengerId;
    private String name;
    private int age;
    private String gender;

    /**
     * Default constructor initializing fields to default values.
     */
    public Passenger() {
        this.passengerId = "";
        this.name = "";
        this.age = 0;
        this.gender = "";
    }

    /**
     * Parameterized constructor to initialize a Passenger with specific values.
     *
     * @param passengerId Unique identifier of the passenger
     * @param name        Passenger's name
     * @param age         Passenger's age
     * @param gender      Passenger's gender
     */
    public Passenger(String passengerId, String name, int age, String gender) {
        this.passengerId = passengerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Overrides the default toString() method to provide a clean, human-readable
     * representation of the Passenger object.
     *
     * @return String representation of Passenger
     */
    @Override
    public String toString() {
        return "Passenger [ID: " + passengerId + ", Name: " + name + 
               ", Age: " + age + ", Gender: " + gender + "]";
    }
}
