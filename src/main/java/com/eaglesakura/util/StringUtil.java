package com.eaglesakura.util;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringUtil {

    private static final Locale sLocale = Locale.getDefault();


    static {
        base64Converter = new Base64Converter() {
            /**
             * base64 encode/decode
             */
            private Object mBase64EncoderObj = null;

            /**
             * base64 encode/decode
             */
            private Object mBase64DecoderObj = null;

            /**
             * base64 encode
             */
            private Method base64Encode = null;

            /**
             * base64 decode
             */
            private Method base64Decode = null;

            private int flags;

            static final int ANDROID_URL_SAFE = 8;

            static final int ANDROID_NO_WRAP = 2;

            static final int ANDROID_NO_PADDING = 1;

            static final int ANDROID_NO_CLOSE = 16;

            Base64Converter init() {
                // init for Android
                try {
                    mBase64EncoderObj = Class.forName("android.util.Base64");
                    mBase64DecoderObj = mBase64EncoderObj;
                    base64Encode = ((Class) mBase64EncoderObj).getMethod("encodeToString", byte[].class, int.class);
                    base64Decode = ((Class) mBase64EncoderObj).getMethod("decode", String.class, int.class);
                    flags = ANDROID_NO_CLOSE | ANDROID_NO_PADDING | ANDROID_NO_WRAP | ANDROID_URL_SAFE;
                    return this;
                } catch (Exception e) {
                }

                // init for Apache
                try {
                    mBase64EncoderObj = Class.forName("org.apache.commons.codec.binary.Base64");
                    mBase64DecoderObj = mBase64EncoderObj;
                    base64Encode = ((Class) mBase64EncoderObj).getMethod("encodeBase64String", byte[].class);
                    base64Decode = ((Class) mBase64EncoderObj).getMethod("decodeBase64", String.class);
                    return this;
                } catch (Exception e) {
                }

                // init java8
                try {
                    Class Base64 = Class.forName("java.util.Base64");
                    {
                        Method getEncoder = Base64.getMethod("getEncoder");
                        mBase64EncoderObj = getEncoder.invoke(Base64);
                        base64Encode = mBase64EncoderObj.getClass().getMethod("encodeToString", byte[].class);
                    }
                    {
                        Method getDecoder = Base64.getMethod("getDecoder");
                        mBase64DecoderObj = getDecoder.invoke(Base64);
                        base64Decode = mBase64DecoderObj.getClass().getMethod("decode", String.class);
                    }

                    return this;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public String encode(byte[] buffer) {
                try {
                    if (flags != 0) {
                        return (String) base64Encode.invoke(mBase64EncoderObj, buffer, flags /* Base64.Default */);
                    } else {
                        return (String) base64Encode.invoke(mBase64EncoderObj, buffer);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public byte[] decode(String base64) {
                try {
                    if (flags != 0) {
                        return (byte[]) base64Decode.invoke(mBase64DecoderObj, base64, flags /* Base64.Default */);
                    } else {
                        return (byte[]) base64Decode.invoke(mBase64DecoderObj, base64);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }.init();
    }

    /**
     * 引数全ての文字列が空であればtrueを返す
     */
    public static boolean allEmpty(String... args) {
        for (String arg : args) {
            if (!StringUtil.isEmpty(arg)) {
                return false;
            }
        }

        return true;
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
     * 前後にある半角・全角スペースを削除する
     *
     * nullの場合は空文字を返却する
     */
    public static String trimSpacesOrEmpty(String src) {
        if (src == null) {
            return "";
        }

        while (src.startsWith(" ")) {
            src = src.substring(1);
        }

        while (src.endsWith(" ")) {
            src = src.substring(0, src.length() - 1);
        }


        while (src.startsWith("　")) {
            src = src.substring(1);
        }

        while (src.endsWith("　")) {
            src = src.substring(0, src.length() - 1);
        }

        return src;
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

    /**
     * デフォルトのLocaleを利用して書式設定する
     */
    public static String format(String fmt, Object... args) {
        return String.format(sLocale, fmt, args);
    }

    /**
     * 文字列の簡易ハッシュを生成する
     */
    public static long getHash64(String str) {
        return EncodeUtil.getHash64(str.getBytes(), 0x123456789ABCD012L);
    }

    /**
     * 正規表現を使用せず、単純な置換のみを行う
     *
     * @param value 対象文字列
     * @param org   検索対象文字
     * @param rep   置換文字
     */
    public static String replaceAllSimple(String value, String org, String rep) {
        String result = value;
        while (result.contains(org)) {
            result = value.replace(org, rep);
        }

        return result;
    }
}
