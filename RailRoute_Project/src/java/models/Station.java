package models;

public class Station {

    private String stationId;
    private String stationName;
    private String city;
    private double elevation;

    public Station(String stationId, String stationName, String city, double elevation) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.city = city;
        this.elevation = elevation;
    }

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