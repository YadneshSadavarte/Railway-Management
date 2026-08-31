package models;

/**
 * Represents a connection or track path between two stations within the RailRoute system.
 * This class links a source station to a destination station, capturing the 
 * physical distance (in kilometers) and the travel duration (in hours).
 *
 * Designed for 2nd-year B.Tech PSOOP Review 1.
 */
public class Route {
    // Private instance variables for encapsulation
    private Station sourceStation;
    private Station destinationStation;
    private double distanceInKm;
    private double travelTimeInHours;

    /**
     * Default constructor initializing fields to default values.
     */
    public Route() {
        this.sourceStation = new Station();
        this.destinationStation = new Station();
        this.distanceInKm = 0.0;
        this.travelTimeInHours = 0.0;
    }

    /**
     * Parameterized constructor to initialize a Route with specific values.
     *
     * @param sourceStation      Station object representing the starting station
     * @param destinationStation Station object representing the ending station
     * @param distanceInKm       Distance between the stations in kilometers
     * @param travelTimeInHours  Estimated travel time in hours
     */
    public Route(Station sourceStation, Station destinationStation, double distanceInKm, double travelTimeInHours) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.distanceInKm = distanceInKm;
        this.travelTimeInHours = travelTimeInHours;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public Station getSourceStation() {
        return sourceStation;
    }

    public void setSourceStation(Station sourceStation) {
        this.sourceStation = sourceStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(Station destinationStation) {
        this.destinationStation = destinationStation;
    }

    public double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public double getTravelTimeInHours() {
        return travelTimeInHours;
    }

    public void setTravelTimeInHours(double travelTimeInHours) {
        this.travelTimeInHours = travelTimeInHours;
    }

    /**
     * Overrides the default toString() method to provide a clean, human-readable
     * representation of the Route object.
     *
     * @return String representation of Route
     */
    @Override
    public String toString() {
        return "Route [From: " + (sourceStation != null ? sourceStation.getName() : "None") + 
               " -> To: " + (destinationStation != null ? destinationStation.getName() : "None") + 
               ", Distance: " + distanceInKm + " km, Duration: " + travelTimeInHours + " hrs]";
    }
}
