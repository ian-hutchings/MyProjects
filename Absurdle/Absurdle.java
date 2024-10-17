package Absurdle;// Ian Hutchings
// 11/09/2023
// CSE 122
// TA: Kyle Du

// This class is the Absurdle.Absurdle game. The user attempts to guess a word, but the correct word
// changes after each guess. The class generates a pattern of green, yellow, and gray blocks that
// represents the accuarcy of each letter in the user's guess for each possible word in the user's
// chosen dictionary. The class picks the output of blocks that corresponds to the most words and
// replaces the dictionary of words with these words, pruning the set of potential words the
// least amount possible. When the output conists of only green squares, the user has guessed the
// "correct" word and the game ends.

import java.util.*;
import java.io.*;

public class Absurdle  {
    public static final String GREEN = "🟩";
    public static final String YELLOW = "🟨";
    public static final String GRAY = "⬜";

    // [[ ALL OF MAIN PROVIDED ]]
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the game of Absurdle.Absurdle.");

        System.out.print("What dictionary would you like to use? ");
        String dictName = console.next();

        System.out.print("What length word would you like to guess? ");
        int wordLength = console.nextInt();

        List<String> contents = loadFile(new Scanner(new File(dictName)));

        Set<String> words = pruneDictionary(contents, wordLength);

        List<String> guessedPatterns = new ArrayList<>();
        while (!isFinished(guessedPatterns)) {
            System.out.print("> ");
            String guess = console.next();
            String pattern = record(guess, words, wordLength);
            guessedPatterns.add(pattern);
            System.out.println(": " + pattern);
            System.out.println();
        }
        System.out.println("Absurdle " + guessedPatterns.size() + "/∞");
        System.out.println();
        printPatterns(guessedPatterns);
    }

    // [[ PROVIDED ]]
    // Prints out the given list of patterns.
    // - List<String> patterns: list of patterns from the game
    public static void printPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            System.out.println(pattern);
        }
    }

    // [[ PROVIDED ]]
    // Returns true if the game is finished, meaning the user guessed the word. Returns
    // false otherwise.
    // - List<String> patterns: list of patterns from the game
    public static boolean isFinished(List<String> patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        String lastPattern = patterns.get(patterns.size() - 1);
        return !lastPattern.contains("⬜") && !lastPattern.contains("🟨");
    }

    // [[ PROVIDED ]]
    // Loads the contents of a given file Scanner into a List<String> and returns it.
    // - Scanner dictScan: contains file contents
    public static List<String> loadFile(Scanner dictScan) {
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }

    // Behavior:
    //   - This method takes the list of words from the chosen dictionary and adds only the words
    //     of the user's desired length to a new set.
    // Parameters:
    //   - contents: The list of words taken from a dictionary
    //   - wordLength: The user's chosen length of words to use
    // Returns:
    //   - Set<String>: The set of words of the correct length 
    // Exceptions:
    //   - wordLength < 1: if the value of wordLength is less than 1, an IllegalArgumentException
    //     will be thrown
    public static Set<String> pruneDictionary(List<String> contents, int wordLength) 
                                             throws IllegalArgumentException {
        if (wordLength < 1) {
            throw new IllegalArgumentException();
        }
        
        Set<String> wordSet = new TreeSet<>();
        for (int i = 0; i < contents.size(); i++) {
            String word = contents.get(i);
            if (word.length() == wordLength) {
                wordSet.add(word);
            }
        }
        return wordSet;
    }

    // Behavior:
    //   - This method processes the user's guess, creating patterns of blocks for all possible 
    //     correct words and finally outputting the pattern that corresponds with the greatest 
    //     number of possible correct words. It updates the set of possible words to be only
    //     the words that correspond with this outputted pattern.
    // Parameters:
    //   - guess: The word that the user has guessed
    //   - words: The set containing all possible correct words
    //   - wordLength: The user's chosen length of word to use
    // Returns:
    //   - String: The pattern that corresponds with the greatest number of possible correct words
    // Exceptions:
    //   - If the set of potential correct words is empty or if the length of the user's imputted
    //     guess does not match their desired word length, an IllegalArgumentException is thrown
    public static String record(String guess, Set<String> words, int wordLength) 
                                throws IllegalArgumentException {

        if (words.isEmpty() || !(guess.length() == wordLength)) {
            throw new IllegalArgumentException();
        }

        Map<String, TreeSet<String>> recordMap = new HashMap<>();
        Set<String> allPatterns = new TreeSet<>();
        Iterator<String> iter = words.iterator();

        while (iter.hasNext()) {
            String word = iter.next();
            String pattern = patternFor(word, guess);

            if (!recordMap.containsKey(pattern)) {
                recordMap.put(pattern, new TreeSet<String>());
            }
            recordMap.get(pattern).add(word);
            allPatterns.add(pattern);
        }

        String bestPattern = findBestPattern(allPatterns, recordMap);

        words.clear();
        for (String word : recordMap.get(bestPattern)) {
            words.add(word);
        }

        return bestPattern;
    }

    // Behavior:
    //   - This method replaces the letters of a given guessed word with a pattern of colored 
    //     blocks representing the accuracy of each letter in relation to a given correct word.
    //     A green block represents a letter that is in the right place in the word, a yellow 
    //     block represents a letter that is in the word but is not in the correct place, and a
    //     gray blocks represents a letter that is not in the word.
    // Parameters:
    //   - word: The correct word
    //   - guess: The word that the user has guessed
    // Returns:
    //   - String: the pattern of blocks corresponding to the guessed word
    public static String patternFor(String word, String guess) {
        List<String> guessList = new ArrayList<>();
        Map<Character, Integer> charCount = new HashMap<>();

        for (int i = 0; i < guess.length(); i++) {
            guessList.add(String.valueOf(guess.charAt(i)));
            char wordChar = word.charAt(i);
            if (!charCount.containsKey(wordChar)) {
                charCount.put(wordChar, 0);
            }
            charCount.put(wordChar, charCount.get(wordChar) + 1);
        }

        // Green loop
        for (int i = 0; i < guessList.size(); i++) {
            String currGuessLet = guessList.get(i);
            if (word.contains(currGuessLet)) {
                if (word.charAt(i) == currGuessLet.charAt(0)) {
                    charCount.put(currGuessLet.charAt(0), 
                        charCount.get(currGuessLet.charAt(0)) - 1);
                    guessList.set(i, GREEN);
                }
            }
        }

        // Yellow/Gray loop
        for (int i = 0; i < guessList.size(); i++) {
            String currGuessLet = guessList.get(i);
            if (!(currGuessLet.equals(GREEN))) {
                if (word.contains(currGuessLet) && charCount.get(currGuessLet.charAt(0)) > 0) {
                    charCount.put(currGuessLet.charAt(0), 
                        charCount.get(currGuessLet.charAt(0)) - 1);
                    guessList.set(i, YELLOW);
                }
                else {
                    guessList.set(i, GRAY);
                }
            }
        }

        String finalGuess = guessList.toString().replace("[", "").replace("]", "")
            .replace(",", "").replace(" ", "");
            
        return finalGuess;
    }

    // Behavior:
    //   - This method determines the pattern that has the most corresponding words.
    // Parameters:
    //   - allPatterns: The set of all possible correct words
    //   - recordMap: The map that stores all patterns and their corresponding words
    // Returns:
    //   - String: The pattern that corresponds with the greatest number of possible correct words
    public static String findBestPattern(Set<String> allPatterns, Map<String, 
                                         TreeSet<String>> recordMap) {
        int longestLength = 0;
        String bestPattern = "";
        for (String pattern : allPatterns) {
            if (recordMap.get(pattern).size() > longestLength) {
                longestLength = recordMap.get(pattern).size();
                bestPattern = pattern;
            }
        }
        return bestPattern;
    }
}
