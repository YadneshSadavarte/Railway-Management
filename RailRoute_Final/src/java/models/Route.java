package models;

public class Route {
    private static int idCounter = 0;
    private String routeId;
    private String sourceStationId;
    private String destinationStationId;
    private double distance;
    private double travelTime;

    public Route(String sourceStationId, String destinationStationId) {
        this.routeId = "RT00" + (++idCounter);
        this.sourceStationId = sourceStationId;
        this.destinationStationId = destinationStationId;
        this.distance = 0.0;
        this.travelTime = 0.0;
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

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    @Override
    public String toString() {
        return "Route[" + routeId + ": " + sourceStationId + " -> " + destinationStationId + ", " + distance + " km, " + travelTime + " hrs]";
    }
}
