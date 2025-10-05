package com.bwxor.portix.entity;

import com.bwxor.portix.exception.IPAddressBuildException;
import com.bwxor.portix.util.ByteParser;

import java.util.Arrays;
import java.util.Objects;

public class IPAddress {
    /**
     * Characters are used instead of bytes, because Java bytes are signed and they only go until 127
     */
    private char[] bytes;

    public IPAddress(String address) {
        bytes = splitStringIntoByteArray(address);
    }

    private char[] splitStringIntoByteArray(String input) {
        String[] elements = input.split("\\.");

        if (elements.length != 4) {
            throw new IPAddressBuildException("String is not delimited by 4 dots.");
        }

        if (Arrays.stream(elements).anyMatch(e -> !ByteParser.tryParse(e))) {
            throw new IPAddressBuildException("Parts of the IP Address need to be bytes.");
        }

        char[] output = new char[4];
        output[0] = (char) Integer.parseInt(elements[0]);
        output[1] = (char) Integer.parseInt(elements[1]);
        output[2] = (char) Integer.parseInt(elements[2]);
        output[3] = (char) Integer.parseInt(elements[3]);

        return output;
    }

    public char[] getBytes() {
        return bytes;
    }

    public void increment() {
        boolean incrementNext = true;

        for (int i = 3; i > 0; i--) {
            if (incrementNext) {
                if (bytes[i] >= 255) {
                    bytes[i] = 0;
                } else {
                    bytes[i]++;
                    incrementNext = false;
                }
            }
        }
    }

    public long diff(IPAddress target) {
        long sourceAbs = (long) bytes[0] * 256 * 256 * 256
                + (long) bytes[1] * 256 * 256
                + (long) bytes[2] * 256
                + (long) bytes[3];
        long targetAbs = (long) target.getBytes()[0] * 256 * 256 * 256
                + (long) target.getBytes()[1] * 256 * 256
                + (long) target.getBytes()[2] * 256
                + (long) target.getBytes()[3];

        return sourceAbs - targetAbs;
    }

    public boolean before(IPAddress target) {
        return diff(target) < 0;
    }

    public boolean after(IPAddress target) {
        return diff(target) > 0;
    }

    @Override
    public String toString() {
        return (int) bytes[0] + "." + (int) bytes[1] + "." + (int) bytes[2] + "." + (int) bytes[3];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IPAddress target = (IPAddress) o;
        return diff(target) == 0;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
