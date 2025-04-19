import java.util.*;

public class DijkstraAlgorithm {

    public static class Result {
        public int[] dist;
        public int[] parent;

        public Result(int[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }

        // Reconstructs path from source to destination
        public List<Integer> getPath(int destination) {
            List<Integer> path = new ArrayList<>();
            int current = destination;

            while (current != -1) {
                path.add(current);
                current = parent[current];
            }

            Collections.reverse(path);
            return path;
        }
    }

    public static Result dijkstra(int v, int src, ArrayList<ArrayList<Pair>> graph) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.dist));

        int[] dist = new int[v + 1];      // Distance array
        int[] parent = new int[v + 1];    // Parent array for path reconstruction

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);          // Initialize all parents as -1

        dist[src] = 0;
        pq.add(new Pair(src, 0));

        while (!pq.isEmpty()) {
            Pair current = pq.poll();
            int node = current.node;
            int cost = current.dist;

            for (Pair neighbor : graph.get(node)) {
                if (cost + neighbor.dist < dist[neighbor.node]) {
                    dist[neighbor.node] = cost + neighbor.dist;
                    parent[neighbor.node] = node;
                    pq.add(new Pair(neighbor.node, dist[neighbor.node]));
                }
            }
        }

        return new Result(dist, parent);
    }
}
