package com.eaglesakura.util;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    static {
        base64Converter = new Base64Converter() {
            /**
             * base64 encode/decode
             */
            private Class base64Class = null;

            /**
             * base64 encode
             */
            private Method base64Encode = null;

            /**
             * base64 decode
             */
            private Method base64Decode = null;

            private int flags;

            static final int URL_SAFE = 8;

            static final int NO_WRAP = 2;

            static final int NO_PADDING = 1;

            static final int NO_CLOSE = 16;

            Base64Converter init() {
                // init for Android
                try {
                    base64Class = Class.forName("android.util.Base64");
                    base64Encode = base64Class.getMethod("encodeToString", byte[].class, int.class);
                    base64Decode = base64Class.getMethod("decode", String.class, int.class);
                    flags = NO_CLOSE | NO_PADDING | NO_WRAP | URL_SAFE;
                    return this;
                } catch (Exception e) {
                }

                // init for Apache
                try {
                    base64Class = Class.forName("org.apache.commons.codec.binary.Base64");
                    base64Encode = base64Class.getMethod("encodeBase64", byte[].class, int.class);
                    base64Decode = base64Class.getMethod("decodeBase64", String.class, int.class);
                    flags = 0;
                    return this;
                } catch (Exception e) {
                }
                throw new IllegalStateException();
            }

            @Override
            public String encode(byte[] buffer) {
                try {
                    return (String) base64Encode.invoke(base64Class, buffer, flags /* Base64.Default */);
                } catch (Exception e) {
                    throw new IllegalStateException();
                }
            }

            @Override
            public byte[] decode(String base64) {
                try {
                    return (byte[]) base64Decode.invoke(base64Class, base64, flags /* Base64.Default */);
                } catch (Exception e) {
                    throw new IllegalStateException();
                }
            }
        }.init();
    }

    /**
     * 引数全ての文字列が有効であればtrueを返す
     */
    public static boolean allNotEmpty(String... args) {
        for (String arg : args) {
            if (StringUtil.isEmpty(arg)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 文字列がnullか空文字だったらtrueを返す。
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.length() == 0;
    }

    /**
     * strがnullかemptyだったらnullを返す。
     */
    public static String emptyToNull(String str) {
        return isEmpty(str) ? null : str;
    }

    /**
     * 全角英数を半角英数に変換する
     */
    public static String zenkakuEngToHankakuEng(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c >= 'Ａ' && c <= 'Ｚ') {
                c = (char) (c - 'Ａ' + 'A');
            } else if (c >= '０' && c <= '９') {
                c = (char) (c - '０' + '0');
            } else {
                switch (c) {
                    case '＜':
                        c = '<';
                        break;
                    case '＞':
                        c = '>';
                        break;
                    case '　':
                        c = ' ';
                        break;
                    case '／':
                        c = '/';
                        break;
                    case '！':
                        c = '!';
                        break;
                    case '？':
                        c = '?';
                        break;
                    case '．':
                        c = '.';
                        break;
                }
            }

            sb.setCharAt(i, c);
        }

        return sb.toString();
    }

    /**
     * 全角文字を半角文字に変更する
     */
    public static String zenkakuHiraganaToZenkakuKatakana(String s) {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c >= 'ァ' && c <= 'ン') {
                sb.setCharAt(i, (char) (c - 'ァ' + 'ぁ'));
            } else if (c == 'ヵ') {
                sb.setCharAt(i, 'か');
            } else if (c == 'ヶ') {
                sb.setCharAt(i, 'け');
            } else if (c == 'ヴ') {
                sb.setCharAt(i, 'う');
                sb.insert(i + 1, '゛');
                i++;
            }
        }

        return sb.toString();
    }

    /**
     * Macエンコードの文字列を、Winで使用可能な文字列に変換する
     */
    public static String macStringToWinString(String str) {
        final int indexOffsetDakuten = ('が' - 'か');
        final int indexOffsetHandakuten = ('ぱ' - 'は');
        final int dakuten = '゙';
        final int handakuten = '゚';

        StringBuffer sb = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i < (str.length() - 1)) {
                char cNext = str.charAt(i + 1);
                if (cNext == dakuten) {
                    // 特殊な濁点補正
                    switch (c) {
                        case 'う':
                            c = 'ゔ';
                            break;
                        case 'ウ':
                            c = 'ヴ';
                            break;
                        default:
                            c += indexOffsetDakuten;
                            break;
                    }
                } else if (cNext == handakuten) {
                    c += indexOffsetHandakuten;
                }
            }

            if (c != dakuten && c != handakuten) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 日本語を意識してJavaの辞書順に並び替える
     */
    public static int compareString(String a, String b) {
        a = zenkakuHiraganaToZenkakuKatakana(a.toLowerCase());
        a = zenkakuEngToHankakuEng(a);
        b = zenkakuHiraganaToZenkakuKatakana(b.toLowerCase());
        b = zenkakuEngToHankakuEng(b);

        return a.compareTo(b);
    }

    private static final SimpleDateFormat DEFAULT_FORMATTER = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SS");

    private static final SimpleDateFormat EXIF_FORMATTER = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    /**
     * 指定時刻を文字列に変換する
     * 内容はyyyyMMdd-hh:mm:ss.SSとなる。
     */
    public static String toString(Date date) {
        return DEFAULT_FORMATTER.format(date);
    }

    /**
     * yyyyMMdd-hh:mm:ss.SSフォーマットの文字列をDateに変換する
     */
    public static Date toDate(String date) {
        try {
            return DEFAULT_FORMATTER.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * EXIF記録されている時刻から日時に変換する
     */
    public static Date toExifDate(String exifDate) {
        try {
            return EXIF_FORMATTER.parse(exifDate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Base64変換器
     */
    private static Base64Converter base64Converter;

    /**
     * Base64変換インターフェース
     */
    public interface Base64Converter {
        String encode(byte[] buffer);

        byte[] decode(String base64);
    }

    public static int toInteger(String value, int def) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return def;
        }
    }

    public static double toDouble(String value, double def) {
        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            return def;
        }
    }

    public static boolean toBoolean(String value, boolean def) {
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * base64エンコードする
     */
    public static String toString(byte[] buffer) {
        return base64Converter.encode(buffer);
    }

    /**
     * base64文字列をバイト配列へ変換する
     */
    public static byte[] toByteArray(String base64) {
        return base64Converter.decode(base64);
    }

    /**
     * 16進数の文字に変換する。
     * 1桁の場合、頭に"0"を挿入する
     */
    public static String toHexString(byte b) {
        String result = Integer.toHexString(((int) b) & 0xFF);
        if (result.length() == 1) {
            return "0" + result;
        } else {
            return result;
        }
    }

    /**
     * 16進数表現に変換する
     */
    public static String toHexString(byte[] bytes) {
        StringBuffer sBuffer = new StringBuffer(bytes.length * 2);
        for (byte b : bytes) {
            String s = Integer.toHexString(((int) b) & 0xff);

            if (s.length() == 1) {
                sBuffer.append('0');
            }
            sBuffer.append(s);
        }

        return sBuffer.toString();
    }

    /**
     * 16進数変換を行う
     */
    public static long parseHex(String intHex, long defValue) {
        if (StringUtil.isEmpty(intHex)) {
            return defValue;
        }

        intHex = intHex.toUpperCase();
        if (intHex.startsWith("0X")) {
            // Program
            intHex = intHex.substring(2);
        } else if (intHex.startsWith("#")) {
            // WebColor
            intHex = intHex.substring(1);
        }

        try {
            return Long.parseLong(intHex, 16);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * WebColor RGBをARGB形式に変換する
     */
    public static int parseWebColorRGB2XRGB(String webColor) {
        int rgb = (int) (parseHex(webColor, 0x00000000FFFFFFFF) & 0x0000000000FFFFFFL);
        return 0xFF000000 | rgb;
    }

    /**
     * WebColor ARGBをARGB形式に変換する
     */
    public static int parseWebColorARGB2ARGB(String webColor) {
        int argb = (int) (parseHex(webColor, 0x00000000FFFFFFFF) & 0x00000000FFFFFFFFL);
        return argb;
    }

}
