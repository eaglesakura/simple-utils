package com.eaglesakura.thread;

public class LongHolder {
    public long value;

    public LongHolder(long value) {
        this.value = value;
    }

    public LongHolder() {
    }

    public long add(long v) {
        value += v;
        return value;
    }
}
