package com.bwxor.portix.service.parser;

public class IntParsingService {
    public boolean tryParse(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
