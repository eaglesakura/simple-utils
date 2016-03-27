package com.eaglesakura.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final int DAY_MILLI_SEC = 1000 * 60 * 60 * 24;

    /**
     * 現在の年を取得する。
     * 2016年1月2日の場合は2016をそのまま返却する。
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int getYear(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(zone);
        calendar.setTime(date);
        return getYear(calendar);
    }

    /**
     * 現在の月を取得する
     *
     * 2016年1月2日の場合は1をそのまま返却する
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(zone);
        calendar.setTime(date);
        return getMonth(calendar);
    }


    /**
     * 現在の日を取得する
     *
     * 2016年1月2日の場合は2を返却する
     */
    public static int getDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDay(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(zone);
        calendar.setTime(date);
        return getDay(calendar);
    }

    /**
     * 現在の時間を24時間単位で取得する
     *
     * @param calendar 0-23
     */
    public static int getHour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(zone);
        calendar.setTime(date);
        return getHour(calendar);
    }

    /**
     * 年月日を指定し、午前0時0分を取得する
     *
     * @param timeZone 時差補正
     * @param year     2016年の場合は2016
     * @param month    1月の場合は1
     * @param day      2日の場合は2
     * @return Date
     */
    public static Date getTime(TimeZone timeZone, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.set(year, month - 1, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
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
     * 時差を考慮して一日の開始時間を取得する
     *
     * @param date     現在時刻
     * @param timeZone 時差設定
     * @return 現在時刻が所属する日の午前0時0分
     */
    public static Date getDateStart(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 指定ミリ秒だけオフセットさせた時刻を生成する
     *
     * @param date 時刻
     * @param time オフセット時間
     */
    public static Date offset(Date date, long time) {
        return new Date(date.getTime() + time);
    }

    /**
     * 指定日の開始時刻を取得する
     *
     * MEMO: 正確な時刻を扱うには、時差を加算しなければならない。
     */
    @Deprecated
    public static Date getDateStart(Date date) {
        long now = date.getTime();
        return new Date((now / DAY_MILLI_SEC * DAY_MILLI_SEC));
    }

    /**
     * 指定日の終了時刻を取得する
     */
    @Deprecated
    public static Date getDateEnd(Date date) {
        return new Date(getDateStart(date).getTime() + (1000 * 60 * 60 * 24) - 1);
    }

    /**
     * 今日の0時0分を取得する
     */
    @Deprecated
    public static Date getTodayStart() {
        return getDateStart(new Date());
    }

    /**
     * 今日の23時59分59秒....を取得する
     */
    @Deprecated
    public static Date getTodayEnd() {
        return new Date(getTodayStart().getTime() + (1000 * 60 * 60 * 24) - 1);
    }

}
