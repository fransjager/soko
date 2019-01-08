import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Algorithm {

    GameBoard gameBoard;
    State initialState;
    CoordinatePool coordinatePool = new CoordinatePool(1000);
    boolean isDebug = false;


    public Algorithm(GameBoard gameBoard, State initialState) {
        this.gameBoard = gameBoard;
        this.initialState = initialState;
    }


    public abstract Node solve();

    protected State getNextState(Direction direction, State state) {
        Coordinate tmpCoordinate = coordinatePool.getCoordinate();
        tmpCoordinate.x = state.playerPosition.x;
        tmpCoordinate.y = state.playerPosition.y;

        switch (direction) {
            case UP:
                tmpCoordinate.y -= 1;
                break;
            case DOWN:
                tmpCoordinate.y += 1;
                break;
            case LEFT:
                tmpCoordinate.x -= 1;
                break;
            case RIGHT:
                tmpCoordinate.x += 1;
                break;
            default:
                // Nothing
        }
        HashSet wallCoordinates = gameBoard.wallCoordinates;

        if (wallCoordinates.contains(tmpCoordinate)) {
            coordinatePool.returnCoordinate(tmpCoordinate);
            return null;
        }

        Coordinate newBoxCoordinate = null;
        //Check if we want to move a box
        if (state.boxPositions.contains(tmpCoordinate)) {
            newBoxCoordinate = getBoxMove(tmpCoordinate, direction, state);
            if (newBoxCoordinate == null) {
                state.boxPositions.add(tmpCoordinate);
                coordinatePool.returnCoordinate(tmpCoordinate);
                return null;
            }
        }


        SortedSet<Coordinate> hashSet = new TreeSet<>();
        state.boxPositions.forEach(c -> hashSet.add(new Coordinate(c.x, c.y)));
        State newState = new State(hashSet, new Coordinate(tmpCoordinate.x, tmpCoordinate.y));
        newState.deadlocked = false;
        newState.move = direction.value();
        if (newBoxCoordinate != null) {
            newState.boxPositions.remove(tmpCoordinate);
            hashSet.add(newBoxCoordinate);
            newState.deadlocked = DeadlockManager.isDeadLocked(gameBoard, newBoxCoordinate, newState);
        }

        coordinatePool.returnCoordinate(tmpCoordinate);
        return newState;
    }

    protected Coordinate getBoxMove(Coordinate boxCoordinate, Direction direction, State state) {
        Coordinate tmpCoordinate = coordinatePool.getCoordinate();
        tmpCoordinate.x = boxCoordinate.x;
        tmpCoordinate.y = boxCoordinate.y;

        switch (direction) {
            case UP:
                tmpCoordinate.y -= 1;
                break;
            case DOWN:
                tmpCoordinate.y += 1;
                break;
            case LEFT:
                tmpCoordinate.x -= 1;
                break;
            case RIGHT:
                tmpCoordinate.x += 1;
                break;
            default:
                // Nothing
        }

        HashSet wallCoordinates = gameBoard.wallCoordinates;
        if (wallCoordinates.contains(tmpCoordinate)) {
            coordinatePool.returnCoordinate(tmpCoordinate);
            return null;
        }

        if (state.boxPositions.contains(tmpCoordinate)) {
            coordinatePool.returnCoordinate(tmpCoordinate);
            return null;
        }

        coordinatePool.returnCoordinate(tmpCoordinate);
        return new Coordinate(tmpCoordinate.x, tmpCoordinate.y);
    }

    protected boolean isCompleted(State state) {
        boolean onGoal = false;
        for (Coordinate goal : gameBoard.goalCoordinates) {
            onGoal = false;
            for (Coordinate box : state.boxPositions) {
                if (goal.x == box.x && goal.y == box.y) {
                    onGoal = true;
                    continue;
                }
            }

            if (!onGoal) {
                return false;
            }
        }
        return onGoal;
    }
}
