package com.eaglesakura.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CollectionUtil {

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
    public static <T> T[] asArray(Collection<T> c, T[] array) {
        return c.toArray(array);
    }

    /**
     * @param array 変換元配列
     * @param <T>   type
     * @return 変換したList
     */
    public static <T> List<T> asList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * 2つの配列をコピーする
     */
    public static <T> T[] copyOf(T[] array) {
        if (array == null) {
            return null;
        }
        return Arrays.copyOf(array, array.length);
    }

    /**
     * Mapに変換する
     *
     * @see CollectionUtil#
     */
    public static <Key, Value> Map<Key, Value> asMap(Collection<Value> values, Converter<Value, Key> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        for (Value value : values) {
            result.put(keyCreator.convert(value), value);
        }
        return result;
    }

    /**
     * 入力をKey-Valueの連続した配列として扱い、Mapに変換する
     */
    public static <Key, Value> Map<Key, Value> asPairMap(Collection<Value> values, Converter<Value, Key> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        Iterator<Value> iterator = values.iterator();
        while (iterator.hasNext()) {
            Key key = keyCreator.convert(iterator.next());
            Value value = iterator.next();
            result.put(key, value);
        }
        return result;
    }

    /**
     * 別なクラスのリストへ変換する
     */
    public static <T, R> List<R> asOtherList(List<T> origin, Converter<T, R> converter) {
        if (isEmpty(origin)) {
            return Collections.emptyList();
        }

        List<R> result = new ArrayList<>(origin.size());
        for (T item : origin) {
            result.add(converter.convert(item));
        }
        return result;
    }

    /**
     * あるオブジェクトを別なオブジェクトに変換する
     */
    public interface Converter<T, R> {
        R convert(T src);
    }

    public static boolean isEmpty(byte[] item) {
        return item == null || item.length == 0;
    }

    public static <T> boolean isEmpty(T[] item) {
        return item == null || item.length == 0;
    }

    public static <T> boolean isEmpty(Collection<T> item) {
        return item == null || item.isEmpty();
    }

}
