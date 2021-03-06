package com.eaglesakura.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * a-z, 0-9, A-Zの中からランダムに文字を返す
     */
    public static byte randNumAlphabet() {
        switch (randUInt8() % 5) {
            case 0:
            case 1:
                return (byte) ('a' + (randUInt8() % 26));
            case 2:
            case 3:
                return (byte) ('A' + (randUInt8() % 26));
            default:
                return (byte) ('0' + (randUInt8() % 10));
        }
    }

    /**
     * ランダムな文字列を生成する
     */
    public static String randString(int length) {
        byte[] buffer = new byte[length];
        for (int i = 0; i < length; ++i) {
            buffer[i] = randNumAlphabet();
        }
        return new String(buffer);
    }

    /**
     * 短い周期では衝突しないと思われる短い文字列を生成する
     */
    public static String randShortString() {
        return randString(6) + ((int) RandomUtil.randUInt16() & 0xFF);
    }

    /**
     * ランダムな文字列を生成する
     */
    public static String randString() {
        return randString(32);
    }

    /**
     * ランダムである程度長い文字列を生成する
     */
    public static String randLargeString() {
        return randString(4 * 256);
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

    /**
     * 配列からランダムに１個取得する
     *
     * @param items 1以上の要素を持つリスト
     */
    public static <T> T randGet(Collection<T> items) {
        if (CollectionUtil.isEmpty(items)) {
            throw new IllegalArgumentException();
        }

        List<T> temp = new ArrayList<>(items);
        return temp.get(randUInt32() % items.size());
    }
}
