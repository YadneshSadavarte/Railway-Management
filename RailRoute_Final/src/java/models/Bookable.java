package models;

public interface Bookable {
    boolean isAvailable();
    void reserve();
    String getBookableId();
}
