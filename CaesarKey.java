import java.util.*;

public class CaesarKey extends Substitution {

    public CaesarKey(String key) throws IllegalArgumentException {
        if (key.isEmpty() || super.shifterCheck(key)) {
            throw new IllegalArgumentException();
        }
        String shifter = createShifter(key);
        super.setShifter(shifter);
    }

    private String createShifter(String key) {
        Queue<Character> alphaQueue = new LinkedList<>();
        Queue<Character> auxQ = new LinkedList<>();
        String shifter = "";

        for (int i = 0; i < TOTAL_CHARS; i++) {
            alphaQueue.add((char) (i + MIN_CHAR));
        }

        for (int i = 0; i < key.length(); i++) {
            int alphaSize = alphaQueue.size();
            for (int j = 0; j < alphaSize; j++) {
                char letter = alphaQueue.remove();
                if (letter == key.charAt(i)) {
                    auxQ.add(letter);
                }
                else {
                    alphaQueue.add(letter);
                }
            }
        }

        while (!alphaQueue.isEmpty()) {
            auxQ.add(alphaQueue.remove());
        }

        for (char letter : auxQ) {
            shifter += letter;
        }

        return shifter;
    }
}