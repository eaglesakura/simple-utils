package com.eaglesakura.util;

/**
 * Runnableをラップし、例外と戻り値を利用できるようにする。
 */
public class ThrowableRunner<ResultType, ErrorType extends Throwable> implements Runnable {
    /**
     * 投げられた例外
     */
    private ErrorType mError;

    /**
     * 処理の戻り値
     */
    private ResultType mResult;

    private ThrowableRunnable<ResultType, ErrorType> mRunner;

    public ThrowableRunner(ThrowableRunnable<ResultType, ErrorType> runner) {
        mRunner = runner;
    }

    /**
     * 処理を実行させる
     */
    @Override
    public final void run() {
        try {
            mResult = mRunner.run();
        } catch (Throwable e) {
            mError = (ErrorType) e;
        }
    }

    /**
     * 値を取得するか例外を投げる
     */
    public ResultType await() throws ErrorType {
        while (mResult == null) {
            if (mError != null) {
                throw mError;
            }

            Util.sleep(1);
        }
        return mResult;
    }

    /**
     * 値を取得するか例外を投げる
     */
    public ResultType getOrThrow() throws ErrorType {
        if (mError != null) {
            throw mError;
        }
        return mResult;
    }
}
