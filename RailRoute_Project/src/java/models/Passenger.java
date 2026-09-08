package models;

/**
 * Represents a passenger using the RailRoute system.
 * Part of RailRoute PR 1/2/3 - Core Entity Modeling (Review 1).
 */
public class Passenger {

    // Instance variables
    private String passengerId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;

    // Constructor
    public Passenger(String passengerId, String name, int age, String gender, String contactNumber) {
        this.passengerId = passengerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
    }

    // Getters and Setters
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // For quick console testing/debugging in Review 1
    @Override
    public String toString() {
        return "Passenger{" +
                "passengerId='" + passengerId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}