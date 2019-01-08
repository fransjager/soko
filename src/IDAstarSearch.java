import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class IDAstarSearch extends Algorithm {

    private final int FOUND     = Integer.MIN_VALUE;
    private final int NOT_FOUND = Integer.MAX_VALUE;

    public IDAstarSearch(GameBoard gameBoard, State initialState) {
        super(gameBoard, initialState);
    }

    @Override
    public Node solve() {
        // LIFO stack
        ArrayDeque<Node> stack = new ArrayDeque<>(20000);

        Node currentNode = new Node(initialState);
        int bound = CalculationUtil.calculateDistance(currentNode.state, gameBoard);
        HashSet<Node> visited = new HashSet<>(20000);

        visited.add(currentNode);
        stack.push(currentNode);

        if (isCompleted(currentNode.state)) {
            return currentNode;
        }

        while(true) {
            bound = search(stack,visited, 0, bound);

            if (bound == FOUND) {
                return stack.peek();
            }

            if (bound == NOT_FOUND) {
                return null;
            }

            visited.clear();
        }
    }

    private int search(ArrayDeque<Node> stack, HashSet<Node> visited, int cost, int bound) {
        Node node = stack.peek();

        int estimatedCost = cost + CalculationUtil.calculateDistance(node.state, gameBoard);

        if (estimatedCost > bound) {
            return estimatedCost;
        }

        int min = Integer.MAX_VALUE;
        ArrayList<Node> nodes = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            // For all directions
            State state = getNextState(direction, node.state);
            Node successor = new Node(state);
            if (state != null && !node.state.deadlocked && !visited.contains(successor)) {
                nodes.add(successor);
                successor.parent = node;

                if (isCompleted(successor.state)) {
                    stack.push(successor);
                    return FOUND;
                }
            }
        }

        for (Node n : nodes) {
            stack.push(n);
            n.parent = node;
            visited.add(n);
            int successorBound =  search(stack, visited, cost + 1, bound);

            if (successorBound == FOUND) {
                return FOUND;
            }

            if (successorBound < min) {
                min = successorBound;
            }

            stack.pop();
        }

        return min;
    }
}
