public class Substitution extends Cipher {
    private String shifter;
    private String alpha;
    
    public Substitution() {
        this.shifter = "";
        initializeAlpha();
    }

    public Substitution(String shifter) throws IllegalArgumentException {
        if (shifter.length() != TOTAL_CHARS || shifterCheck(shifter)) {
            throw new IllegalArgumentException();
        }
        this.shifter = shifter;
        initializeAlpha();
    }

    private void initializeAlpha() {
        this.alpha = "";
        for (int i = MIN_CHAR; i <= MAX_CHAR; i++) {
            alpha += (char) i;
        }
    }

    public void setShifter(String shifter) {
        if (shifter.length() != TOTAL_CHARS || shifterCheck(shifter)) {
            throw new IllegalArgumentException();
        }
        this.shifter = shifter;
    }

    private boolean shifterCheck(String shifter) {
        boolean dup = false;
        boolean outside = false;
        for (int i = 0; i < shifter.length() - 1; i++) {
            if ((int) shifter.charAt(i) > MAX_CHAR || (int) shifter.charAt(i) < MIN_CHAR) {
                outside = true;
            }
            for (int j = i + 1; j < shifter.length() - 1; j++) {
                if (shifter.charAt(i) == shifter.charAt(j)) {
                    dup = true;
                }
            }
        }

        return (dup || outside);
    }

    public String encrypt(String input) throws IllegalStateException {
        if (shifter == null || shifter.isEmpty()) {
            throw new IllegalStateException();
        }
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            char encryptedChar = shifter.charAt(alpha.indexOf(input.charAt(i)));
            output += encryptedChar;
        }

        return output;
    }

    public String decrypt(String input) {
        if (shifter == null || shifter.isEmpty()) {
            throw new IllegalStateException();
        }
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            char decryptedChar = alpha.charAt(shifter.indexOf((input.charAt(i))));
            output += decryptedChar;
        }

        return output;
    }
}