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

        try {
            game_mode = in.readLine();
    	}
    	catch ( IOException e ) { }
        
        if (!in(PLAY_MODES[1], game_mode)) {
            s = "Could not understand input. Please type ";
            for (int i = 0; i < PLAY_MODES[1].length; i++) {
                if (i == PLAY_MODES[1].length - 1) {
                    s += "or " + "'" + PLAY_MODES[1][i] + "'";
                } else {
                    s += "'" + PLAY_MODES[1][i] + "'" + ", ";
                }
            }
            System.out.println(s);
            game_mode = "fp";
        }
        
        s = "Game mode is: " + game_mode;
        System.out.println(s);
    }
    public boolean in(String[] arr, String s) {
        //https://stackoverflow.com/questions/4962361/where-is-javas-array-indexof
        return java.util.Arrays.asList(arr).indexOf(s) != -1;
    }
    
    public boolean play() {
        if (game_mode.equals("fp")) {
            return freePlay();
        } else {
            return solveScrambled();
        }
    }
    public boolean freePlay() {
        RubiksCube rc = new RubiksCube(3);
        System.out.println(rc);

        String moves = "";
        for (;;) {
            System.out.print("Move(s): ");
            try {
                moves = in.readLine();
            }
            catch ( IOException e ) { }
            
            if (moves.equals("q")) {
                break;
            }
            if (!movesAction(rc, moves)) {
                System.out.println("Could not recognize " + moves + "\nTry again.");
            } else {
                System.out.println(rc);
            }
        }
        
        return false;
    }
    public boolean solveScrambled() {
        RubiksCube winningC = new RubiksCube(3);
        RubiksCube rc = new RubiksCube(3);
        
        int scrambleTimes = chooseTimesToScramble();
        scramble(rc, scrambleTimes);
        System.out.println(rc);

        String moves = "";
        for (;;) {
            System.out.print("Move(s): ");
            try {
                moves = in.readLine();
            }
            catch ( IOException e ) { }
            
            if (moves.equals("q")) {
                break;
            }
            if (!movesAction(rc, moves)) {
                System.out.println("Could not recognize " + moves + "\nTry again.");
            } else {
                System.out.println(rc);
                
                if (rc.equals(winningC)) {
                    System.out.println("Congrats! You solved it.");
                    break;
                }
            }
        }
        return false;
    }
    public int chooseTimesToScramble() {
        System.out.println("How many times to scramble the cube?");
        int scrambleTimes = 1;

        try {
            scrambleTimes = Integer.parseInt(in.readLine());
        }
        catch ( IOException e ) { }

        if (scrambleTimes < 1) {
            scrambleTimes = 1;
        }
        
        return scrambleTimes;
    }
    public void scramble(RubiksCube rc, int scrambleTimes) {
        String scrambleSequence = "";
        int tml = TURN_MOVES[0].length;
        int rml = ROT_MOVES[0].length;
        int totMoves = tml + rml;
        //maximum possible moves; include for i and 2;
        int maxMoves = (tml + rml) * 3;
        
        // [0, totMoves - 1] : move
        // [totMoves, 2 * totMoves - 1] : move2
        // [2 * totMoves, 3 * totMoves - 1] : movei
        // [0, tml - 1] : tml move
        for (int i = 0; i < scrambleTimes; i++) {
            int random = (int)(Math.random() * maxMoves);
            
            String tail = "";
            if (random >= 2 * totMoves) {
                tail = "i";
                random -= 2 * totMoves;
            } else if (random >= totMoves) {
                tail = "2";
                random -= totMoves;
            }
            
            String head = "";
            if (random < tml) {
                head = TURN_MOVES[0][random];
            } else {
                random -= tml;
                head = ROT_MOVES[0][random];
            }
            
            String move = head + tail;
            scrambleSequence += move;
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
