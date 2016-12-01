//mimick static class behavior
public final class Move {
    public static final String[] ALL_MOVES = {"f", "u", "l", "r", "d", "b", "x", "y", "z"};
    
    public static final String[][] TURN_MOVES = {{"f", "u", "l", "r", "d", "b"},
                                                 {"Front turn", "Up turn", "Left turn", "Right turn", "Down turn", "Back turn"}};
    public static final String[][] ROT_MOVES = {{"x", "y", "z"}, {"x rotation", "y rotation", "z rotation"}};

    public static final String[] APPEND_MOVES = {"i", "2", ""};
    
    private Move() {}
    
    public static String head(String move) {
        return move.substring(0, 1);
    }
    public static String tail(String move) {
        return move.substring(1);
    }
    
    public static boolean isMove(String move) {
        return Arr.in(ALL_MOVES, head(move));
    }
    public static boolean isTurnMove(String move) {
        return Arr.in(TURN_MOVES[0], head(move));
    }

    public static boolean hasAppendMove(String move) {
        return Arr.in(APPEND_MOVES, tail(move));
    }
}
