package com.eaglesakura.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RandomUtilTest {

    final int TRY_CHECK_COUNT = 4096;

    @Test
    public void byteで負の値が返却されないことを確認() throws Exception {
        for (int i = 0; i < TRY_CHECK_COUNT; ++i) {
            assertTrue(RandomUtil.randUInt8() >= 0);
        }
    }

    @Test
    public void shortで負の値が返却されないことを確認() throws Exception {
        for (int i = 0; i < TRY_CHECK_COUNT; ++i) {
            assertTrue(RandomUtil.randUInt16() >= 0);
        }
    }

    @Test
    public void intで負の値が返却されないことを確認() throws Exception {
        for (int i = 0; i < TRY_CHECK_COUNT; ++i) {
            assertTrue(RandomUtil.randUInt32() >= 0);
        }
    }

    @Test
    public void longで負の値が返却されないことを確認() throws Exception {
        for (int i = 0; i < TRY_CHECK_COUNT; ++i) {
            assertTrue(RandomUtil.randUInt64() >= 0);
        }
    }

    public enum TestEnum {
        Value0,
        Value1,
        value2
    }

    @Test
    public void ランダムenumでnullが返却されないことを確認() throws Exception {
        for (int i = 0; i < TRY_CHECK_COUNT; ++i) {
            assertNotNull(RandomUtil.randEnum(TestEnum.class));
        }
    }


    @Test
    public void ランダムenumでnullが含まれることを確認する() throws Exception {
        int retNull = 0;
        int retNotNull = 0;

        for (int i = 0; i < TRY_CHECK_COUNT; ++i) {
            if (RandomUtil.randEnumWithNull(TestEnum.class) != null) {
                ++retNotNull;
            } else {
                ++retNull;
            }
        }

        // どちらも返却に含まれることを確認する
        assertTrue(retNotNull > 0);
        assertTrue(retNull > 0);
    }
}
