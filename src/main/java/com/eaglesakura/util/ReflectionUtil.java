package com.eaglesakura.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * リフレクション関係のUtil
 */
public class ReflectionUtil {

    /**
     * objがclazzクラスかサブクラスである場合trueを返す
     */
    public static boolean instanceOf(Object obj, Class<?> clazz) {
        try {
            return obj.getClass().asSubclass(clazz) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ListがImplされている場合はtrueを返却する
     */
    public static boolean isListInterface(Class<?> clazz) {
        try {
            return clazz.asSubclass(List.class) != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Listの引数に指定されたClassを取得する
     */
    public static Class getListGenericClass(Field field) {
        if (!isListInterface(field)) {
            throw new IllegalArgumentException();
        }

        try {
            Type genericType = field.getGenericType();
            return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Listがimplされている場合はtrueを返却する
     */
    public static boolean isListInterface(Field field) {
        return isListInterface(field.getType());
    }

    /**
     * コンストラクタを取得するか、nullを返却する
     */
    public static <T> Constructor<T> getConstructorOrNull(Class<T> clazz, Class... args) {
        try {
            return clazz.getConstructor(args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * オブジェクトを生成するか、nullを返却する
     */
    public static <T> T newInstanceOrNull(Constructor<T> constructor, Object... args) {
        if (constructor == null) {
            return null;
        }

        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Classのインスタンスを生成するか、nullを返却する
     */
    public static <T> T newInstanceOrNull(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Classのインスタンスを生成するか、nullを返却する
     */
    public static <T> T newInstanceOrNull(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}
