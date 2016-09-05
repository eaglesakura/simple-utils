package com.eaglesakura.collection;

import com.eaglesakura.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文字列をフラグとして扱う
 *
 * 内部のセパレータに半角カンマ`,`を使用するため、フラグにカンマを利用することはできない。
 */
public class StringFlag {
    static final char SEPARATOR = ',';

    Set<String> mFlags = new HashSet<>();

    public StringFlag() {
    }

    public StringFlag(Collection<Object> flags) {
        for (Object flg : flags) {
            add(flg);
        }
    }

    public StringFlag(Iterable<Object> flags) {
        for (Object flg : flags) {
            add(flg);
        }
    }

    public StringFlag(Object... flags) {
        for (Object flag : flags) {
            add(flag);
        }
    }

    public boolean contains(Object flag) {
        return mFlags.contains(flag.toString());
    }

    /**
     * 文字列をフラグとして扱う
     */
    public StringFlag add(Object flag) {
        String strFlag = flag.toString();
        if (strFlag.indexOf(SEPARATOR) >= 0) {
            throw new IllegalArgumentException(strFlag);
        }
        mFlags.add(strFlag);
        return this;
    }

    /**
     * 他のフラグリストを追加する
     */
    public StringFlag add(StringFlag flag) {
        mFlags.addAll(flag.mFlags);
        return this;
    }

    private static final Comparator<String> COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    };

    private List<String> toList() {
        List<String> list = new ArrayList<>(mFlags);
        Collections.sort(list, COMPARATOR);
        return list;
    }

    /**
     * SQLiteのLIKE句で検索するためのキーワードを取得する
     */
    public String toLikeQuery() {
        if (mFlags.isEmpty()) {
            return ",,,";   // フラグが設定されていない場合、絶対にヒットしないようにする。
        }

        StringBuilder builder = new StringBuilder();
        for (String flag : toList()) {
            builder.append("%").append(SEPARATOR).append(flag).append(SEPARATOR);
        }
        return builder.append("%").toString();
    }

    /**
     * フラグ文字列に変換する
     */
    @Override
    public String toString() {
        if (mFlags.isEmpty()) {
            // フラグが設定されていない
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (String flag : toList()) {
            builder.append(SEPARATOR).append(flag);
        }
        builder.append(SEPARATOR);
        return builder.toString();
    }

    /**
     * フラグリストの文字列からフラグを再生する
     */
    public static StringFlag parse(String flags) {
        StringFlag result = new StringFlag();
        if (StringUtil.isEmpty(flags)) {
            return result;
        }

        for (String flag : flags.split(String.valueOf(SEPARATOR))) {
            if (!StringUtil.isEmpty(flag)) {
                result.add(flag);
            }
        }
        return result;
    }

    /**
     * フラグ同士の和を取得する
     *
     * @param aFlags 文字列フラグA
     * @param bFlags 文字列フラグB
     */
    public static String or(String aFlags, String bFlags) {
        StringFlag flagA = parse(aFlags);
        StringFlag flagB = parse(bFlags);

        return flagA.add(flagB).toString();
    }

}
