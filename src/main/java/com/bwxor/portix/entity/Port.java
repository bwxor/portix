package com.bwxor.portix.entity;

public class Port {
    private int value;

    public Port(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }
}
