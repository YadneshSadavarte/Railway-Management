package models;

/**
 * Represents a railway station within the RailRoute system.
 * This class stores core geographical and administrative information about a station,
 * such as its unique ID, name, and elevation (which will be used for energy score
 * calculations in later phases).
 *
 * Designed for 2nd-year B.Tech PSOOP Review 1.
 */
public class Station {
    // Private instance variables for encapsulation
    private String stationId;
    private String name;
    private int elevationInMeters;

    /**
     * Default constructor initializing fields to default values.
     */
    public Station() {
        this.stationId = "";
        this.name = "";
        this.elevationInMeters = 0;
    }

    /**
     * Parameterized constructor to initialize a Station with specific values.
     *
     * @param stationId         Unique identifier of the station
     * @param name              Name of the railway station
     * @param elevationInMeters Elevation above sea level in meters
     */
    public Station(String stationId, String name, int elevationInMeters) {
        this.stationId = stationId;
        this.name = name;
        this.elevationInMeters = elevationInMeters;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElevationInMeters() {
        return elevationInMeters;
    }

    public void setElevationInMeters(int elevationInMeters) {
        this.elevationInMeters = elevationInMeters;
    }

    /**
     * Overrides the default toString() method to provide a clean, human-readable
     * representation of the Station object.
     *
     * @return String representation of Station
     */
    @Override
    public String toString() {
        return "Station [ID: " + stationId + ", Name: " + name + ", Elevation: " + elevationInMeters + "m]";
    }
}
