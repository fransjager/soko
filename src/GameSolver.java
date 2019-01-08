public class GameSolver {
    GameBoard gameBoard;
    State initialState;
    boolean isDebug = true;

    public GameSolver(GameBoard gameBoard, State initialState) {
        this.initialState = initialState;
        this.gameBoard = gameBoard;
    }

    public String solve() {
        long start = 0;

            Search choice = Search.IDA_STAR;

        Node solved;
        Algorithm algorithm;
        /**
         * [1] Choose main search algorithm
         */
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
        algorithm.isDebug = isDebug;

        /**
         * [2] Calculate static dead locks
         */
        DeadlockManager.calculateStaticDeadSquares(gameBoard);

        /**
         *  [3] Solve the board
         */
        if (isDebug)
            start = System.currentTimeMillis();
        long stop;
        solved = algorithm.solve();
        if (isDebug) {
            stop = System.currentTimeMillis();
            System.out.println("The search took: " + (stop - start) + " ms");
            System.out.println("Found " + DeadlockManager.deadState.size() + " dead states");
            System.out.println("Found " + DeadlockManager.deadSquares.size() + " dead squares");
        }

        String solution = "";
        solution = solved != null ? getSolution(solved) : "no path";
        return solution;
    }


    protected String getSolution(Node node) {
        StringBuilder stringBuilder = new StringBuilder();
        Node currentNode = node;
        while(currentNode.parent != null) {
            stringBuilder.append(currentNode.state.move);
            stringBuilder.append(" ");
            currentNode = currentNode.parent;
        }

        stringBuilder.reverse();
        return stringBuilder.toString();
    }
}
