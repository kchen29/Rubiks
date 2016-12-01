import java.io.*;
import java.util.*;

public class RubiksGame {
    
    //~~~~~~~~~~INSTANCE VARIABLES
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
        for (int i = 0; i < Move.TURN_MOVES[0].length; i++) {
            s += "\t" + Move.TURN_MOVES[0][i] + ": " + Move.TURN_MOVES[1][i] + "\n";
        }
        for (int i = 0; i < Move.ROT_MOVES[0].length; i++) {
            s += "\t" + Move.ROT_MOVES[0][i] + ": " + Move.ROT_MOVES[1][i] + "\n";
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
        
            if (Arr.in(PLAY_MODES[1], game_mode)) {
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
        int totMoves = Move.ALL_MOVES.length;

        String head, tail;
        for (int i = 0; i < scrambleLength; i++) {
            int random1 = (int)(Math.random() * totMoves);
            int random2 = (int)(Math.random() * 3);

            head = Move.ALL_MOVES[random1];
            tail = Move.APPEND_MOVES[random2];
            
            String move = head + tail;
            scrambleSequence += move + " ";
            moveAction(rc, move);
        }
        System.out.println(scrambleSequence);
    }

    public boolean movesAction(RubiksCube rc, String moves) {
        List<String> moveSequence = new ArrayList<String>();

        //parse
        for (int i = 0; i < moves.length(); i++) {
            String add = moves.substring(i, i + 1);
            if (add.equals(" ")) {
                continue; //skip spaces
            }
                
            if (!Move.isMove(add)) {
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
        }
        
        for (String move : moveSequence) {
            moveAction(rc, move);
        }
        return true;
    }
    public void moveAction(RubiksCube rc, String move) {
        if (Move.isTurnMove(move)) {
            rc.turn(move);
        } else {
            rc.rot(move);
        }
    }

    //~~~~~~~~~~MAIN
    public static void main(String[] args) {
        RubiksGame rg = new RubiksGame();
        rg.play();
        System.out.println("Your game ends.");
    }
}
