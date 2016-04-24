package com.eaglesakura.lambda;

public class CallbackUtils {
    public static boolean isCanceled(CancelCallback callback) {
        if (callback == null) {
            return false;
        }
        try {
            return callback.isCanceled();
        } catch (Throwable e) {
            return true;
        }
    }
}
