package com.eaglesakura.thread;

import com.eaglesakura.lambda.ResultAction2;

/**
 * 遅延初期化を行なうオブジェクト
 *
 * シングルトン等の保持に利用する
 */
public class LazyObjectHolder2<T, A1, A2> {
    T mObject;

    ResultAction2<A1, A2, T> mCreator;

    public LazyObjectHolder2(ResultAction2<A1, A2, T> creator) {
        mCreator = creator;
    }

    public T get(A1 arg1, A2 arg2) {
        if (mObject == null) {
            synchronized (this) {
                if (mObject == null) {
                    try {
                        mObject = mCreator.action(arg1, arg2);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return mObject;
    }
}
