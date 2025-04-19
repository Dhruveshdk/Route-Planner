import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    private Map<Integer, Location> locations;

    public ReportGenerator(Map<Integer, Location> locations) {
        this.locations = locations;
    }

    /**
     * Calculates travel information for a path
     * @param path List of location IDs in the path
     * @return TravelInfo object containing distance and time
     */
    public TravelInfo calculateTravelInfo(List<Integer> path) {
        double totalDistance = 0;
        int totalTime = 0;

        if (path.size() < 2) {
            return new TravelInfo(0, 0);
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed in ReportGenerator.");
                return new TravelInfo(0, 0);
            }

            // Calculate for each segment in the path
            for (int i = 0; i < path.size() - 1; i++) {
                int start = path.get(i);
                int end = path.get(i + 1);

                String query = "SELECT distance_km, travel_time_min FROM Routes " +
                        "WHERE start_location = ? AND end_location = ?";

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, start);
                    stmt.setInt(2, end);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            double distance = rs.getDouble("distance_km");
                            int time = rs.getInt("travel_time_min");

                            totalDistance += distance;
                            totalTime += time;
                        } else {
                            System.out.println("Warning: No route data found between " +
                                    start + " and " + end);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating travel info: " + e.getMessage());
        }

        return new TravelInfo(totalDistance, totalTime);
    }

    /**
     * Generates a detailed route report and prints to console
     * @param path List of location IDs in the path
     */
    public void displayRouteReport(List<Integer> path) {
        if (path == null || path.isEmpty()) {
            System.out.println("Cannot generate report: Empty path");
            return;
        }

        TravelInfo info = calculateTravelInfo(path);

        System.out.println("\n============= ROUTE REPORT =============");
        System.out.println("From: " + getLocationName(path.get(0)));
        System.out.println("To: " + getLocationName(path.get(path.size() - 1)));
        System.out.println("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("---------------------------------------");

        System.out.println("Route Details:");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(getLocationName(path.get(i)));
            if (i < path.size() - 1) {
                System.out.print(" → ");
            }
        }
        System.out.println("\n---------------------------------------");

        System.out.println("Total Distance: " + String.format("%.2f", info.distance) + " km");
        System.out.println("Estimated Travel Time: " + formatTime(info.travelTime));

        // Calculate a simple delivery cost based on distance
        double costPerKm = 0.5; // $0.50 per km
        double deliveryCost = info.distance * costPerKm;
        System.out.println("Estimated Delivery Cost: $" + String.format("%.2f", deliveryCost));
        System.out.println("=======================================");
    }

    /**
     * Exports the route report to a text file
     * @param path List of location IDs in the path
     * @param filePath Path to save the report file
     * @return boolean indicating success or failure
     */
    public boolean exportReportToFile(List<Integer> path, String filePath) {
        if (path == null || path.isEmpty()) {
            System.out.println("Cannot export report: Empty path");
            return false;
        }

        TravelInfo info = calculateTravelInfo(path);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("============= ROUTE REPORT =============\n");
            writer.write("From: " + getLocationName(path.get(0)) + "\n");
            writer.write("To: " + getLocationName(path.get(path.size() - 1)) + "\n");
            writer.write("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("---------------------------------------\n");

            writer.write("Route Details:\n");
            for (int i = 0; i < path.size(); i++) {
                writer.write(getLocationName(path.get(i)));
                if (i < path.size() - 1) {
                    writer.write(" → ");
                }
            }
            writer.write("\n---------------------------------------\n");

            writer.write("Total Distance: " + String.format("%.2f", info.distance) + " km\n");
            writer.write("Estimated Travel Time: " + formatTime(info.travelTime) + "\n");

            // Calculate a simple delivery cost based on distance
            double costPerKm = 0.5; // $0.50 per km
            double deliveryCost = info.distance * costPerKm;
            writer.write("Estimated Delivery Cost: $" + String.format("%.2f", deliveryCost) + "\n");
            writer.write("=======================================\n");

            System.out.println("Report successfully exported to: " + filePath);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to export report: " + e.getMessage());
            return false;
        }
    }

    private String getLocationName(int locationId) {
        Location loc = locations.get(locationId);
        return loc != null ? loc.getName() : "Unknown Location(" + locationId + ")";
    }

    private String formatTime(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return (hours > 0 ? hours + " hr " : "") + mins + " min";
    }

    /**
     * Inner class to hold travel information
     */
    public static class TravelInfo {
        public double distance;
        public int travelTime;

        public TravelInfo(double distance, int travelTime) {
            this.distance = distance;
            this.travelTime = travelTime;
        }
    }
}