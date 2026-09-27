package models;

public abstract class Person {

    protected final String personId;
    private String name;
    private String contactNumber;
    private static int personCount = 0;

    public Person(String personId, String name, String contactNumber) {
        this.personId = personId;
        this.name = name;
        this.contactNumber = contactNumber;
        personCount++;
    }

    public abstract String getRole();

    public String getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public static int getPersonCount() {
        return personCount;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId='" + personId + '\'' +
                ", name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }
}
