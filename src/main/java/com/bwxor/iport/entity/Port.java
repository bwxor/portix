package com.bwxor.iport.entity;

public class Port {
    private int value;

    public Port(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
