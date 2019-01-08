public class Coordinate implements Comparable {
    public int x;
    public int y;

    public Coordinate() {

    }

    public Coordinate(int k, int v) {
        x = k;
        y = v;
    }

    @Override
    public int hashCode() {
        // Pairing function
        return y + (((x + y) * (x + y + 1)) / 2);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Coordinate ? ((Coordinate) obj).hashCode() == hashCode() : false;
    }


    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int compareTo(Object o) {
        Coordinate other = (Coordinate) o;

        if (x < other.x) {
            return -1;
        }

        if (x > other.x) {
            return 1;
        }

        //Else check y
        if (y < other.y) {
            return 1;
        }

        if (y > other.y) {
            return -1;
        }


        return 0;
    }
}
