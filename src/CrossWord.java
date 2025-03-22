import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class CrossWord {
    private static final int CROSSWORD_DIMENSION = 100;
    private static final int SQUARE_DIMENSION = 40;

    private final Square[][] crossword;
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private final Stack<Position> tempPlace;
    private final List<String> words;

    public CrossWord() {
        crossword = new Square[CROSSWORD_DIMENSION][CROSSWORD_DIMENSION];
        for (int i = 0; i < CROSSWORD_DIMENSION; i++) {
            for (int j = 0; j < CROSSWORD_DIMENSION; j++) {
                crossword[i][j] = null;
            }
        }
        xStart = CROSSWORD_DIMENSION + 1;
        yStart = CROSSWORD_DIMENSION + 1;
        xEnd = -1;
        yEnd = -1;
        tempPlace = new Stack<>();
        words = new ArrayList<>();
    }

    private int getColumns() {
        return xEnd - xStart + 1;
    }

    private int getRows() {
        return yEnd - yStart + 1;
    }

    private void place(Placement placement) {
        if (placement.getDirection() == Direction.HORIZONTAL) {
            placeHorizontal(placement.getWord(), placement.getX(), placement.getY());
        } else {
            placeVertical(placement.getWord(), placement.getX(), placement.getY());
        }

        crossword[placement.getY()][placement.getX()].start = true;

        UpdateMeasure(placement);
//        updateMeasureIterative();
        words.add(new String(placement.getWord()));
    }

    private void placeHorizontal(char[] word, int x, int y) {
        for (char letter : word) {
            if (crossword[y][x] == null) {
                crossword[y][x] = new Square(letter, Direction.HORIZONTAL);
            } else {
                crossword[y][x].makeIntersection();
            }
            x += 1;
        }
        crossword[y][x - 1].end = true;
    }

    private void placeVertical(char[] word, int x, int y) {
        for (char letter : word) {
            if (crossword[y][x] == null) {
                crossword[y][x] = new Square(letter, Direction.VERTICAL);
            } else {
                crossword[y][x].makeIntersection();
            }
            y += 1;
        }
        crossword[y - 1][x].end = true;
    }

    public void showSolution() {
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice[] gs = ge.getScreenDevices();
//        JFrame frame = new JFrame(gs[0].getDefaultConfiguration());

        JFrame frame = new JFrame();
        frame.setTitle("Crossword");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(getColumns() * SQUARE_DIMENSION, getRows() * SQUARE_DIMENSION);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(getRows(), getColumns()));

        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                JLabel label;
                if (crossword[i][j] != null) {
                    label = new JLabel(crossword[i][j].getChar() + "", SwingConstants.CENTER);
                } else {
                    label = new JLabel(" ");
                    label.setBackground(Color.BLACK);
                    label.setOpaque(true);
                }
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(label);
            }
        }

        frame.add(panel);

        frame.setVisible(true);
    }

    private void printCrosswordToGraphics(Graphics2D g2d) {

    }

    public void createEmptyImage() {
        BufferedImage image = new BufferedImage(getColumns() * SQUARE_DIMENSION,
                getRows() * SQUARE_DIMENSION, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                int x = (j - xStart) * SQUARE_DIMENSION;
                int y = (i - yStart) * SQUARE_DIMENSION;

                // Draw the square border (empty or with background)
                if (crossword[i][j] == null) {
                    // Empty square, fill it with black
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, SQUARE_DIMENSION, SQUARE_DIMENSION);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(x, y, SQUARE_DIMENSION, SQUARE_DIMENSION);
                }

                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, SQUARE_DIMENSION, SQUARE_DIMENSION);
            }
        }

        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File("empty.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSolutionImage() {
        BufferedImage image = new BufferedImage(getColumns() * SQUARE_DIMENSION,
                getRows() * SQUARE_DIMENSION, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                int x = (j - xStart) * SQUARE_DIMENSION;
                int y = (i - yStart) * SQUARE_DIMENSION;

                // Draw the square border (empty or with background)
                if (crossword[i][j] == null) {
                    // Empty square, fill it with black
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, SQUARE_DIMENSION, SQUARE_DIMENSION);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(x, y, SQUARE_DIMENSION, SQUARE_DIMENSION);
                }

                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, SQUARE_DIMENSION, SQUARE_DIMENSION);

                if (crossword[i][j] != null) {
                    String letter = "" + crossword[i][j].getChar();
                    FontMetrics fm = g2d.getFontMetrics();
                    int letterWidth = fm.stringWidth(letter);
                    int letterHeight = fm.getHeight();

                    int letterX = x + (SQUARE_DIMENSION - letterWidth) / 2;
                    int letterY = y + (SQUARE_DIMENSION + letterHeight) / 2 - fm.getDescent();
                    g2d.drawString(letter, letterX, letterY);
                }
            }
        }

        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File("solution.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void print() {
        for (int i = 0; i < getColumns() * 2 + 1; i++) {
            System.out.print("-");
        }
        System.out.println();

        for (int i = yStart; i <= yEnd; i++) {
            System.out.print("|");
            for (int j = xStart; j <= xEnd; j++) {
                if (crossword[i][j] != null) {
                    System.out.print(crossword[i][j].getChar());
                } else {
                    System.out.print(' ');
                }
                System.out.print("|");
            }
            System.out.println();

            for (int k = 0; k < getColumns() * 2 + 1; k++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    private void UpdateMeasure(Placement placement) {
        if (placement.getX() < xStart) {
            xStart = placement.getX();
        }
        if (placement.getY() < yStart) {
            yStart = placement.getY();
        }
        if (placement.getDirection() == Direction.HORIZONTAL) {
            if (placement.getX() + placement.size() - 1 > xEnd) {
                xEnd = placement.getX() + placement.size() - 1;
            }
            if (placement.getY() > yEnd) {
                yEnd = placement.getY();
            }
        }
        if (placement.getDirection() == Direction.VERTICAL) {
            if (placement.getY() + placement.size() - 1 > yEnd) {
                yEnd = placement.getY() + placement.size() - 1;
            }
            if (placement.getX() > xEnd) {
                xEnd = placement.getX();
            }
        }
    }


    private void updateMeasureIterative() {
        xStart = CROSSWORD_DIMENSION + 1;
        yStart = CROSSWORD_DIMENSION + 1;
        xEnd = -1;
        yEnd = -1;

        for (int i = 0; i < CROSSWORD_DIMENSION; i++) {
            for (int j = 0; j < CROSSWORD_DIMENSION; j++) {
                if (crossword[i][j] != null) {
                    if (j < xStart) xStart = j;
                    if (j > xEnd) xEnd = j;
                    if (i < yStart) yStart = i;
                    if (i > yEnd) yEnd = i;
                }
            }
        }
    }


    private List<Placement> findPlacement(char[] word, int x, int y) {
        List<Integer> offsets = new ArrayList<>();
        for (int i = 0; i < word.length; i++) {
            if (word[i] == crossword[y][x].getChar()) {
                offsets.add(i);
            }
        }

        List<Placement> placements = new ArrayList<>();
        for (int offset : offsets) {
            int xi = x - offset;
            int yi = y - offset;
            if (canPlaceHorizontally(word, xi, y) && checkMarginHorizontal(word, x, y, xi)) {
                placements.add(new Placement(word, xi, y, Direction.HORIZONTAL));
            } else if (canPlaceVertically(word, x, yi) && checkMarginVertical(word, x, y, yi)) {
                placements.add(new Placement(word, x, yi, Direction.VERTICAL));
            }
        }

        return placements;
    }

    private boolean canPlaceVertically(char[] word, int x, int yi) {
        for (int i = 0; i < word.length; i++) {
            if (crossword[yi + i][x] != null && crossword[yi + i][x].getChar() != word[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean checkMarginVertical(char[] word, int x, int y, int yi) {
        if (crossword[yi - 1][x] != null || crossword[yi + word.length][x] != null) {
            return false;
        }

        for (int i = 0; i < word.length; i++) {
            if (((crossword[yi + i][x - 1] != null && crossword[yi + i][x - 1].getDirection() != Direction.VERTICAL && crossword[yi + i][x - 1].end) ||
                    (crossword[yi + i][x - 1] != null && crossword[yi + i][x - 1].getDirection() == Direction.VERTICAL))
                    || ((crossword[yi + i][x + 1] != null && crossword[yi + i][x + 1].getDirection() != Direction.VERTICAL && crossword[yi + i][x + 1].start) ||
                    (crossword[yi + i][x + 1] != null && crossword[yi + i][x + 1].getDirection() == Direction.VERTICAL))) {
                return false;
            }
        }

        return true;
    }

    private boolean checkMarginHorizontal(char[] word, int x, int y, int xi) {
        if (crossword[y][xi - 1] != null || crossword[y][xi + word.length] != null) {
            return false;
        }


        for (int i = 0; i < word.length; i++) {
            if (((crossword[y - 1][xi + i] != null && crossword[y - 1][xi + i].getDirection() != Direction.HORIZONTAL && crossword[y - 1][xi + i].end) ||
                    (crossword[y - 1][xi + i] != null && crossword[y - 1][xi + i].getDirection() == Direction.HORIZONTAL))
                    || ((crossword[y + 1][xi + i] != null && crossword[y + 1][xi + i].getDirection() != Direction.HORIZONTAL && crossword[y + 1][xi + i].start) ||
                    (crossword[y + 1][xi + i] != null && crossword[y + 1][xi + i].getDirection() == Direction.HORIZONTAL))) {


                return false;
            }
        }

        return true;
    }

    private boolean canPlaceHorizontally(char[] word, int xi, int y) {
        for (int i = 0; i < word.length; i++) {
            if (crossword[y][xi + i] != null && crossword[y][xi + i].getChar() != word[i]) {
                return false;
            }
        }
        return true;
    }

    private void temporaryPlace(Placement placement) {
        if (placement.getDirection() == Direction.HORIZONTAL) {
            temporaryPlaceHorizontal(placement.getWord(), placement.getX(), placement.getY());
        } else {
            temporaryPlaceVertical(placement.getWord(), placement.getX(), placement.getY());
        }

        UpdateMeasure(placement);
    }

    private void temporaryPlaceHorizontal(char[] word, int x, int y) {
        for (char letter : word) {
            if (crossword[y][x] == null) {
                crossword[y][x] = new Square(letter, Direction.HORIZONTAL);
            } else {
                crossword[y][x].makeIntersection();
            }
            tempPlace.push(new Position(x, y));
            x += 1;
        }
    }

    private void temporaryPlaceVertical(char[] word, int x, int y) {
        for (char letter : word) {
            if (crossword[y][x] == null) {
                crossword[y][x] = new Square(letter, Direction.VERTICAL);
            } else {
                crossword[y][x].makeIntersection();
            }
            tempPlace.push(new Position(x, y));
            y += 1;
        }
    }

    private void removeTemporary(Placement placement) {
        int x = placement.getX();
        int y = placement.getY();
        for (int i = 0; i < placement.size(); i++) {
            if (crossword[y][x].isIntersection()) {
                crossword[y][x].notIntersection();
            } else {
                crossword[y][x] = null;
            }

            if (placement.getDirection() == Direction.HORIZONTAL) {
                x += 1;
            } else {
                y += 1;
            }

        }
//        if (placement.getDirection() == Direction.HORIZONTAL) {
//            x -= 1;
//        } else {
//            y -= 1;
//        }

//        if (placement.getX() == xStart || placement.getY() == yStart || x == xEnd || y == yEnd) {
//            updateMeasureIterative();
//        }

    }

    private double getPlacementScore(Placement placement) {
        int oldXStart = xStart;
        int oldXEnd = xEnd;
        int oldYStart = yStart;
        int oldYEnd = yEnd;

        temporaryPlace(placement);
        double score = getBoardScore();
        removeTemporary(placement);

        xStart = oldXStart;
        yStart = oldYStart;
        xEnd = oldXEnd;
        yEnd = oldYEnd;
        return score;
    }

    public double getBoardScore() {
        double dimScore = (double) getRows() / getColumns();
        if (getRows() > getColumns()) {
            dimScore = (double) getColumns() / getRows();
        }

        Stats stats = getStats();


//        System.out.println("both " + (double) both / getWordsCounter());
//        System.out.println("filled " + (double) filled / empty);
//        System.out.println("dim " + dimScore);
//        System.out.println("both = " + both);
//        System.out.println("words = " + getWordsCounter());
//        System.out.println();

        return dimScore + (double) stats.filled / (stats.empty + 1) + (double) stats.intersection / getWordsCounter() * 1.25;
    }

    public void generate(String[] words) {
//        Arrays.sort(words, (s1, s2) -> s2.length() - s1.length());
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].toUpperCase();
        }
        place(new Placement(words[0], CROSSWORD_DIMENSION / 2, CROSSWORD_DIMENSION / 2, Direction.HORIZONTAL));

        for (int i = 1; i < words.length; i++) {
            Stack<Placement> placements = new Stack<>();
            char[] word = words[i].toCharArray();

            for (char letter : word) {
                for (int y = yStart; y <= yEnd; y++) {
                    for (int x = xStart; x <= xEnd; x++) {
                        if (crossword[y][x] != null && crossword[y][x].getChar() == letter) {
                            List<Placement> p = findPlacement(word, x, y);
                            placements.addAll(p);
                        }
                    }
                }
            }

            double bestScore = -1;
            Placement bestPlacement = null;

            while (!placements.isEmpty()) {
                Placement placement = placements.pop();
                double score = getPlacementScore(placement);
                if (score > bestScore) {
                    bestScore = score;
                    bestPlacement = placement;
                }
            }

            if (bestPlacement != null) {
                place(bestPlacement);
            }
        }
    }

    private Stats getStats() {
        int filled = 0;
        int empty = 0;
        int intersection = 0;
        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                if (crossword[i][j] == null) {
                    empty++;
                } else {
                    filled++;
                    if (crossword[i][j].isIntersection()) {
                        intersection++;
                    }
                }
            }
        }

        return new Stats(filled, empty, intersection);
    }

    public void printStats() {
        System.out.println("Rows: " + getRows());
        System.out.println("Columns: " + getColumns());
        System.out.println("Words[" + getWordsCounter() + "]: " + Arrays.toString(words.toArray()));

        Stats stats = getStats();

        System.out.println("Filled: " + stats.filled);
        System.out.println("Empty: " + stats.empty);
        System.out.println("Intersection: " + stats.intersection);
    }

    public int getWordsCounter() {
        return words.size();
    }

    public List<String> getWords() {
        return words;
    }
}

class Stats {
    int filled;
    int empty;
    int intersection;

    public Stats(int filled, int empty, int intersection) {
        this.filled = filled;
        this.empty = empty;
        this.intersection = intersection;
    }
}
