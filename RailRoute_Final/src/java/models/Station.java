package models;

public class Station {
    private static int idCounter = 0;
    private String stationId;
    private String irctcCode;
    private String stationName;
    private String city;
    private double latitude;
    private double longitude;
    private double elevation;
    private double openglX;
    private double openglY;

    public Station(String stationName, String city, double elevation) {
        this.stationId = String.format("MH-%03d", ++idCounter);
        this.stationName = stationName;
        this.city = city;
        this.elevation = elevation;
        this.irctcCode = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.openglX = 0.0;
        this.openglY = 0.0;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getIrctcCode() {
        return irctcCode;
    }

    public void setIrctcCode(String irctcCode) {
        this.irctcCode = irctcCode;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getOpenglX() {
        return openglX;
    }

    public void setOpenglX(double openglX) {
        this.openglX = openglX;
    }

    public double getOpenglY() {
        return openglY;
    }

    public void setOpenglY(double openglY) {
        this.openglY = openglY;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    @Override
    public String toString() {
        return "Station[" + stationId + ", " + irctcCode + ", " + stationName + ", " + city + ", " + elevation + "m, (" + openglX + ", " + openglY + ")]";
    }
}
