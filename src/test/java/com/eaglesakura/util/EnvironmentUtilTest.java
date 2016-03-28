package com.eaglesakura.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class EnvironmentUtilTest {

    @Test
    public void 通常のJava環境での環境チェックを行う() {
        assertFalse(EnvironmentUtil.isRunningAndroid());
        assertFalse(EnvironmentUtil.isRunningRobolectric());
    }
}
