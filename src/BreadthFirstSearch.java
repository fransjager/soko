import java.util.HashSet;
import java.util.LinkedList;

public class BreadthFirstSearch extends Algorithm {
    boolean isDebug = false;


    public BreadthFirstSearch(GameBoard gameBoard, State initialState) {
        super(gameBoard, initialState);
    }

    @Override
    public Node solve() {
        int nodesCreated = 0;

        // FIFO queue
        LinkedList<Node> queue = new LinkedList<>();

        // Visited Nodes
        HashSet<Node> visited = new HashSet<>();

        // Set state for root node
        Node root = new Node(initialState);
        visited.add(root);
        queue.push(root);

        // Make sure we didn't receive an already solved board..
        if (isCompleted(initialState)) {
            return root;
        }

        while (!queue.isEmpty()) {
            Node node = queue.remove();

            if (node.state.deadlocked)
                continue;

            for (Direction direction : Direction.values()) { // For all directions
                State state = getNextState(direction, node.state);
                if (state != null) { // If a next state is found
                    Node child = new Node(state);
                    if (!visited.contains(child)) { // ..and we have not visited this state before..
                        nodesCreated++;
                        child.visited = true;
                        child.parent = node;
                        visited.add(node);
                        queue.push(child);

                        if (isCompleted(state)) { // Return the end state upon completion. Backtrace with parents..
                            if (isDebug)
                                System.out.println("Created " + nodesCreated + " nodes and a goal was found.");
                            return child;
                        }
                    }
                }
            }

        }
        if (isDebug)
            System.out.println("Created " + nodesCreated + " nodes.");
        return null;
    }
}
