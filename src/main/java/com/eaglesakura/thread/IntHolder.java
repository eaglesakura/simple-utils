package com.eaglesakura.thread;

public class IntHolder {
    public int value;

    public IntHolder(int value) {
        this.value = value;
    }

    public IntHolder() {
    }

    public int add(int v) {
        value += v;
        return value;
    }
}
