package com.bwxor.portix.service.parser;

public class ByteParsingService {
    public boolean tryParse(String input) {
        int parseResult;

        try {
            parseResult = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        return parseResult >= 0 && parseResult <= 255;
    }
}
