package com.eaglesakura.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimerTest {
    private static final double ASSERT_DELTA = 0.0001;

    @Test
    public void ミリ秒を別な単位に変換する() throws Exception {
        assertEquals(Timer.msToSec(1000), 1.0, ASSERT_DELTA);
        assertEquals(Timer.msToMinute(1000 * 60), 1.0, ASSERT_DELTA);
        assertEquals(Timer.msToHour(1000 * 60 * 60), 1.0, ASSERT_DELTA);
        assertEquals(Timer.msToDay(1000 * 60 * 60 * 24), 1.0, ASSERT_DELTA);
    }

    private void assertTime(int day, int hour, int minute, int sec, int milli) {
        final long MILLISEC = Timer.toMilliSec(day, hour, minute, sec, milli);
        assertEquals((MILLISEC / 1000 / 60 / 60 / 24), day);
        assertEquals((MILLISEC / 1000 / 60 / 60) % 24, hour);
        assertEquals((MILLISEC / 1000 / 60) % 60, minute);
        assertEquals((MILLISEC / 1000) % 60, sec);
        assertEquals(MILLISEC % 1000, milli);
    }

    @Test
    public void 指定した時刻に変換する() throws Exception {
        assertTime(0, 0, 0, 0, 500);
        assertTime(0, 0, 0, 30, 0);
        assertTime(0, 0, 30, 0, 0);
        assertTime(0, 12, 0, 0, 0);
        assertTime(1, 0, 0, 0, 0);
        assertTime(1, 12, 15, 30, 500);
    }
}
