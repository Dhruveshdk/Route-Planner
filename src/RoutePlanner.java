import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class RoutePlanner {

    private static Map<Integer, Location> locations = new HashMap<>();
    private static List<Route> routes = new ArrayList<>();
    private static Graph routeGraph;
    private static ReportGenerator reportGenerator;

    public static void main(String[] args) {
        System.out.println("Loading Route Planner System...");
        loadLocationsFromDB();

        // Use Graph constructor which loads directly from the database
        routeGraph = new Graph();

        // Initialize the report generator
        reportGenerator = new ReportGenerator(locations);

        // Display available locations to help user choose
        displayAvailableLocations();

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("\nEnter source location ID:");
            int source = scanner.nextInt();

            // Validate source ID exists
            if (!locations.containsKey(source)) {
                System.out.println("Source location ID " + source + " does not exist in the database.");
                return;
            }

            System.out.println("Enter destination location ID:");
            int destination = scanner.nextInt();

            // Validate destination ID exists
            if (!locations.containsKey(destination)) {
                System.out.println("Destination location ID " + destination + " does not exist in the database.");
                return;
            }

            System.out.println("\nCalculating optimal route...");

            // Get the graph and vertices count from the Graph class
            ArrayList<ArrayList<Pair>> graph = routeGraph.getGraph();
            int numberOfVertices = routeGraph.getNumberOfVertices();

            DijkstraAlgorithm.Result result = DijkstraAlgorithm.dijkstra(numberOfVertices, source, graph);
            int distance = result.dist[destination];

            if (distance == Integer.MAX_VALUE) {
                System.out.println("No path found between the given locations.");
            } else {
                System.out.println("\n--- ROUTE FOUND ---");
                System.out.println("Shortest distance from " +
                        locations.get(source).getName() + " to " +
                        locations.get(destination).getName() + " is " + distance + " km");

                List<Integer> path = result.getPath(destination);
                System.out.print("Path: ");
                for (int i = 0; i < path.size(); i++) {
                    int id = path.get(i);
                    if (locations.containsKey(id)) {
                        System.out.print(locations.get(id).getName());
                        if (i < path.size() - 1) {
                            System.out.print(" -> ");
                        }
                    } else {
                        System.out.print("Unknown Location(" + id + ")");
                        if (i < path.size() - 1) {
                            System.out.print(" -> ");
                        }
                    }
                }
                System.out.println();

                // Generate and display a detailed report
                reportGenerator.displayRouteReport(path);

                // Ask if user wants to export the report
                System.out.println("\nDo you want to export this report to a file? (y/n)");
                scanner.nextLine(); // Consume the newline
                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("y") || response.equals("yes")) {
                    String filename = "route_report_" + source + "_to_" + destination + ".txt";
                    reportGenerator.exportReportToFile(path, filename);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric location IDs.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close(); // Properly close the scanner
        }
    }

    private static void displayAvailableLocations() {
        System.out.println("\nAVAILABLE LOCATIONS:");
        System.out.println("----------------------------------------");
        System.out.printf("%-5s | %-30s\n", "ID", "Location Name");
        System.out.println("----------------------------------------");

        List<Integer> locationIds = new ArrayList<>(locations.keySet());
        Collections.sort(locationIds);

        for (Integer id : locationIds) {
            Location loc = locations.get(id);
            System.out.printf("%-5d | %-30s\n", id, loc.getName());
        }
        System.out.println("----------------------------------------");
    }

    private static void loadLocationsFromDB() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to connect to database. Check your database configuration.");
                System.exit(1);
            }

            String query = "SELECT * FROM Locations";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Location loc = new Location(
                            rs.getInt("location_id"),
                            rs.getString("name"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    );
                    locations.put(loc.getLocationId(), loc);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading locations from database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}