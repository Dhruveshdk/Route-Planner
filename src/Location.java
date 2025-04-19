public class Location {
    private int locationId;
    private String name;
    private double latitude;
    private double longitude;

    public Location(int locationId, String name, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return name + " (ID: " + locationId + ")";
    }
}
