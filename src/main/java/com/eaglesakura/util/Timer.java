package com.eaglesakura.util;

public class Timer {
    protected long startTime = System.currentTimeMillis();
    protected long endTime = System.currentTimeMillis();

    public Timer() {

    }

    public Timer(long startTime) {
        this.startTime = startTime;
    }

    /**
     * タイマーを開始する。
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * ストップウォッチを開始する。
     */
    public void start() {
        startTime = System.currentTimeMillis();
        endTime = startTime;
    }

    /**
     * ストップウォッチを停止し、時間をミリ秒で取得する。
     */
    public long end() {
        endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * ストップウォッチを停止し、時間を秒単位で取得する
     */
    public double endSec() {
        return ((double) end()) / 1000.0;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * {@link #start()}を呼び出した後、一度でも
     * {@link #end()}を呼び出したらtrue
     */
    public boolean isEnd() {
        return endTime != startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    /**
     * 各桁を指定して値を設定する
     */
    public static long toMilliSec(int day, int hour, int minute, int sec, int millisec) {
        return (1000L * 60L * 60L * 24L * day) +
                (1000L * 60L * 60L * hour) +
                (1000L * 60L * minute) +
                (1000L * sec) +
                millisec;
    }

    /**
     * ミリ秒を秒に変換する
     */
    public static double msToSec(long milliSec) {
        return ((double) milliSec) / 1000.0;
    }

    /**
     * ミリ秒を分に変換する
     */
    public static double msToMinute(long milliSec) {
        return ((double) milliSec) / (1000.0 * 60.0);
    }

    /**
     * ミリ秒を時間に変換する
     */
    public static double msToHour(long milliSec) {
        return ((double) milliSec) / (1000.0 * 60.0 * 60.0);
    }

    /**
     * ミリ秒を日に変換する
     */
    public static double msToDay(long milliSec) {
        return ((double) milliSec) / (1000.0 * 60.0 * 60.0 * 24.0);
    }
}
