public class GameSolver {
    private final GameBoard gameBoard;
    private final State initialState;
    private final boolean isDebug = true;

    public GameSolver(GameBoard gameBoard, State initialState) {
        this.initialState = initialState;
        this.gameBoard = gameBoard;
    }

    public String solve() {
        long start = 0;

        final Search choice = Search.IDA_STAR;

        /*
         * [1] Choose main search algorithm
         */
        final Algorithm algorithm = getAlgorithm(choice, gameBoard, initialState);
        algorithm.isDebug = isDebug;

        /*
         * [2] Calculate static dead locks
         */
        DeadlockManager.calculateStaticDeadSquares(gameBoard);

        /*
         *  [3] Solve the board
         */
        if (isDebug)
            start = System.currentTimeMillis();

        final Node solved = algorithm.solve();
        if (isDebug) {
            final long stop = System.currentTimeMillis();
            System.out.println("The search took: " + (stop - start) + " ms");
            System.out.println("Found " + DeadlockManager.deadState.size() + " dead states");
            System.out.println("Found " + DeadlockManager.deadSquares.size() + " dead squares");
        }

        return solved != null ? getSolution(solved) : "no path";
    }

    private static Algorithm getAlgorithm(final Search choice,
                                          final GameBoard gameBoard,
                                          final State initialState) {
        Algorithm algorithm;
        switch (choice) {
            case A_STAR:
                algorithm = new AstarSearch(gameBoard, initialState);
                break;
            case IDA_STAR:
                algorithm = new IDAstarSearch(gameBoard, initialState);
                break;
            case DFS:
                algorithm = new DepthFirstSearch(gameBoard, initialState);
                break;
            case BFS:
                algorithm = new BreadthFirstSearch(gameBoard, initialState);
                break;
            default:
                algorithm = new IDAstarSearch(gameBoard, initialState);
        }
        return algorithm;
    }


    private String getSolution(final Node node) {
        final StringBuilder stringBuilder = new StringBuilder();
        Node currentNode = node;
        while (currentNode.parent != null) {
            stringBuilder.append(currentNode.state.move);
            stringBuilder.append(" ");
            currentNode = currentNode.parent;
        }

        stringBuilder.reverse();
        return stringBuilder.toString();
    }
}
