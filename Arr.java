public final class Arr {
    public static boolean in(String[] arr, String s) {
        //https://stackoverflow.com/questions/4962361/where-is-javas-array-indexof
        return java.util.Arrays.asList(arr).indexOf(s) != -1;
    }

    public static String chooseRandom(String[] arr) {
        int random = (int)(Math.random() * arr.length);
        return arr[random];
    }
}
