package com.eaglesakura.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * リフレクション関係のUtil
 */
public class ReflectionUtil {

    /**
     * 指定したAnnotationが含まれたフィールド(public以外を含む)一覧を返す
     *
     * AnnotationにはRuntime属性が付与されてなければならない
     */
    public static <T extends Annotation> List<Field> listAnnotationFields(Class srcClass, Class<T> annotationClass) {
        List<Field> result = new ArrayList<>();

        while (!srcClass.equals(Object.class)) {
            for (Field field : srcClass.getDeclaredFields()) {
                T annotation = field.getAnnotation(annotationClass);
                if (annotation != null) {
                    result.add(field);
                }
            }

            srcClass = srcClass.getSuperclass();
        }

        return result;
    }

    /**
     * 指定したAnnotationが含まれたメソッド(public以外を含む)一覧を返す
     *
     * AnnotationにはRuntime属性が付与されてなければならない
     * オーバーライドされたメソッドは1つにまとめて扱う
     */
    public static <T extends Annotation> List<Method> listAnnotationMethods(Class srcClass, Class<T> annotationClass) {
        Map<String, Method> result = new HashMap<>();

        while (!srcClass.equals(Object.class)) {
            for (Method method : srcClass.getDeclaredMethods()) {
                T annotation = method.getAnnotation(annotationClass);
                if (annotation != null && !result.containsKey(method.getName())) {
                    result.put(method.getName(), method);
                }
            }

            srcClass = srcClass.getSuperclass();
        }

        return new ArrayList<>(result.values());
    }

    /**
     * Enumのname()から実体を取得する。
     * 取得に失敗した場合はnullを返却する。
     */
    public static <T extends Enum> T valueOf(Class<T> clazz, String name) {
        try {
            Method valueOfMethod = clazz.getMethod("valueOf", String.class);
            return (T) valueOfMethod.invoke(clazz, name);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Enumのname()から実体を取得する。
     * 取得に失敗した場合はdefValueを返却する。
     */
    public static <T extends Enum> T valueOf(Class<T> clazz, String name, T defValue) {
        T result = valueOf(clazz, name);
        if (result == null) {
            return defValue;
        } else {
            return result;
        }
    }

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
