package models;

/**
 * Represents a single railway station in the network.
 * Part of RailRoute PR 1/2/3 - Core Entity Modeling (Review 1).
 */
public class Station {

    // Instance variables
    private String stationId;
    private String stationName;
    private String city;
    private double elevation; // in meters, used later by Operator analytics (Energy Score)

    // Constructor
    public Station(String stationId, String stationName, String city, double elevation) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.city = city;
        this.elevation = elevation;
    }

    // Getters and Setters
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    // For quick console testing/debugging in Review 1
    @Override
    public String toString() {
        return "Station{" +
                "stationId='" + stationId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", city='" + city + '\'' +
                ", elevation=" + elevation +
                '}';
    }
}