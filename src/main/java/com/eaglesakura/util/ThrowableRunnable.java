package com.eaglesakura.util;

public interface ThrowableRunnable<ResultType, ErrorType extends Throwable> {
    ResultType run() throws ErrorType;
}
