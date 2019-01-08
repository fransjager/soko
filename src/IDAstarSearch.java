import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class IDAstarSearch extends Algorithm {
    private final static int FOUND     = Integer.MIN_VALUE;

    public IDAstarSearch(final GameBoard gameBoard, final State initialState) {
        super(gameBoard, initialState);
    }

    @Override
    public Node solve() {
        // LIFO stack
        ArrayDeque<Node> stack = new ArrayDeque<>(20000);

        final Node currentNode = new Node(initialState);
        int bound = CalculationUtil.calculateDistance(currentNode.state, gameBoard);
        final HashSet<Node> visited = new HashSet<>(20000);

        visited.add(currentNode);
        stack.push(currentNode);

        if (isCompleted(currentNode.state)) {
            return currentNode;
        }

        while (true) {
            bound = search(stack, visited, 0, bound);

            if (bound == FOUND) {
                return stack.peek();
            }

            visited.clear();
        }
    }

    private int search(final ArrayDeque<Node> stack,
                       final HashSet<Node> visited,
                       final int cost,
                       final int bound) {
        final Node node = stack.peek();
        final int estimatedCost = cost + CalculationUtil.calculateDistance(node.state, gameBoard);

        if (estimatedCost > bound) {
            return estimatedCost;
        }

        int min = Integer.MAX_VALUE;
        final ArrayList<Node> nodes = new ArrayList<>();

        // For all directions
        for (final Direction direction : Direction.values()) {
            final State state = getNextState(direction, node.state);
            final Node successor = new Node(state);
            if (state != null && !node.state.deadlocked && !visited.contains(successor)) {
                nodes.add(successor);
                successor.parent = node;

                if (isCompleted(successor.state)) {
                    stack.push(successor);
                    return FOUND;
                }
            }
        }

        for (final Node n : nodes) {
            stack.push(n);
            n.parent = node;
            visited.add(n);
            final int successorBound =  search(stack, visited, cost + 1, bound);

            if (successorBound == FOUND) {
                return FOUND;
            }

            min = Math.min(min, successorBound);

            stack.pop();
        }

        return min;
    }
}
