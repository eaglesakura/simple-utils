package com.eaglesakura.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 暗号化・指紋サポートを行う
 */
public class EncodeUtil {

    /**
     * byte配列からMD5を求める
     */
    public static String genMD5(byte[] buffer) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            byte[] digest = md.digest();

            StringBuffer sBuffer = new StringBuffer(digest.length * 2);
            for (byte b : digest) {
                String s = Integer.toHexString(((int) b) & 0xff);

                if (s.length() == 1) {
                    sBuffer.append('0');
                }
                sBuffer.append(s);
            }
            return sBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * byte配列からMD5を求める
     */
    public static String genMD5(InputStream is) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            {
                byte[] buffer = new byte[1024 * 8];
                int readed = 0;
                while ((readed = is.read(buffer)) > 0) {
                    md.update(buffer, 0, readed);
                }
            }

            byte[] digest = md.digest();

            StringBuffer sBuffer = new StringBuffer(digest.length * 2);
            for (byte b : digest) {
                String s = Integer.toHexString(((int) b) & 0xff);

                if (s.length() == 1) {
                    sBuffer.append('0');
                }
                sBuffer.append(s);
            }
            return sBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * byte配列からMD5を求める
     */
    public static String genSHA1(byte[] buffer) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(buffer);
            byte[] digest = md.digest();

            StringBuffer sBuffer = new StringBuffer(digest.length * 2);
            for (byte b : digest) {
                String s = Integer.toHexString(((int) b) & 0xff);

                if (s.length() == 1) {
                    sBuffer.append('0');
                }
                sBuffer.append(s);
            }
            return sBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * データをGZIP圧縮するか、そのまま返却する
     * 容量の小さいほうが採用される。
     */
    public static byte[] compressOrRaw(byte[] buffer) {
        if (buffer.length > 1024) {
            // ある程度データが大きくないと非効率的である
            byte[] resultBuffer = IOUtil.compressGzip(buffer);
            // データを比較し、もし圧縮率が高いようだったら圧縮した方を送信する
            if (resultBuffer.length < buffer.length) {
                return resultBuffer;
            } else {
                return buffer;
            }
        } else {
            return buffer;
        }
    }

    /**
     * GZIPバッファをデコードする。失敗したら元のオブジェクトを返却する。
     *
     * @param buffer 解凍対象バッファ
     * @return 解凍したバッファ
     */
    public static byte[] decompressOrRaw(byte[] buffer) {
        if (IOUtil.isGzip(buffer)) {
            byte[] resultBuffer = IOUtil.decompressGzipOrNull(buffer);
            if (resultBuffer == null) {
                return buffer;
            }
//            LogUtil.log("decompress gzip(%d bytes) -> raw(%d bytes) %.2f compress", buffer.length, resultBuffer.length, (float) buffer.length / (float) resultBuffer.length);
            return resultBuffer;
        } else {
            return buffer;
        }
    }

    public static long getHash64(byte[] buffer, long seed) {
        return getHash64(buffer, 0, buffer.length, seed);
    }

    /**
     * 既存アルゴリズムほどの巨大な長さは不要だが、そこそこの衝突耐性が必要なハッシュ値を生成する
     *
     * 移植元: https://github.com/eaglesakura/protoground/blob/develop/core/es/system/Hash.cpp
     *
     * @param buffer 計算するバッファ
     * @param offset 計算開始地点
     * @param length 計算するバッファの長さ
     * @param seed   ハッシュの初期値
     * @return 64bitハッシュ値
     */
    public static long getHash64(byte[] buffer, int offset, int length, long seed) {
        long result = seed;
        while (length > 0) {
            result ^= (((int) buffer[offset]) & 0xFF) * seed;
            seed = ~((seed << 7) | (seed >>> 57));
            ++offset;
            --length;
        }
        return result;
    }
}
