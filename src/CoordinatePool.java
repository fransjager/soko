import java.util.LinkedList;
import java.util.Queue;

class CoordinatePool {
    private Queue<Coordinate> pool;

    CoordinatePool(int initialsize) {
        pool = new LinkedList<>();
        for(int i = 0; i < initialsize; i++) {
            pool.add(new Coordinate());
        }
    }

    public Coordinate getCoordinate() {
        if (pool.isEmpty()) {
            expand(100);
        }
        return pool.remove();
    }

    private void expand(int size) {
        for(int i = 0; i < size; i++) {
            pool.add(new Coordinate());
        }
    }

    public void returnCoordinate(Coordinate coordinate) {
        pool.add(coordinate);
    }
}
