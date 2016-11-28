public class RubiksCube {
    //possible implementations: use faces or use edges / corners

    //~~~~~~~~~~INSTANCE VARIABLES
    //3D cube
    public static final int NUM_FACES = 6;
    //default colors of the faces
    public static String[] COLORS = {"w", "r", "b", "g", "o", "y"};

    //faces contains all those faces, in that order
    private Face centerF, topF, leftF, rightF, bottomF, backF;

    private int sideLength;

    //~~~~~~~~~~CONSTRUCTORS
    public RubiksCube(int newSideLength) {
        centerF = new Face(COLORS[0], newSideLength);
        topF = new Face(COLORS[1], newSideLength);
        leftF = new Face(COLORS[2], newSideLength);
        rightF = new Face(COLORS[3], newSideLength);
        bottomF = new Face(COLORS[4], newSideLength);
        backF = new Face(COLORS[5], newSideLength);
        
        sideLength = newSideLength;
    }

    //~~~~~~~~~~METHODS
    //~~~Turns (i is counter-clockwise, 2 is double)
    public void turn(String orient) {
        String tail = "";
        if (orient.length() > 1) {
            tail = orient.substring(1);
            orient = orient.substring(0, 1);
        }
        
        switch (orient) {
        case "f":
            break;
        case "u": rot("xi");
            break;
        case "l": rot("yi");
            break;
        case "r": rot("y");
            break;
        case "d": rot("x");
            break;
        case "b": rot("y2");
            break;
        default: throw new IllegalArgumentException("Invalid orient: " + orient);
        }

        switch(tail) {
        case "i": fTurn();
        case "2": fTurn();
        default: fTurn();
        }
        
        switch (orient) {
        case "f":
            break;
        case "u": rot("x");
            break;
        case "l": rot("y");
            break;
        case "r": rot("yi");
            break;
        case "d": rot("xi");
            break;
        case "b": rot("y2");
            break;
        }
    }
    public void fTurn() {
        centerF.rearrange("r");

        leftF.rearrange("r");
        rightF.rearrange("l");
        bottomF.rearrange("b");

        int line = sideLength - 1;
        String[] lineTemp = topF.colors[line];
        topF.colors[line] = leftF.colors[line];
        leftF.colors[line] = bottomF.colors[line];
        bottomF.colors[line] = rightF.colors[line];
        rightF.colors[line] = lineTemp;
        
        leftF.rearrange("l");
        rightF.rearrange("r");
        bottomF.rearrange("b");
    }
    
    //~~~Cube Rotations (i is counter-clockwise, 2 is double)
    public void rot(String orient) {
        switch(orient) {
        case "xi": xRot();
        case "x2": xRot();
        case "x": xRot();
            break;
        case "yi": yRot();
        case "y2": yRot();
        case "y": yRot();
            break;
        case "zi": zRot();
        case "z2": zRot();
        case "z": zRot();
            break;
        default: throw new IllegalArgumentException("Invalid orient: " + orient);
        }
    }
    public void xRot() {
        Face temp = topF;
        topF = centerF;
        centerF = bottomF;
        backF.rearrange("b");
        bottomF = backF;
        temp.rearrange("b");
        backF = temp;

        rightF.rearrange("r");
        leftF.rearrange("l");
    }
    public void yRot() {
        Face temp = leftF;
        leftF = centerF;
        centerF = rightF;
        rightF = backF;
        backF = temp;

        topF.rearrange("r");
        bottomF.rearrange("l");
    }
    public void zRot() {
        rot("yi");
        xRot();
        yRot();
    }
    //toString should output (w/ sideLength of 3):
    //   top
    //   top
    //   top
    //lefcenrigbac
    //lefcenrigbac
    //lefcenrigbac
    //   bot
    //   bot
    //   bot
    public String toString() {
        String[] lines = new String[3 * sideLength];

        //https://stackoverflow.com/questions/2255500/can-i-multiply-strings-in-java-to-repeat-sequence
        String spaces = new String(new char[sideLength]).replace("\0", " ");
        
        for (int i = 0; i < sideLength; i ++) {
            lines[i] = spaces + toStringLine(topF, i);
            lines[i + sideLength] = toStringLine(leftF, i) + toStringLine(centerF, i) +
                toStringLine(rightF, i) + toStringLine(backF, i);
            lines[i + 2 * sideLength] = spaces + toStringLine(bottomF, i);
        }
        
        String retStr = "";
        for (String s : lines) {
            retStr += s + "\n";
        }
        return retStr;
    }
    public String toStringLine(Face f, int line) {
        String retStr = "";
        for (String s : f.colors[line]) {
            retStr += s;
        }
        return retStr;
    }

    public boolean equals(RubiksCube rc) {
        return (centerF.equals(rc.centerF) &&
                topF.equals(rc.topF) &&
                leftF.equals(rc.leftF) &&
                rightF.equals(rc.rightF) &&
                bottomF.equals(rc.bottomF) &&
                backF.equals(rc.backF));
    }
    
    //~~~~~~~~~~MAIN
    public static void main(String[] args) {
        RubiksCube r = new RubiksCube(3);
        System.out.println(r);

        r.rot("z");
        System.out.println(r);
        /*
        r.turn("f");
        System.out.println(r);
        r.turn("r");
        System.out.println(r);
        r.turn("l");
        System.out.println(r);
        r.turn("b");
        System.out.println(r);
        r.rot("x");
        System.out.println(r);
        r.turn("u");
        System.out.println(r);
        r.turn("d");
        System.out.println(r);
        */

    }
}
