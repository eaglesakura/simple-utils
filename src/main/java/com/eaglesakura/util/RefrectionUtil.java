package com.eaglesakura.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * リフレクション関係のUtil
 */
public class RefrectionUtil {
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
    public static Class getArrayClass(Field field) {
        if (!isListInterface(field)) {
            throw new IllegalArgumentException();
        }

        try {
            Type genericType = field.getGenericType();
            Class result = (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
            return result;
        } catch (Exception e) {
            LogUtil.log(e);
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
