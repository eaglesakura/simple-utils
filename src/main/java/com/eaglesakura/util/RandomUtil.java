package com.eaglesakura.util;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 乱数生成用Util
 */
public class RandomUtil {
    /**
     * ランダムな真偽値を生成する
     */
    public static boolean randBool() {
        return randInt8() % 2 == 0;
    }

    /**
     * 1byteの整数を生成する
     */
    public static byte randInt8() {
        return (byte) ((Math.random() * 255) - 128);
    }

    /**
     * 2byteの整数を生成する
     */
    public static short randInt16() {
        return (short) (Math.random() * (double) 0x0000FFFF);
    }

    /**
     * 4byteの整数を生成する
     */
    public static int randInt32() {
        return (int) (Math.random() * (double) (0x00000000FFFFFFFFL));
    }

    /**
     * 8byteの整数を生成する
     */
    public static long randInt64() {
        return ((long) randInt32() & 0x00000000FFFFFFFFL) << 32 | ((long) randInt32() & 0x00000000FFFFFFFFL);
    }

    /**
     * 0~127の整数を生成する
     */
    public static byte randUInt8() {
        return (byte) (Math.random() * 127);
    }

    /**
     * 2byteの0以上整数を生成する
     */
    public static short randUInt16() {
        return (short) ((int) randInt16() & 0x00007FFF);
    }

    /**
     * 4byteの0以上整数を生成する
     */
    public static int randUInt32() {
        return randInt32() & 0x7FFFFFFF;
    }

    /**
     * 8byteの0以上整数を生成する
     */
    public static long randUInt64() {
        return randInt64() & 0x7FFFFFFFFFFFFFFFL;
    }

    /**
     * 0.0～1.0の乱数を生成する
     */
    public static float randFloat() {
        return (float) Math.random();
    }

    /**
     * ランダムな文字列を生成する
     */
    public static String randString() {
        return UUID.randomUUID().toString();
    }

    /**
     * ランダムである程度長い文字列を生成する
     */
    public static String randLargeString() {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 256; ++i) {
            result.append(randString());
            result.append("-");
        }
        return result.toString();
    }

    /**
     * ランダムな長さ・内容のバイト配列を生成する
     */
    public static byte[] randBytes() {
        return randBytes(32 + randUInt8());
    }

    /**
     * ランダムな長さ・内容のバイト配列を生成する
     */
    public static byte[] randBytes(int length) {
        byte[] buffer = new byte[length];
        for (int i = 0; i < buffer.length; ++i) {
            buffer[i] = randInt8();
        }
        return buffer;
    }


    /**
     * ランダムなenumを取得する
     */
    public static <T extends Enum> T randEnum(Class<T> clazz) {
        try {
            Method valuesMethod = clazz.getMethod("values");
            T[] values = (T[]) valuesMethod.invoke(clazz);
            return values[randUInt8() % values.length];
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * nullを含めたランダムなenumを取得する
     */
    public static <T extends Enum> T randEnumWithNull(Class<T> clazz) {
        try {
            Method valuesMethod = clazz.getMethod("values");
            T[] values = (T[]) valuesMethod.invoke(clazz);
            if (randUInt8() % (values.length + 1) == 0) {
                return null;
            } else {
                return values[randUInt8() % values.length];
            }
        } catch (Exception e) {
            return null;
        }
    }
}
