package models;

public class Route {

    private String routeId;
    private String sourceStationId;
    private String destinationStationId;
    private double distance;
    private double travelTime;

    public Route(String routeId, String sourceStationId, String destinationStationId,
                 double distance, double travelTime) {
        this.routeId = routeId;
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.distance = distance;
        this.travelTime = travelTime;
    }

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