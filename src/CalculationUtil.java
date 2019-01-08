public class CalculationUtil {
    private static int BOX_PUSH_WEIGHT = 5;
    private static int BOX_GOAL_WEIGHT = 25;

    /**
     * Very simple lower bound calculations.
     */
    public static int calculateDistance(State state, GameBoard board) {
        if (state.boxPositions.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        int distance = 0;
        for (Coordinate box : state.boxPositions) {
            distance += manhattanDistance(state.playerPosition, box);
        }

        for (Coordinate box : state.boxPositions) {
            for (Coordinate goal : board.goalCoordinates) {
                int manhattan = manhattanDistance(goal, box);
                distance += manhattan * BOX_PUSH_WEIGHT;
                if (board.isGoal(box) && DeadlockManager.isDeadLocked(board, box, state)) {
                    distance -= state.boxPositions.size();
                }
            }
        }

        return distance;
    }

    // https://en.wikipedia.org/wiki/Taxicab_geometry
    public static int manhattanDistance(Coordinate a, Coordinate c) {
        return Math.abs(a.x - c.x) + Math.abs(a.y - c.y);
    }

}
