import java.util.HashSet;

public class GameBoard {
    HashSet<Coordinate> wallCoordinates;
    HashSet<Coordinate> goalCoordinates;
    HashSet<Coordinate> emptyCoordinates;
    char[][] board;
    int maxY;
    int maxX;

    public GameBoard(HashSet<Coordinate> wallCoordinates, HashSet<Coordinate> goalCoordinates, HashSet<Coordinate> emptyCoordinates, char[][] board) {
        this.wallCoordinates = wallCoordinates;
        this.goalCoordinates = goalCoordinates;
        this.emptyCoordinates = emptyCoordinates;
        this.board = board;

        maxY = board.length - 1;
        maxX = 0;
        for (char[] x : board) {
            if (maxX < x.length) {
                maxX = x.length - 1;
            }
        }
    }

    public boolean isGoal(Coordinate goal) {
        return goalCoordinates.contains(goal);
    }

    public boolean isWall(Coordinate wall) {
        return wallCoordinates.contains(wall);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
