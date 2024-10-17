package Cipher;

import java.util.*;

public class MultiCipher extends Substitution {
    private List<Cipher> ciphers;

    public MultiCipher(List<Cipher> ciphers) throws IllegalArgumentException {
        if (ciphers == null) {
            throw new IllegalArgumentException();
        }
        this.ciphers = ciphers;
    }

    public String encrypt(String input) {
        String output = input;
        for (int i = 0; i < ciphers.size(); i++) {
            output = ciphers.get(i).encrypt(input);
            input = output;
        }
        return output;
    }

    public String decrypt(String input) {
        String output = "";
        for (int i = ciphers.size() - 1; i >= 0; i--) {
            output = ciphers.get(i).decrypt(input);
            input = output;
        }
        return output;
    }
}
