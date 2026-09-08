package models;

/**
 * Represents a route between two stations, carrying distance and time
 * data used later for Fastest vs. Shortest route comparison.
 * Part of RailRoute PR 1/2/3 - Core Entity Modeling (Review 1).
 */
public class Route {

    // Instance variables
    private String routeId;
    private String sourceStationId;
    private String destinationStationId;
    private double distance;    // in kilometers
    private double travelTime;  // in hours, Review 1 simplicity

    // Constructor
    public Route(String routeId, String sourceStationId, String destinationStationId,
                 double distance, double travelTime) {
        this.routeId = routeId;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    // Getters and Setters
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSourceStationId() {
        return sourceStationId;
    }

    public void setSourceStationId(String sourceStationId) {
        this.sourceStationId = sourceStationId;
    }

    public String getDestinationStationId() {
        return destinationStationId;
    }

    public void setDestinationStationId(String destinationStationId) {
        this.destinationStationId = destinationStationId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    // For quick console testing/debugging in Review 1
    @Override
    public String toString() {
        return "Route{" +
                "routeId='" + routeId + '\'' +
                ", sourceStationId='" + sourceStationId + '\'' +
                ", destinationStationId='" + destinationStationId + '\'' +
                ", distance=" + distance +
                ", travelTime=" + travelTime +
                '}';
    }
}