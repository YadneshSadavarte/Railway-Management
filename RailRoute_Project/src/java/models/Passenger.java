package models;

public class Passenger extends Person {

    private String passengerId;
    private int age;
    private String gender;

    public Passenger(String passengerId, String name, int age, String gender, String contactNumber) {
        super(passengerId, name, validateContactNumber(contactNumber));

        try {
            if (age <= 0 || age > 120) {
                throw new IllegalArgumentException("Invalid age: " + age + ". Age must be between 1 and 120.");
            }
            this.age = age;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error in Passenger: " + e.getMessage() + " Defaulting age to 0.");
            this.age = 0;
        }

        this.passengerId = passengerId;
        this.gender = gender;
    }

    private static String validateContactNumber(String contactNumber) {
        try {
            if (contactNumber == null || !contactNumber.matches("\\d{10}")) {
                throw new IllegalArgumentException("Invalid contact number: " + contactNumber + ". Must be a 10-digit number.");
            }
            return contactNumber;
        } catch (IllegalArgumentException e) {
            System.err.println("Validation Error in Passenger: " + e.getMessage() + " Defaulting to \"0000000000\".");
            return "0000000000";
        }
    }

    @Override
    public String getRole() {
        return "Passenger";
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
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

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerId='" + passengerId + '\'' +
                ", name='" + getName() + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + getContactNumber() + '\'' +
                '}';
    }
}