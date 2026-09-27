package models;

public class Operator extends Person {

    private String operatorId;
    private String department;

    public Operator(String operatorId, String name, String contactNumber, String department) {
        super(operatorId, name, contactNumber);
        this.operatorId = operatorId;
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

    @Override
    public String toString() {
        return "Operator{" +
                "operatorId='" + operatorId + '\'' +
                ", name='" + getName() + '\'' +
                ", contactNumber='" + getContactNumber() + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
