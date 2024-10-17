package Cipher;

import java.util.*;

public class CaesarShift extends Substitution {

    public CaesarShift(int shift) throws IllegalArgumentException {
        if (shift <= 0) {
            throw new IllegalArgumentException();
        }
        String shifter = createShifter(shift);
        super.setShifter(shifter);
    }

    private String createShifter(int shift) {
        Queue<Character> alphaQueue = new LinkedList<>();
        String shifter = "";

        for (int i = 0; i < Cipher.TOTAL_CHARS; i++) {
            alphaQueue.add((char) (i + Cipher.MIN_CHAR));
        }
        for (int i = 0; i < shift; i++) {
            char temp = alphaQueue.remove();
            alphaQueue.add(temp);
        }
        for (char letter : alphaQueue) {
            shifter += letter;
        }

        return shifter;
    }
}