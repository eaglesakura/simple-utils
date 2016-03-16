package com.eaglesakura.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    /**
     * 現在の年を取得する。
     * 2016年1月2日の場合は2016をそのまま返却する。
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 現在の月を取得する
     *
     * 2016年1月2日の場合は1をそのまま返却する
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 現在の日を取得する
     *
     * 2016年1月2日の場合は2を返却する
     */
    public static int getDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 現在のLocaleの時差を取得する。
     *
     * 日本であればGMT+9なので、1000 * 60 * 60 * 9の値が返却される
     */
    public static int getDateOffset() {
        return TimeZone.getDefault().getRawOffset();
    }

    /**
     * 指定日の開始時刻を取得する
     *
     * MEMO: 正確な時刻を扱うには、時差を加算しなければならない。
     */
    public static Date getDateStart(Date date) {
        long oneDay = 1000 * 60 * 60 * 24;
        long now = date.getTime();
        return new Date((now / oneDay * oneDay));
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
        return getDateStart(new Date());
    }

    /**
     * 今日の23時59分59秒....を取得する
     */
    public static Date getTodayEnd() {
        return new Date(getTodayStart().getTime() + (1000 * 60 * 60 * 24) - 1);
    }

}
