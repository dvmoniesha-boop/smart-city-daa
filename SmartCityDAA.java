import java.util.*;

public class SmartCityDAA {

    // unit 4: emergency routing (Dijkstra algorithm)
    static class Edge {
        String target;
        int weight;

        Edge(String target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        String name;
        int distance;

        Node(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }

        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public static int dijkstra(Map<String, List<Edge>> graph, String start, String end) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Map<String, Integer> distances = new HashMap<>();

        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.name.equals(end)) {
                return current.distance;
            }

            if (current.distance > distances.getOrDefault(current.name, Integer.MAX_VALUE)) {
                continue;
            }

            for (Edge edge : graph.getOrDefault(current.name, new ArrayList<>())) {
                int newDist = current.distance + edge.weight;
                if (newDist < distances.getOrDefault(edge.target, Integer.MAX_VALUE)) {
                    distances.put(edge.target, newDist);
                    pq.add(new Node(edge.target, newDist));
                }
            }
        }
        return distances.getOrDefault(end, -1);
    }

    // unit 3: budget allocation (0/1 knapsack dp)
    public static int allocateBudget(int budget, int[] costs, int[] impact, int n) {
        int[][] dp = new int[n + 1][budget + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= budget; w++) {
                if (costs[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], impact[i - 1] + dp[i - 1][w - costs[i - 1]]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        return dp[n][budget];
    }

    // unit 2: vehicle dispatch (activity selection greedy)
    static class Job {
        int start, end;

        Job(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public static int scheduleVehicles(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return 0;
        }

        jobs.sort(Comparator.comparingInt(a -> a.end));
        int count = 1;
        int lastEnd = jobs.get(0).end;

        for (int i = 1; i < jobs.size(); i++) {
            if (jobs.get(i).start >= lastEnd) {
                count++;
                lastEnd = jobs.get(i).end;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== smart city daa system ===");

        // 1. graph routing test
        Map<String, List<Edge>> cityGraph = new HashMap<>();
        cityGraph.put("Hospital", Arrays.asList(new Edge("Junction_A", 4), new Edge("Junction_B", 2)));
        cityGraph.put("Junction_A", Arrays.asList(new Edge("Hospital", 4), new Edge("Emergency_Site", 5)));
        cityGraph.put("Junction_B", Arrays.asList(new Edge("Hospital", 2), new Edge("Junction_A", 1), new Edge("Emergency_Site", 8)));
        cityGraph.put("Emergency_Site", Arrays.asList(new Edge("Junction_A", 5), new Edge("Junction_B", 8)));

        int shortestTime = dijkstra(cityGraph, "Hospital", "Emergency_Site");
        System.out.println("ambulance shortest travel time: " + shortestTime + " mins");

        // 2. resource allocation test
        int budget = 50;
        int[] costs = {10, 20, 30};
        int[] impact = {60, 100, 120};
        int maxImpact = allocateBudget(budget, costs, impact, costs.length);
        System.out.println("maximum resource impact: " + maxImpact);

        // 3. job scheduling test
        List<Job> jobs = new ArrayList<>(Arrays.asList(
            new Job(1, 3), new Job(2, 5), new Job(4, 6),
            new Job(6, 7), new Job(5, 9), new Job(8, 9)
        ));
        int scheduledCount = scheduleVehicles(jobs);
        System.out.println("maximum emergency tasks scheduled: " + scheduledCount);
    }
}