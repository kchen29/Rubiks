import java.util.Arrays;

public class Face {
    
    //~~~~~~~~~~INSTANCE VARIABLES
    // color represents, from top left to right, middle left to right, bottom left to right
    public String[][] colors;

    private int sideLength;
    
    //~~~~~~~~~~CONSTRUCTORS
    public Face(String color, int newSideLength) {
        colors = new String[newSideLength][newSideLength];
        for (int i = 0; i < newSideLength; i++) {
            for (int j = 0; j < newSideLength; j++) {
                colors[i][j] = color;
            }
        }
        sideLength = newSideLength;
    }

    //~~~~~~~~~~METHODS
    public void rearrange(String orient) {
        switch (orient) {
        case "l": case "r": case "b": break;
        default: throw new IllegalArgumentException("Invalid orient: " + orient);
        }
        
        String[][] newColors = new String[sideLength][sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                switch (orient) {
                case "l": newColors[i][j] = colors[j][(sideLength - 1) - i];
                    break;
                case "r": newColors[i][j] = colors[(sideLength - 1) - j][i];
                    break;
                case "b": newColors[i][j] = colors[(sideLength - 1) - i][(sideLength - 1) - j];
                    break;
                }
            }
        }
        
        colors = newColors;
    }
    
    public String toString() {
        String retStr = "";
        for (String[] line : colors) {
            for (String c : line) {
                retStr += c;
            }
            retStr += "\n";
        }
        return retStr;
    }

    public boolean equals(Face f) {
        return Arrays.deepEquals(colors, f.colors);
    }
    
    //~~~~~~~~~~MAIN
    public static void main(String[] args) {
        Face f = new Face("w", 3);
        System.out.println(f);
        
        Face f2 = new Face("b", 3);
        System.out.println(f.equals(f2));
        
        /*
          String[][] newColors = {
          {"w", "r", "g"},
          {"b", "o", "r"},
          {"w", "g", "o"}
        };
        
        f.colors = newColors;
        System.out.println(f);
        f.rearrange("l");
        System.out.println(f);
        
        f.colors = newColors;
        f.rearrange("r");
        System.out.println(f);
        f.rearrange("l");
        System.out.println(f);
        
        f.colors = newColors;
        f.rearrange("b");
        System.out.println(f);
        f.rearrange("b");
        System.out.println(f);
        */
        
    }
    //*/
}
