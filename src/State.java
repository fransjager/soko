import java.util.SortedSet;

public class State {
    SortedSet<Coordinate> boxPositions;
    Coordinate playerPosition;
    boolean deadlocked = false;
    char move = ' ';

    public State(SortedSet<Coordinate> boxPositions, Coordinate playerPosition)  {
        this.boxPositions = boxPositions;
        this.playerPosition = playerPosition;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof State ? hashCode() == obj.hashCode() : false;
    }

    /**
     * Bernstein hash: http://www.cse.yorku.ca/~oz/hash.html
     */
    @Override
    public int hashCode() {
        int hash = 5381;
        for(Coordinate coordinate : boxPositions) {
            //hash = ((hash << 5) + hash) + coordinate.hashCode();
            hash = (hash * 33) ^ coordinate.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        int boxNbr = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for(Coordinate box : boxPositions) {
            stringBuilder.append("Box[");
            stringBuilder.append(boxNbr);
            stringBuilder.append("] ");
            stringBuilder.append(box);
            stringBuilder.append("\n");
            boxNbr++;
        }

        stringBuilder.append("Player ");
        stringBuilder.append(playerPosition);
        return stringBuilder.toString();
    }
}
