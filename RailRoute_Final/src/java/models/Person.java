package models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    public static List<Person> filterPeople(List<Person> people, Predicate<Person> condition) {
        List<Person> filtered = new ArrayList<>();
        if (people != null && condition != null) {
            for (Person p : people) {
                if (condition.test(p)) {
                    filtered.add(p);
                }
            }
        }
        return filtered;
    }

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
        return "[" + getRole() + "] ID: " + personId + ", Name: " + name + ", Contact: " + contactNumber;
    }
}
