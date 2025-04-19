public class Route {
    private int startLocationId;
    private int endLocationId;
    private double distance;
    private int travelTime;

    public Route(int startLocationId, int endLocationId, double distance, int travelTime) {
        this.startLocationId = startLocationId;
        this.endLocationId = endLocationId;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public int getStartLocationId() {
        return startLocationId;
    }

    public int getEndLocationId() {
        return endLocationId;
    }

    public double getDistance() {
        return distance;
    }

    public int getTravelTime() {
        return travelTime;
    }

    @Override
    public String toString() {
        return "Route from " + startLocationId + " to " + endLocationId +
                " | Distance: " + distance + " km | Time: " + travelTime + " min";
    }
}
