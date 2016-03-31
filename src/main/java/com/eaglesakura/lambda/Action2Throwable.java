package com.eaglesakura.lambda;

public interface Action2Throwable<T, T2, E extends Throwable> {
    void action(T it, T2 obj2) throws E;
}
