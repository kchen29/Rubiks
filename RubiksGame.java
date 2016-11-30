import java.io.*;
import java.util.*;

public class RubiksGame {
    
    //~~~~~~~~~~INSTANCE VARIABLES
    public static final String[][] TURN_MOVES = {{"f", "u", "l", "r", "d", "b"},
                                                 {"Front turn", "Up turn", "Left turn", "Right turn", "Down turn", "Back turn"}};
    public static final String[][] ROT_MOVES = {{"x", "y", "z"}, {"x rotation", "y rotation", "z rotation"}};
    public static final String[][] PLAY_MODES = {{"Free-play", "Solve Scrambled"}, {"fp", "ss"}};

    private String game_mode;
    
    private InputStreamReader isr;
    private BufferedReader in;

    //~~~~~~~~~~CONSTRUCTORS
    public RubiksGame() {
    	isr = new InputStreamReader( System.in );
        in = new BufferedReader( isr );
        newGame();
    }

    //~~~~~~~~~~METHODS
    public void newGame() {
        System.out.println("Welcome to Rubik's Game!\n");
        instructions();
        chooseMode();
        System.out.println("\nLet's start playing!");
    }
    public void instructions() {
        String s;
        s = "Instructions: To play, type in a move, and the cube will move accordingly:\n";
        for (int i = 0; i < TURN_MOVES[0].length; i++) {
            s += "\t" + TURN_MOVES[0][i] + ": " + TURN_MOVES[1][i] + "\n";
        }
        for (int i = 0; i < ROT_MOVES[0].length; i++) {
            s += "\t" + ROT_MOVES[0][i] + ": " + ROT_MOVES[1][i] + "\n";
        }
        s += "\n'i' and '2' can be appended to these moves to do inverse and double, respectively\n";
        s += "\n" + "Press 'q' in-game to quit\n";
        System.out.println(s);
    }
    public void chooseMode() {
        String s;
        s = "What mode do you want to play in?\n";
        for (int i = 0; i < PLAY_MODES[0].length; i++) {
            s += "\t" + PLAY_MODES[0][i] + " (" + PLAY_MODES[1][i] + ")" + "\n";
        }
        System.out.print(s);

        for (;;) {
            try {
                game_mode = in.readLine();
            }
            catch ( IOException e ) { }
        
            if (in(PLAY_MODES[1], game_mode)) {
                break;
            }
            s = "Could not understand input. Please type ";
            for (int i = 0; i < PLAY_MODES[1].length; i++) {
                if (i == PLAY_MODES[1].length - 1) {
                    s += "or " + "'" + PLAY_MODES[1][i] + "'";
                } else {
                    s += "'" + PLAY_MODES[1][i] + "'" + ", ";
                }
            }
            System.out.println(s);
        }
        
        s = "Game mode is: " + game_mode;
        System.out.println(s);
    }
    public boolean in(String[] arr, String s) {
        //https://stackoverflow.com/questions/4962361/where-is-javas-array-indexof
        return java.util.Arrays.asList(arr).indexOf(s) != -1;
    }
    
    public void play() {
        if (game_mode.equals("fp")) {
            freePlay();
        } else {
            solveScrambled();
        }
    }
    public void freePlay() {
        RubiksCube rc = new RubiksCube(3);
        System.out.println(rc);

        String moves = "";
        for (;;) {
            moves = queryMoves();
            if (moves.equals("q"))
                break;
            if (movesAction(rc, moves))
                System.out.println(rc);
            else
                System.out.println("Could not recognize " + moves + "\nTry again.");
        }
    }
    public String queryMoves() {
        String moves = "";
        System.out.print("Move(s): ");
        try {
            moves = in.readLine();
        }
        catch ( IOException e ) { }
        return moves;
    }
    public void solveScrambled() {
        RubiksCube rc = new RubiksCube(3);
        
        scramble(rc);
        System.out.println(rc);

        String moves = "";
        for (;;) {
            moves = queryMoves();
            if (moves.equals("q"))
                break;
            if (movesAction(rc, moves)) {
                System.out.println(rc);
                if (rc.allSame()) {
                    System.out.println("Congratulations! You solved it.");
                    break;
                }
            } else {
                System.out.println("Could not recognize " + moves + "\nTry again.");
            }
        }
    }
    public void scramble(RubiksCube rc) {
        int scrambleLength = queryScrambleLength();
        scramble(rc, scrambleLength);
    }
    public int queryScrambleLength() {
        System.out.println("How long is the scramble?");
        int scrambleLength = 1;

        try {
            scrambleLength = Integer.parseInt(in.readLine());
        }
        catch ( IOException e ) { }

        if (scrambleLength < 1) {
            scrambleLength = 1;
        }
        
        return scrambleLength;
    }
    public void scramble(RubiksCube rc, int scrambleLength) {
        String scrambleSequence = "";
        int tml = TURN_MOVES[0].length;
        int rml = ROT_MOVES[0].length;
        int totMoves = tml + rml;

        String head, tail;
        for (int i = 0; i < scrambleLength; i++) {
            int random1 = (int)(Math.random() * totMoves);
            int random2 = (int)(Math.random() * 3);
            
            // [0, tml - 1] : tml move
            if (random1 < tml) {
                head = TURN_MOVES[0][random1];
            } else {
                random1 -= tml;
                head = ROT_MOVES[0][random1];
            }
            
            switch (random2) {
            case 2: tail = "i";
                break;
            case 1: tail = "2";
                break;
            default: tail = "";
            }
            
            String move = head + tail;
            scrambleSequence += move + " ";
            moveAction(rc, move);
        }
        System.out.println(scrambleSequence);
    }

    public boolean movesAction(RubiksCube rc, String moves) {
        List<String> moveSequence = new ArrayList<String>();

        //parse
        int i = 0;
        while (i < moves.length()) {
            String add = moves.substring(i, i + 1);
            if (add.equals(" ")) {
                i++; //skip spaces
                continue;
            }
                
            if (!in(TURN_MOVES[0], add) && !in(ROT_MOVES[0], add)) {
                return false;
            }

            if (i < moves.length() - 1) {
                String tail = moves.substring(i + 1, i + 2);
                if (tail.equals("i") || tail.equals("2")) {
                    add += tail;
                    i++;
                }
            }
            moveSequence.add(add);
            i++;
        }
        
        for (String move : moveSequence) {
            moveAction(rc, move);
        }
        return true;
    }
    public boolean moveAction(RubiksCube rc, String move) {
        String head = move.substring(0, 1);
        if (in(TURN_MOVES[0], head)) {
            rc.turn(move);
        } else {
            rc.rot(move);
        }
        return true;
    }

    //~~~~~~~~~~MAIN
    public static void main(String[] args) {
        RubiksGame rg = new RubiksGame();
        rg.play();
        System.out.println("Your game ends.");
    }
}
