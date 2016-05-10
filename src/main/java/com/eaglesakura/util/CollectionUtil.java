package com.eaglesakura.util;

import com.eaglesakura.lambda.Action1;
import com.eaglesakura.lambda.Action2;
import com.eaglesakura.lambda.ResultAction1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    /**
     * Setの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <T> Set<T> each(Set<T> set, Action1<T> action) throws Throwable {
        for (T it : set) {
            action.action(it);
        }
        return set;
    }

    /**
     * Mapの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <K, V> Map<K, V> each(Map<K, V> map, Action1<V> action) throws Throwable {
        for (V it : map.values()) {
            action.action(it);
        }
        return map;
    }

    /**
     * Mapの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <K, V> Map<K, V> each(Map<K, V> map, Action2<K, V> action) throws Throwable {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            action.action(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Listの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <T> List<T> each(List<T> list, Action1<T> action) throws Throwable {
        for (T it : list) {
            action.action(it);
        }
        return list;
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
     * リストに全てのオブジェクトを登録し、listをそのまま返す
     */
    public static <T> List<T> addAll(List<T> list, Collection<T> items) {
        list.addAll(items);
        return list;
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
    public static <Key, Value> Map<Key, Value> asMap(Collection<Value> values, ResultAction1<Value, Key> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        for (Value value : values) {
            try {
                result.put(keyCreator.action(value), value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 入力をKey-Valueの連続した配列として扱い、Mapに変換する
     */
    public static <Key, Value> Map<Key, Value> asPairMap(Collection<Value> values, ResultAction1<Value, Key> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        Iterator<Value> iterator = values.iterator();
        while (iterator.hasNext()) {
            try {
                Key key = keyCreator.action(iterator.next());
                Value value = iterator.next();
                result.put(key, value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 別なクラスのリストへ変換する
     */
    public static <T, R> List<R> asOtherList(Iterable<T> origin, ResultAction1<T, R> converter) {
        List<R> result = new ArrayList<>();
        for (T item : origin) {
            try {
                result.add(converter.action(item));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 別なクラスのリストへ変換する
     */
    public static <T, R> List<R> asOtherList(Iterator<T> origin, ResultAction1<T, R> converter) {
        List<R> result = new ArrayList<>();
        while (origin.hasNext()) {
            try {
                result.add(converter.action(origin.next()));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
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
