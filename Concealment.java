import java.util.*;

public class Concealment extends Substitution {
    private int filler;

    public Concealment(int filler) throws IllegalArgumentException {
        if (filler <= 0) {
            throw new IllegalArgumentException();
        }
        this.filler = filler;
    }

    public String encrypt(String input) {
        String output = "";
        Random rand = new Random();
        for (int i = 0; i < input.length(); i++) {
            for (int j = 0; j < filler; j++) {
                // Code from: https://mkyong.com/java/java-generate-random-integers-in-a-range/
                output += rand.nextInt(TOTAL_CHARS + MIN_CHAR);
            }
            output += input.charAt(i);
        }
        return output;
    }

    public String decrypt(String input) {
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            if (i % filler == 0) {
                output += input.charAt(i);
            }
        }
        return output;  
    }
}
