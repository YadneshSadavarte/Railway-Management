package models;

public class Operator extends Person {
    private static int idCounter = 0;
    private String operatorId;
    private String department;

    public Operator(String name, String contactNumber, String department) {
        super("OP00" + (++idCounter), name, contactNumber);
        this.operatorId = this.personId;
        this.department = department;
    }

    @Override
    public String getRole() {
        return "Operator";
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    @Override
    public String toString() {
        return super.toString() + ", Department: " + department;
    }
}
