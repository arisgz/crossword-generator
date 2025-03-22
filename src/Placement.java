public class Placement {
    private char[] word;
    private int x;
    private int y;
    private Direction direction;

    public Placement(char[] word, int x, int y, Direction direction) {
        this.word = word;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Placement(String word, int x, int y, Direction direction) {
        this.word = word.toCharArray();
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public char[] getWord() {
        return word;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int size() {
        return word.length;
    }

    public Direction getDirection() {
        return direction;
    }
}
