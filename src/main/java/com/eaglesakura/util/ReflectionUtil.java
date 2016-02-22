package com.eaglesakura.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * リフレクション関係のUtil
 */
public class ReflectionUtil {
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
}
