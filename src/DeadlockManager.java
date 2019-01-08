import java.util.ArrayDeque;
import java.util.HashSet;

public final class DeadlockManager {
    static HashSet<State> deadState = new HashSet<>(1000);
    static HashSet<Coordinate> deadSquares = new HashSet<>(1000);
    static CoordinatePool coordinatePool = new CoordinatePool(1000);

    private static boolean isDebug = false;


    public static void calculateStaticDeadSquares(GameBoard gameBoard) {
        HashSet<Coordinate> empty = gameBoard.emptyCoordinates;
        HashSet<Coordinate> visited = null;
        for (Coordinate coordinate : empty) {

            if (deadSquares.contains(coordinate)) {
                continue;
            }

            // LIFO stack
            ArrayDeque<Coordinate> stack = new ArrayDeque<>();

            // Visited coord
            visited = new HashSet<>();

            // Set root coord
            visited.add(coordinate);
            stack.add(coordinate);

            boolean found = false;
            while (!stack.isEmpty() && !found) {
                Coordinate coordinate1 = stack.pop();
                for (Direction direction : Direction.values()) { // For all directions
                    Coordinate nextMove = getNextMove(direction, coordinate1, gameBoard);
                    if (nextMove != null && !visited.contains(nextMove) && !deadSquares.contains(nextMove)) { // If a next state is found
                        if(!visited.contains(nextMove) && !deadSquares.contains(nextMove)) {
                            visited.add(nextMove);
                            stack.push(nextMove);

                            if (gameBoard.isGoal(nextMove)) { // Return the end state upon completion. Backtrace with parents..
                                found = true;
                                break;
                            }
                        }
                    }
                }
        }

            if (!found) {
                //If we didn't find it. We add all tiles we visited.
                deadSquares.addAll(visited);
                if (isDebug) {
                    System.out.println("Found " + visited.size() + " dead squares.");

                    for (Coordinate visit : visited) {
                        System.out.println(visit);
                    }
                }
            }
        }
    }

    private static Coordinate getNextMove(Direction direction, Coordinate coordinate, GameBoard gameBoard) {
        switch (direction) {
            case UP:
                if ((0 > (coordinate.y - 1) || gameBoard.maxY < (coordinate.y + 1) || (gameBoard.board[coordinate.y - 1].length - 1) < (coordinate.x) || (gameBoard.board[coordinate.y + 1].length - 1) < (coordinate.x))) {
                    return null;
                }

                if (GameObject.getEnum(gameBoard.board[coordinate.y + 1][coordinate.x]) != GameObject.WALL && GameObject.getEnum(gameBoard.board[coordinate.y - 1][coordinate.x]) != GameObject.WALL) {
                    return new Coordinate(coordinate.x, coordinate.y - 1);
                }
                break;
            case DOWN:
                if (0 > (coordinate.y - 1) || (coordinate.y + 1) > gameBoard.maxY || (gameBoard.board[coordinate.y - 1].length - 1) < (coordinate.x) || (gameBoard.board[coordinate.y + 1].length - 1) < (coordinate.x)) {
                    return null;
                }

                if (GameObject.getEnum(gameBoard.board[coordinate.y - 1][coordinate.x]) != GameObject.WALL && GameObject.getEnum(gameBoard.board[coordinate.y + 1][coordinate.x]) != GameObject.WALL) {
                    return new Coordinate(coordinate.x, coordinate.y + 1);
                }
                break;
            case LEFT:
                if (gameBoard.maxX < coordinate.x + 1 || 0 > coordinate.x - 1 || (gameBoard.board[coordinate.y].length - 1) < (coordinate.x + 1) ) {
                    return null;
                }

                if (GameObject.getEnum(gameBoard.board[coordinate.y][coordinate.x + 1]) != GameObject.WALL && GameObject.getEnum(gameBoard.board[coordinate.y][coordinate.x - 1]) != GameObject.WALL) {
                    return new Coordinate(coordinate.x - 1, coordinate.y);
                }
                break;
            case RIGHT:
                if (0 > coordinate.x - 1 || gameBoard.maxX < coordinate.x + 1 || (gameBoard.board[coordinate.y].length - 1) < (coordinate.x + 1)) {
                    return null;
                }

                if (GameObject.getEnum(gameBoard.board[coordinate.y][coordinate.x - 1]) != GameObject.WALL  && GameObject.getEnum(gameBoard.board[coordinate.y][coordinate.x + 1]) != GameObject.WALL) {
                    return new Coordinate(coordinate.x + 1, coordinate.y);
                }
                break;
        }
        return null;
    }

    public static boolean isDeadLocked(GameBoard gameBoard, Coordinate box, State state) {
        if (simpleDynamicDeadLockCheck(gameBoard, box, state)) {
            return true;
        }

        if (deadState.contains(state)) {
            return true;
        }

        if (deadSquares.contains(box)) {
            return true;
        }

        return false;
    }

    // Simple dynamic deadlock detection
    private static boolean simpleDynamicDeadLockCheck(GameBoard gameBoard, Coordinate box, State state) {
        if (gameBoard.goalCoordinates.contains(box)) {
            return false;
        }

        Coordinate walltmp_1 = coordinatePool.getCoordinate();
        Coordinate walltmp_2 = coordinatePool.getCoordinate();
        Coordinate walltmp_3 = coordinatePool.getCoordinate();

        boolean deadlocked = false;
        for (Direction direction : Direction.values()) {
            switch (direction) {
                case UP:
                    // XX
                    // $X
                    walltmp_1.x = box.x + 1;
                    walltmp_1.y = box.y;
                    walltmp_2.x = box.x;
                    walltmp_2.y = box.y - 1;
                    walltmp_3.x = box.x + 1;
                    walltmp_3.y = box.y - 1;
                    deadlocked = isDeadLockSquare(gameBoard, walltmp_1, walltmp_2, walltmp_3, state);
                    break;
                case RIGHT:
                    // XX
                    // $X
                    walltmp_1.x = box.x + 1;
                    walltmp_1.y = box.y;
                    walltmp_2.x = box.x;
                    walltmp_2.y = box.y - 1;
                    walltmp_3.x = box.x + 1;
                    walltmp_3.y = box.y - 1;
                    deadlocked = isDeadLockSquare(gameBoard, walltmp_1, walltmp_2, walltmp_3, state);
                    break;
                case LEFT:
                    // XX
                    // X$
                    walltmp_1.x = box.x - 1;
                    walltmp_1.y = box.y;
                    walltmp_2.x = box.x;
                    walltmp_2.y = box.y + 1;
                    walltmp_3.x = box.x - 1;
                    walltmp_3.y = box.y + 1;
                    deadlocked = isDeadLockSquare(gameBoard, walltmp_1, walltmp_2, walltmp_3, state);
                    break;
                case DOWN:
                    // $X
                    // XX
                    walltmp_1.x = box.x + 1;
                    walltmp_1.y = box.y;
                    walltmp_2.x = box.x;
                    walltmp_2.y = box.y + 1;
                    walltmp_3.x = box.x + 1;
                    walltmp_3.y = box.y + 1;
                    deadlocked = isDeadLockSquare(gameBoard, walltmp_1, walltmp_2, walltmp_3, state);
                    break;
                default:
                    // Nothing
            }

            if (deadlocked) {
                addDeadState(state);
                break;
            }
        }

        coordinatePool.returnCoordinate(walltmp_1);
        coordinatePool.returnCoordinate(walltmp_2);
        coordinatePool.returnCoordinate(walltmp_3);
        return deadlocked;
    }

    public static boolean isDeadLockSquare(GameBoard gameBoard, Coordinate a, Coordinate b, Coordinate c, State state) {
        if (!gameBoard.isWall(a) && !state.boxPositions.contains(a)) {
            return false;
        }

        if (!gameBoard.isWall(b) && !state.boxPositions.contains(b)) {
            return false;
        }

        if (!gameBoard.isWall(c) && !state.boxPositions.contains(c)) {
            return false;
        }

        return true;
    }

    public static void addDeadState(State state) {
        deadState.add(state);
    }

    public static void isDeadState(State state) {
        deadState.contains(state);
    }

    public static boolean isDeadSquare(Coordinate coordinate) {
        return deadSquares.contains(coordinate);
    }
}
