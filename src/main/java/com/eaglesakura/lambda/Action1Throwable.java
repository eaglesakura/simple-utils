package com.eaglesakura.lambda;

public interface Action1Throwable<T, E extends Throwable> {
    void action(T it) throws E;
}
