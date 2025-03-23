import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String[] words = new String[]{"ZORZI", "RUCOLA", "LIFA", "STO CAZZO", "TFIIH", "BUCCHI",
                "PIANTO", "FANTASTICA", "PARCHEGGIO A L", "PIOVE", "OFELE", "CAZZI", "SAN SIMONE", "PERRO",
                "IMMUNOLOGIA", "RADICAL CHIC", "tappi", "king"};

        Arrays.sort(words, (s1, s2) -> s2.length() - s1.length());


        CrossWord crossword = new CrossWord();

        crossword.generate(words);

        List<CrossWord> res = new ArrayList<>();
        res.add(crossword);

        List<String[]> shuffles = shuffling(words, 30000);

        for (String[] word : shuffles) {
            crossword = new CrossWord();
            crossword.generate(word);
            res.add(crossword);
        }

        double bestScore = -1;
        for (CrossWord cw : res) {
            double score = cw.getBoardScore();
            if (cw.getWordsCounter() > crossword.getWordsCounter()) {
                crossword = cw;
                bestScore = score;
            } else if (cw.getWordsCounter() == crossword.getWordsCounter() && score > bestScore) {
                bestScore = score;
                crossword = cw;
            }
        }

        crossword.showSolution();
        crossword.createSolutionImage();
        crossword.createEmptyImage();
//        crossword.printStats();

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

    public static List<String[]> shuffling(String[] words, int variation) {
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
        Random r = null;
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

