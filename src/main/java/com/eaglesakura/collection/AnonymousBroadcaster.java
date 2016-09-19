package com.eaglesakura.collection;

import com.eaglesakura.lambda.Action1;
import com.eaglesakura.util.ReflectionUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * コールバックリストを管理するクラス。
 *
 * 厳密でない、ある程度適当な条件下でメソッドコールを行うのに適している。
 */
public class AnonymousBroadcaster {

    Set<Item> mObjects = new HashSet<>();

    private void compact() {
        synchronized (this) {
            Iterator<Item> iterator = mObjects.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item.get() == null) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 指定したクラスのサブクラスに一致するオブジェクトを列挙する
     */
    public <T> List<T> list(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        compact();
        for (Item item : mObjects) {
            Object ref = item.get();
            if (ReflectionUtil.instanceOf(ref, clazz)) {
                result.add((T) ref);
            }
        }
        return result;
    }

    /**
     * 指定したクラス一覧にアクションを適用する
     */
    public <T> void safeEach(Class<T> clazz, Action1<T> action) {
        try {
            for (T item : list(clazz)) {
                action.action(item);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 強参照オブジェクトとして登録する
     */
    public AnonymousBroadcaster register(Object obj) {
        compact();
        mObjects.add(new Item().ref(obj));
        return this;
    }

    /**
     * 弱参照オブジェクトとして登録する
     */
    public AnonymousBroadcaster weakRegister(Object obj) {
        compact();
        mObjects.add(new Item().ref(obj));
        return this;
    }

    public AnonymousBroadcaster unregister(Object obj) {
        compact();
        synchronized (this) {
            Iterator<Item> iterator = mObjects.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item.get() == obj) {
                    iterator.remove();
                }
            }
        }
        return this;
    }


    private class Item {
        Object mRef;

        WeakReference<Object> mWeakRef;

        long mHashCode;

        public Item weak(Object obj) {
            mHashCode = obj.hashCode();
            mWeakRef = new WeakReference<>(obj);
            mHashCode = ((long) obj.hashCode() << 32) | ((long) obj.getClass().hashCode() & 0x00000000FFFFFFFFL);
            return this;
        }

        public Item ref(Object obj) {
            mRef = obj;
            mHashCode = ((long) obj.hashCode() << 32) | ((long) obj.getClass().hashCode() & 0x00000000FFFFFFFFL);
            return this;
        }

        public Object get() {
            if (mRef != null) {
                return mRef;
            } else {
                return mWeakRef.get();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item that = (Item) o;

            return mHashCode == that.mHashCode;

        }

        @Override
        public int hashCode() {
            return Long.valueOf(mHashCode).hashCode();
        }
    }
}
