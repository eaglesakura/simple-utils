package com.eaglesakura.util;

import java.util.Date;

public class DateUtil {
    /**
     * 指定日の開始時刻を取得する
     */
    public static Date getDateStart(Date date) {
        long oneDay = 1000 * 60 * 60 * 24;
        long now = date.getTime();

        return new Date(now / oneDay * oneDay);
    }

    /**
     * 指定日の終了時刻を取得する
     */
    public static Date getDateEnd(Date date) {
        return new Date(getDateStart(date).getTime() + (1000 * 60 * 60 * 24) - 1);
    }

    /**
     * 今日の0時0分を取得する
     */
    public static Date getTodayStart() {
        final long oneDay = 1000 * 60 * 60 * 24;
        long now = System.currentTimeMillis();

        return new Date((now / oneDay) * oneDay);
    }

    /**
     * 今日の23時59分59秒....を取得する
     */
    public static Date getTodayEnd() {
        return new Date(getTodayStart().getTime() + (1000 * 60 * 60 * 24) - 1);
    }

}
