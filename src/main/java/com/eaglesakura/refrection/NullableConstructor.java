package com.eaglesakura.refrection;

import com.eaglesakura.util.ReflectionUtil;

import java.lang.reflect.Constructor;

/**
 * Nullを許容したコンストラクタクラス。
 * newInstanceに失敗した場合、nullを返却する
 */
public class NullableConstructor<T> {

    final Constructor<T> mConstructor;

    final Class<T> mClass;

    public NullableConstructor(Class<T> clazz, Class... args) {
        mClass = clazz;
        mConstructor = ReflectionUtil.getConstructorOrNull(clazz, args);
    }

    public Constructor<T> getConstructor() {
        return mConstructor;
    }

    /**
     * インスタンスを生成するが、失敗した場合は例外を投げずにnullを返却する
     */
    public T newInstance(Object... args) {
        return ReflectionUtil.newInstanceOrNull(mConstructor, args);
    }

    /**
     * インスタンスを生成する
     */
    public static <T> NullableConstructor<T> get(Class<T> clazz, Class... args) {
        return new NullableConstructor<>(clazz, args);
    }
}
