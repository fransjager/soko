enum Direction {
    UP('U'), DOWN('D'), LEFT('L'), RIGHT('R');

    private final char c;

    Direction(char c) {
        this.c = c;
    }

    public char value() {
        return c;
    }
}