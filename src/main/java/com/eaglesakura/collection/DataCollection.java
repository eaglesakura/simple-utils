package com.eaglesakura.collection;

import com.eaglesakura.lambda.Matcher1;
import com.eaglesakura.lambda.ResultAction1;
import com.eaglesakura.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * データの集合を示すUtilクラス
 *
 * 元データをListとして持つため、速度やメモリ的には不利だが、簡単にフィルタリング等を行える。
 */
public class DataCollection<T> {
    final List<T> mDataList;

    Comparator<T> mComparator;

    public DataCollection(List<T> dataList) {
        mDataList = dataList;
    }

    public DataCollection() {
        mDataList = new ArrayList<>();
    }

    public DataCollection<T> setComparator(Comparator<T> comparator) {
        mComparator = comparator;
        return this;
    }

    /**
     * データをMapに変換する
     *
     * @param filter     データフィルタ
     * @param keyConvert Key変換
     * @param <Key>      Keyクラス
     */
    public <Key> Map<Key, T> asMap(Matcher1<T> filter, ResultAction1<T, Key> keyConvert) {
        return CollectionUtil.asMap(list(filter, false), keyConvert);
    }

    /**
     * return用の新しいListを生成する
     */
    protected List<T> newList(int size) {
        return new ArrayList<T>(size);
    }

    /**
     * データソートを行う
     */
    protected void sort(List<T> items) {
        if (mComparator != null) {
            Collections.sort(items, mComparator);
        }
    }

    /**
     * 元のリストの参照をそのまま取得する
     */
    public List<T> getSource() {
        return mDataList;
    }

    /**
     * 全てのデータを取得する
     */
    public List<T> list() {
        return list(new Matcher1<T>() {
            @Override
            public boolean match(T it) throws Throwable {
                return true;
            }
        }, true);
    }

    /**
     * 条件にマッチするデータを取得する
     */
    public List<T> list(Matcher1<T> filter) {
        return list(filter, true);
    }

    /**
     * 条件にマッチするデータを取得する
     */
    public List<T> list(Matcher1<T> filter, boolean sort) {
        try {
            List<T> result = newList(mDataList.size());
            for (T item : mDataList) {
                if (filter.match(item)) {
                    result.add(item);
                }
            }
            if (sort) {
                sort(result);
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
