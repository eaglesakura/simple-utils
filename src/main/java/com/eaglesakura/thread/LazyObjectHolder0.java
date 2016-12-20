package com.eaglesakura.thread;

import com.eaglesakura.lambda.ResultAction0;

/**
 * 遅延初期化を行なうオブジェクト
 *
 * シングルトン等の保持に利用する
 */
public class LazyObjectHolder0<T> {
    T mObject;

    ResultAction0<T> mCreator;

    public LazyObjectHolder0(ResultAction0<T> creator) {
        mCreator = creator;
    }

    public T get() {
        if (mObject == null) {
            synchronized (this) {
                if (mObject == null) {
                    try {
                        mObject = mCreator.action();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return mObject;
    }
}
