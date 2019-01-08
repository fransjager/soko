
public class Node {
    Node parent;
    State state;
    boolean visited = false;
    int cost = Integer.MAX_VALUE;

    public Node(State state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        int hash = state.hashCode();
        //hash = ((hash << 5) + hash) + state.playerPosition.hashCode();
        hash = (hash * 33) ^ state.playerPosition.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node ? hashCode() == ((Node) obj).hashCode() : false;
    }
}
