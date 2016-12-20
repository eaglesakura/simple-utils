package com.eaglesakura.thread;

import com.eaglesakura.lambda.ResultAction1;

/**
 * 遅延初期化を行なうオブジェクト
 *
 * シングルトン等の保持に利用する
 */
public class LazyObjectHolder1<T, A1> {
    T mObject;

    ResultAction1<A1, T> mCreator;

    public LazyObjectHolder1(ResultAction1<A1, T> creator) {
        mCreator = creator;
    }

    public T get(A1 arg) {
        if (mObject == null) {
            synchronized (this) {
                if (mObject == null) {
                    try {
                        mObject = mCreator.action(arg);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return mObject;
    }
}
