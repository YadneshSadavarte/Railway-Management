package models;

public class Passenger extends Person {
    private static int idCounter = 0;
    private String passengerId;
    private int age;
    private String gender;

    public Passenger(String name, int age) {
        super("PSG00" + (++idCounter), name, "0000000000");
        this.passengerId = this.personId;
        this.gender = "Not Specified";
        setAge(age);
    }

    public void setAge(int age) {
        try {
            if (age < 1 || age > 120) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
            this.age = age;
        } catch (IllegalArgumentException e) {
            this.age = 0;
        }
    }

    @Override
    public void setContactNumber(String contactNumber) {
        try {
            if (contactNumber == null || !contactNumber.matches("\\d{10}")) {
                throw new IllegalArgumentException("Invalid contact number: " + contactNumber);
            }
            super.setContactNumber(contactNumber);
        } catch (IllegalArgumentException e) {
            super.setContactNumber("0000000000");
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    @Override
    public String toString() {
        return super.toString() + ", Age: " + age + ", Gender: " + gender;
    }
}
