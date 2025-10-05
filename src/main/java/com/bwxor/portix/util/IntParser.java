package com.bwxor.portix.util;

public class IntParser {
    public static boolean tryParse(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
