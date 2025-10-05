package com.bwxor.portix.util;

public class ByteParser {
    public static boolean tryParse(String input) {
        int parseResult;

        try {
            parseResult = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        return parseResult >= 0 && parseResult <= 255;
    }
}
