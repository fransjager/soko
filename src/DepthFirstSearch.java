import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Stack;

public class DepthFirstSearch extends Algorithm {

    public DepthFirstSearch(GameBoard gameBoard, State initialState) {
        super(gameBoard, initialState);
    }

    @Override
    public Node solve() {
        int nodesCreated = 0;

        // LIFO stack
        ArrayDeque<Node> stack = new ArrayDeque<>(20000);

        // Visited Nodes
        HashSet<Node> visited = new HashSet<>();

        // Set state for root node
        Node root = new Node(initialState);
        visited.add(root);
        stack.push(root);

        // Make sure we didn't receive an already solved board..
        if(isCompleted(initialState)) {
            return root;
        }

        while (!stack.isEmpty()) {
            Node node = stack.pop();

            for (Direction direction : Direction.values()) { // For all directions
                State state = getNextState(direction, node.state);
                if (state != null) { // If a next state is found
                    Node child = new Node(state);
                    if (!visited.contains(child) && !node.state.deadlocked) { // ..and we have not visited this state before..
                        nodesCreated++;
                        child.visited = true;
                        child.parent = node;
                        visited.add(node);
                        stack.push(child);

                        if (isCompleted(state)) { // Return the end state upon completion. Backtrace with parents..
                            if(isDebug) {
                                System.out.println("Created " + nodesCreated + " nodes and a goal was found.");
                            }
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
