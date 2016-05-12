package com.eaglesakura.thread;

public class DoubleHolder {
    public double value;

    public DoubleHolder(double value) {
        this.value = value;
    }

    public DoubleHolder() {
    }

    public double add(double v) {
        value += v;
        return value;
    }
}
