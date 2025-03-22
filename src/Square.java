public class Square {
    private final char ch;
    private Direction direction;
    public boolean end = false;
    public boolean start = false;
    private boolean intersection = false;


    public Square(char ch, Direction direction) {
        this.ch = ch;
        this.direction = direction;
    }

    public char getChar() {
        return ch;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void makeIntersection() {
        intersection = true;
    }

    public void notIntersection() {
        intersection = false;
    }

    public boolean isIntersection() {
        return intersection;
    }

}
