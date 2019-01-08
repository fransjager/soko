enum GameObject {
    WALL('#'), EMPTY(' '), BOX('$'), PLAYER('@'), PLAYER_ON_GOAL('+'), BOX_ON_GOAL('*'), GOAL('.'), UNDEFINED('U');

    private final char c;
    GameObject(char c) {
        this.c = c;
    }

    public char getValue() {
        return c;
    }

    public static GameObject getEnum(char c) {
        if (c == '#')
            return WALL;
        if (c == ' ')
            return EMPTY;
        if (c == '$')
            return BOX;
        if (c == '@')
            return PLAYER;
        if (c == '+')
            return PLAYER_ON_GOAL;
        if (c == '*')
            return BOX_ON_GOAL;
        if (c == '.')
            return GOAL;

        return UNDEFINED;
    }
}
