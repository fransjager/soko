import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AstarSearch extends Algorithm {
    boolean isDebug = false;

    public AstarSearch(GameBoard gameBoard, State initialState) {
        super(gameBoard, initialState);
    }

    @Override
    public Node solve() {
        // Visited Nodes
        HashSet<Node> visited = new HashSet<>();

        int nodesCreated = 0;
        Node currentNode = new Node(initialState);
        currentNode.cost = 0;
        PriorityQueue<Node> queue = new PriorityQueue<>(20000, Comparator.comparingInt(a -> (a.cost + CalculationUtil.calculateDistance(a.state, gameBoard))));
        queue.add(currentNode);



        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (isCompleted(node.state)) {
                if (isDebug)
                    System.out.println("Created " + nodesCreated + " nodes and a goal was found.");
                return node;
            }


            visited.add(node);
            for (Direction direction : Direction.values()) { // For all directions
                State state = getNextState(direction, node.state);
                if (state != null) { // If a next state is found
                    Node child = new Node(state);
                    nodesCreated++;
                    if(state.deadlocked) {
                        continue;
                    }

                    if (!visited.contains(child) && !queue.contains(node)) { // ..and we have not visited this state before..
                        nodesCreated++;
                        child.parent = node;
                        child.cost += 1;
                        queue.add(child);
                    } else if (!visited.contains(child) && queue.contains(node)) {
                        if ( queue.removeIf(n -> n.equals(child) && child.cost <= n.cost) ) {
                            queue.add(child);
                            nodesCreated++;
                            child.parent = node;
                            child.cost += 1;
                        }
                    }
                }
            }

        }
        return null;
    }
}
