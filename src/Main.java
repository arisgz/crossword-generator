import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    private final static int MAX_VARIATIONS = 30000;
    private final static String CONF_FILE = "words.conf";

    public static void main(String[] args) throws IOException {
        File file = new File(CONF_FILE);
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Touched " + CONF_FILE + " please fill it with your words");
            return;
        }
        String[] words = wordsFromFile(file);

        Arrays.sort(words, (s1, s2) -> s2.length() - s1.length());

        CrossWord crossword = new CrossWord();

        crossword.generate(words);

        CrossWord best = crossword;
        double bestScore = best.getBoardScore();

        List<String[]> shuffles = shuffling(words, MAX_VARIATIONS);

        for (String[] word : shuffles) {
            crossword = new CrossWord();
            crossword.generate(word);
            double score = crossword.getBoardScore();
            if (crossword.getWordsCounter() > best.getWordsCounter()) {
                best = crossword;
                bestScore = score;
            } else if (crossword.getWordsCounter() == best.getWordsCounter() && score > bestScore) {
                best = crossword;
                bestScore = score;
            }
        }

        crossword = best;

//        crossword.showSolution();
        crossword.createSolutionImage();
        crossword.createEmptyImage();

        // Print missing words
        List<String> w = new ArrayList<>(Arrays.asList(words));
        w.removeAll(crossword.getWords());

        if (w.isEmpty()) {
            System.out.println("Every word is in the crossword");
        } else {
            System.out.println("Missing words");
        }
        for (String s : w) {
            System.out.println(s);
        }

    }

    private static String[] wordsFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<String> w = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                w.add(line.trim().toUpperCase());
            }
        }
        String[] words = new String[w.size()];
        w.toArray(words);
        return words;
    }

    private static int factorial(int n) {
        if (n == 1) {
            return 1;
        } else {
            int res = n * factorial(n - 1);
            return res < 1 ? -1 : res;
        }
    }

    public static List<String[]> shuffling(String[] words, int maxVariations) {
        int variation = factorial(words.length);
        variation = variation < 1 || variation > maxVariations ? maxVariations : variation;

        List<String[]> res = new ArrayList<>();
        int count = 0;
        while (count < variation) {
            String[] temp = Arrays.copyOf(words, words.length);
            shuffle(temp);

            if (!contains(res, temp)) {
                res.add(temp);
                count++;
            }
        }
        return res;
    }

    private static boolean contains(List<String[]> list, String[] arr) {
        for (String[] el : list) {
            if (Arrays.equals(el, arr)) {
                return true;
            }
        }
        return false;
    }

    public static void shuffle(String[] arr) {
        Random r;
        try {
            r = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < arr.length; i++) {
            int i1 = r.nextInt(arr.length);
            int i2 = r.nextInt(arr.length);
            String temp = arr[i1];
            arr[i1] = arr[i2];
            arr[i2] = temp;
        }
    }


}

