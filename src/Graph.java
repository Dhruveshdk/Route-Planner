import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Graph {
    private ArrayList<ArrayList<Pair>> graph;
    private int numberOfVertices;

    public Graph() {
        this.numberOfVertices = getMaxLocationId(); // Find highest location_id from DB
        initializeGraph(numberOfVertices);
        populateGraphFromDatabase();
    }

    private void initializeGraph(int v) {
        graph = new ArrayList<>(v + 1);
        for (int i = 0; i <= v; i++) {  // index 0 unused if location IDs start from 1
            graph.add(new ArrayList<>());
        }
        System.out.println("Graph initialized with " + (v + 1) + " vertices.");
    }

    private int getMaxLocationId() {
        int maxId = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("Failed to connect to database when getting max location ID.");
                return 0;
            }

            stmt = conn.prepareStatement("SELECT MAX(location_id) FROM Locations");
            rs = stmt.executeQuery();

            if (rs.next()) {
                maxId = rs.getInt(1);
                System.out.println("Maximum location ID found: " + maxId);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch max location ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in reverse order
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return maxId;
    }

    private void populateGraphFromDatabase() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                System.out.println("Failed to connect to database when populating graph.");
                return;
            }

            stmt = conn.prepareStatement("SELECT start_location, end_location, distance_km FROM Routes");
            rs = stmt.executeQuery();

            int edgeCount = 0;
            while (rs.next()) {
                int start = rs.getInt("start_location");
                int end = rs.getInt("end_location");
                double distanceKm = rs.getDouble("distance_km");

                // Converting to int for graph representation
                // For more precision, we could multiply by 10 or 100 first
                int distance = (int) Math.round(distanceKm);

                // Ensure the start location is within bounds
                if (start <= numberOfVertices && start >= 0) {
                    graph.get(start).add(new Pair(end, distance));
                    edgeCount++;
                } else {
                    System.out.println("Warning: Ignoring route with out-of-bounds start location ID: " + start);
                }
            }
            System.out.println("Graph populated with " + edgeCount + " edges.");

        } catch (SQLException e) {
            System.out.println("Failed to load graph from database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close resources in reverse order
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ArrayList<Pair>> getGraph() {
        return graph;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }
}