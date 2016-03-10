package com.eaglesakura.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    public static int getInt(Integer value, int defValue) {
        if (value == null) {
            return defValue;
        } else {
            return value;
        }
    }

    public static double getDouble(Double value, double defValue) {
        if (value == null) {
            return defValue;
        } else {
            return value;
        }
    }

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

    /**
     * 単純にsleepさせる。
     */
    public static void sleep(long timems) {
        if (timems <= 0) {
            // sleep時間が0ならば何もする必要はない
            return;
        }

        try {
            Thread.sleep(timems);
        } catch (Exception e) {
            LogUtil.log(e);
        }
    }

    /**
     * nano秒単位でsleepを行う
     */
    public static void nanosleep(long ms, int nano) {
        if (ms <= 0 && nano <= 0) {
            // sleep時間が0ならば何もする必要はない
            return;
        }

        try {
            Thread.sleep(ms, nano);
        } catch (Exception e) {
            LogUtil.log(e);
        }
    }

    /**
     * itemが重複しないようにaddする
     */
    public static <T> boolean addUnique(List<T> list, T item) {
        if (!list.contains(item)) {
            list.add(item);
            return true;
        } else {
            return false;
        }
    }

    /**
     * アイテムを追加し、追加したインデックスを返す
     */
    public static <T> int addUniqueRequestIndex(List<T> list, T item) {
        addUnique(list, item);
        return list.indexOf(item);
    }

    /**
     * @see Collection#toArray(Object[])
     */
    public static <T> T[] convert(Collection<T> c, T[] array) {
        return c.toArray(array);
    }

    /**
     * @param array 変換元配列
     * @param <T>   type
     * @return 変換したList
     */
    public static <T> List<T> convert(T[] array) {
        return new ArrayList<T>(Arrays.asList(array));
    }

    /**
     * 2つの配列をコピーする
     */
    public static <T> T[] copy(T[] array) {
        return Arrays.copyOf(array, array.length);
    }

    /**
     * Mapに変換する
     */
    public static <Key, Value> Map<Key, Value> asMap(Collection<Value> values, KeyCreator<Key, Value> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        for (Value value : values) {
            result.put(keyCreator.createKey(value), value);
        }
        return result;
    }

    public interface KeyCreator<Key, Value> {
        Key createKey(Value value);
    }

    public static boolean isEmpty(byte[] item) {
        return item == null || item.length == 0;
    }

    public static <T> boolean isEmpty(T[] item) {
        return item == null || item.length == 0;
    }

    public static <T> boolean isEmpty(List<T> item) {
        return item == null || item.isEmpty();
    }

    public static <T> T newInstanceOrNull(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T newInstanceOrNull(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public interface ItemConverter<T, R> {
        R as(T origin);
    }

    /**
     * オブジェクトリストを別なオブジェクトに変換する
     */
    public static <T, R> List<R> convert(List<T> origin, ItemConverter<T, R> converter) {
        if (isEmpty(origin)) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<>(origin.size());
        int index = 0;
        for (T item : origin) {
            result.add(index++, converter.as(item));
        }
        return result;
    }
}
