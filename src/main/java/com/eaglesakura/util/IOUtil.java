package com.eaglesakura.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * File関連の便利メソッドを提供する
 */
public class IOUtil {

    /**
     * inputのバッファを全てoutputへコピーする。 完了した時点でストリームはcloseされる。
     *
     * @param input  コピー元
     * @param output コピー先
     * @throws IOException
     */
    public static void copyTo(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 128];
        int length = 0;

        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        input.close();
        output.close();
    }

    /**
     * inputのバッファを全てoutputへコピーする。 完了した時点でストリームはcloseされる。
     *
     * @param input       コピー元
     * @param closeInput  終了時にinputを閉じる
     * @param output      コピー先
     * @param closeOutput 終了時にoutputを閉じる
     * @throws IOException
     */
    public static void copyTo(InputStream input, boolean closeInput, OutputStream output, boolean closeOutput) throws IOException {
        byte[] buffer = new byte[1024 * 128];
        int length = 0;

        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        if (closeInput) {
            input.close();
        }
        if (closeOutput) {
            output.close();
        }
    }

    /**
     * int配列をbit状態を保ってbyte配列へ変換する
     *
     * @param array 　変換元のint配列
     * @return コピーされたbyte配列
     */
    public static final byte[] toByteArray(int[] array) {
        byte[] result = new byte[array.length * 4];
        return toByteArray(array, result);
    }

    /**
     * int配列をbit状態を保ってbyte配列へ変換する
     *
     * @param array  変換元のint配列
     * @param result 変換先のbyte配列
     * @return resultオブジェクト
     */
    public static final byte[] toByteArray(int[] array, byte[] result) {
        for (int i = 0; i < array.length; ++i) {
            result[i * 4 + 0] = (byte) ((array[i] >> 24) & 0xff);
            result[i * 4 + 1] = (byte) ((array[i] >> 16) & 0xff);
            result[i * 4 + 2] = (byte) ((array[i] >> 8) & 0xff);
            result[i * 4 + 3] = (byte) ((array[i] >> 0) & 0xff);
        }
        return result;
    }

    /**
     * InputStreamから文字列へ変換する
     *
     * @param is    読み込み対象のstream
     * @param close closeする場合はtrue
     * @return 読み取った文字列
     * @throws IOException
     */
    public static String toString(InputStream is, boolean close) throws IOException {
        byte[] buffer = toByteArray(is, close);
        return new String(buffer);
    }

    /**
     * ファイルをbyte列 or null
     *
     * @param file ファイルパス
     * @return ファイルが読み込めればbyte配列
     */
    public static byte[] toByteArrayOrNull(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return toByteArray(is, true);
        } catch (Exception e) {
            close(is);
        }
        return null;
    }

    /**
     * ファイルを文字列 or null
     *
     * @param file ファイルパス
     * @return 文字列に変換できれば文字列
     */
    public static String toStringOrNull(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return toString(is, true);
        } catch (Exception e) {
            close(is);
        }
        return null;
    }

    /**
     * InputStreamを全てメモリ上に展開する。 isの長さがOOMにならないように調整すること。
     *
     * @param is    読み込み対象
     * @param close isを閉じるならtrue
     * @return 読み込んだbyte配列
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream is, boolean close) throws IOException {
        byte[] result = null;

        //! 1kbずつ読み込む。
        byte[] tempBuffer = new byte[1024 * 5];
        //! 元ストリームを読み取り
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int n = 0;
            while ((n = is.read(tempBuffer)) > 0) {
                baos.write(tempBuffer, 0, n);
            }
            result = baos.toByteArray();
            if (close) {
                is.close();
            }
        }

        return result;
    }

    /**
     * 指定箇所へファイルをコピーする。
     *
     * @param src コピー元
     * @param dst コピー先
     */
    public static final void copy(File src, File dst) throws IOException {
        mkdir(dst.getParentFile());
        InputStream srcStream = new FileInputStream(src);
        OutputStream dstStream = new FileOutputStream(dst);

        //! 適当なバッファサイズを決める。
        byte[] buffer = new byte[128 * 1024];
        int readed = 0;

        //! 読めなくなるまで読み込みを続ける
        while ((readed = srcStream.read(buffer)) > 0) {
            dstStream.write(buffer, 0, readed);
        }

        srcStream.close();
        dstStream.close();

        //! 最終変更日を修正する
        dst.setLastModified(src.lastModified());
    }

    /**
     * コピー先が存在しない、もしくはMD5が一致しない場合のみコピーを行い、それ以外はコピーを行わない
     *
     * @param src コピー元
     * @param dst コピー先
     * @throws IOException
     */
    public static final void copyOrUpdate(File src, File dst) throws IOException {
        if (!dst.isFile()) {
            // ファイルが存在しないからコピーする
            copy(src, dst);
            return;
        }

        String srcSHA1 = genSHA1(src);
        String dstSHA1 = genSHA1(dst);

        // 2つのSHA1が一致しないため、コピーする
        if (!srcSHA1.equals(dstSHA1)) {
            copy(src, dst);
        }
    }

    /**
     * ファイルからMD5を求める。
     *
     * @param file 生成元ファイル
     * @return MD5
     */
    public static String genMD5(File file) {
        try {

            FileInputStream is = new FileInputStream(file);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            {
                byte[] buffer = new byte[128 * 1024];
                int readed = 0;

                while ((readed = is.read(buffer)) > 0) {
                    md.update(buffer, 0, readed);
                }
            }
            is.close();
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
     * 末尾・先端のバイト列を用いた単純なハッシュを生成する
     * ファイルフォーマットによっては衝突の可能性が非常に高いため、利用する場合は十分に検討を行うこと。
     *
     * @param file        ファイルパス
     * @param checkLength 確認容量
     * @return ハッシュ値
     */
    public static String genShortHash(File file, int checkLength) {
        if (!file.isFile()) {
            return null;
        }

        // 十分に小さいファイルの場合は検証を行わずに返す
        if (file.length() < (checkLength * 2)) {
            return IOUtil.genSHA1(file);
        }

        try {
            String start = null;
            String end = null;
            // 先頭の任意バイトを読み込む
            {
                byte[] buffer = new byte[checkLength];
                FileInputStream is = new FileInputStream(file);
                is.read(buffer);
                is.close();
                start = EncodeUtil.genMD5(buffer);
            }
            // 末尾の任意バイトを読み込む
            {
                byte[] buffer = new byte[checkLength];
                FileInputStream is = new FileInputStream(file);
                is.skip(file.length() - checkLength);
                is.read(buffer);
                is.close();
                end = EncodeUtil.genMD5(buffer);
            }

            return start + end;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ファイルからSHA1を求める。
     *
     * @param file 生成するファイル
     * @return SHA1
     */
    public static String genSHA1(File file) {
        try {

            FileInputStream is = new FileInputStream(file);
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            {
                byte[] buffer = new byte[128 * 1024];
                int readed = 0;

                while ((readed = is.read(buffer)) > 0) {
                    md.update(buffer, 0, readed);
                }
            }
            is.close();
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
     * ディレクトリを再帰的に削除する。
     *
     * @param root 削除するディレクトリ
     */
    public static void delete(File root) {
        if (root.isFile()) {
            root.delete();
            return;
        }
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                delete(file);
            }
        }
        root.delete();
    }

    /**
     * ファイル拡張子を取得する。
     *
     * @param fileName ファイル名
     * @return ドットを含まない拡張子
     */
    public static String getFileExt(String fileName) {
        if (fileName == null)
            return "";
        int point = fileName.lastIndexOf('.');
        if (point != -1) {
            return fileName.substring(point + 1);
        } else {
            // fileName = "";
            return "";
        }
    }

    /**
     * 拡張子付のファイル名からファイル名のみを抜き出す
     *
     * @param fileName ファイル名
     * @return 拡張子を取り除いたファイル名
     */
    public static String getFileName(final String fileName) {
        if (fileName == null)
            return "";
        int point = fileName.lastIndexOf('.');
        if (point != -1) {
            return fileName.substring(0, point);
        } else {
            // fileName = "";
            return "";
        }
    }

    /**
     * ファイルパスからSHA1を得る。
     *
     * @param file 生成するファイル
     * @return SHA1
     */
    public static String genPathSHA1(final File file) {
        String path = file.getAbsolutePath();
        path = normalizeFileName(path);
        return EncodeUtil.genSHA1(path.getBytes());
    }

    /**
     * カレントディレクトリのパスを取得する。
     *
     * @return 現在のディレクトリ
     */
    public static String getCurrentDirectoryPath() {
        return (new File(".").getAbsoluteFile().getParent());
    }

    /**
     * 現在のディレクトリを取得する
     *
     * @return カレントディレクトリ
     */
    public static File getCurrentDirectory() {
        return (new File(".")).getAbsoluteFile().getParentFile();
    }

    /**
     * ファイルを名前順にソートする。
     * inの中身がソートされるため、戻り値は同じ参照となる。
     *
     * @param files ファイルを名前順にソートする
     * @return filesオブジェクト
     */
    public static File[] sort(File[] files) {
        if (files == null) {
            return new File[0];
        }

        List<File> temp = new ArrayList<File>();
        for (File f : files) {
            temp.add(f);
        }

        Collections.sort(temp, new Comparator<File>() {
            @Override
            public int compare(File object1, File object2) {
                String f0 = object1.getAbsolutePath();
                String f1 = object2.getAbsolutePath();
                return StringUtil.compareString(f0, f1);
            }
        });

        for (int i = 0; i < files.length; ++i) {
            files[i] = temp.get(i);
        }

        return files;
    }

    /**
     * 比較等の処理を行うために文字列を正規化する
     *
     * @param origin 元の文字列
     * @return 比較用に変換した文字列
     */
    public static String normalizeFileName(String origin) {
        origin = StringUtil.zenkakuEngToHankakuEng(origin);
        origin = StringUtil.macStringToWinString(origin);

        while (origin.indexOf('?') >= 0) {
            origin = origin.replace('?', '？');
        }
        return origin;
    }

    /**
     * そこまでの道を含めてディレクトリを作成する。
     * {@link File#mkdirs()} を使用すべき
     *
     * @param dir
     * @return
     */
    @Deprecated
    public static File mkdir(File dir) {
        // 作成済みだったら何もしない
        if (dir.isDirectory()) {
            return dir;
        }
        File parent = dir.getAbsoluteFile().getParentFile();
        if (parent.isDirectory()) {
            dir.mkdir();
        } else {
            // 親が作られてなかったら作る
            mkdir(parent);
        }

        return dir;
    }

    /**
     * parentからtargetに到達するまでの全てのファイルを取得する。
     * 戻り値にtargetとparentも含まれる。
     * 階層が上にあるFileがindexの0に近くなる。
     *
     * @param target 探すファイル
     * @param parent 親ディレクトリ
     * @return 探索できた全てのファイル
     */
    public static List<File> getDirectoryRoute(File target, File parent) {
        List<File> result = new LinkedList<>();
        File current = target;
        while (!equals(current, parent)) {
            result.add(0, current);
            current = current.getParentFile();
        }
        result.add(0, parent);
        return result;
    }

    /**
     * ディレクトリの中身を完全削除する。
     * dirフォルダ自体は残る。
     *
     * @param dir 削除対象のディレクトリ
     * @return dirオブジェクト
     */
    public static File cleanDirectory(File dir) {
        if (dir.isFile()) {
            return null;
        }
        delete(dir);
        dir.mkdirs();
        return dir;
    }

    /**
     * 同じ内容を指していた場合はtrue
     *
     * @param a ファイルA
     * @param b ファイルB
     * @return 同じパスである場合はtrue
     */
    public static boolean equals(File a, File b) {
        if (a == null || b == null) {
            return false;
        }
        return a.getAbsolutePath().equals(b.getAbsolutePath());
    }

    /**
     * GZIPバッファであればtrueを返却する。
     * <br>
     * ただし、これはヘッダのみをチェックするため、簡易的なチェックしか行えない。
     *
     * @param buffer ヘッダ
     * @return GZIPであればtrue
     */
    public static boolean isGzip(byte[] buffer) {
        return buffer.length > 2 && buffer[0] == (byte) 0x1F && buffer[1] == (byte) 0x8B;
    }

    /**
     * rawバッファをGZIPに圧縮して返却する
     *
     * @param raw 元ファイル配列
     * @return GZIP配列
     */
    public static byte[] compressGzip(byte[] raw) {
        try {
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(bufferStream);
            gzipOutputStream.write(raw);

            gzipOutputStream.flush();
            gzipOutputStream.close();

            return bufferStream.toByteArray();
        } catch (Exception e) {
            LogUtil.log(e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * GZIPバッファをデコードする。失敗したらnullを返却する。
     *
     * @param gzip GZIPバッファ
     * @return 解凍したバッファ
     */
    public static byte[] decompressGzipOrNull(byte[] gzip) {
        try {
            GZIPInputStream is = new GZIPInputStream(new ByteArrayInputStream(gzip));
            return toByteArray(is, true);
        } catch (Exception e) {
            LogUtil.log(e);
            return null;
        }
    }

    /**
     * InputStream経由でUnzipを行う
     *
     * @param stream       読み込み対象
     * @param outDirectory 書き込み対象
     * @throws IOException
     */
    public static void unzip(InputStream stream, File outDirectory) throws IOException {
        ZipInputStream is = new ZipInputStream(stream);
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            File outFile = outDirectory;
            List<String> path = Util.convert(entry.getName().split("/"));

            // "/"で区切られていたら、パスを追加する
            while (path.size() > 1) {
                outFile = new File(outFile, path.remove(0));
            }

            // パスを生成する
            outFile.mkdirs();

            // ファイル名を確定する
            outFile = new File(outFile, path.get(0));
            LogUtil.log("  unzip(%s)", outFile.getAbsolutePath());

            if (!entry.isDirectory()) {
                // ファイルへ書き込む
                FileOutputStream os = new FileOutputStream(outFile);
                copyTo(is, false, os, false);
                os.close();
            }
        }
    }

    /**
     * ZIPの解凍を行う
     *
     * @param zipFile      ZIPファイル
     * @param outDirectory 解凍ディレクトリ
     * @throws IOException
     */
    public static void unzip(File zipFile, File outDirectory) throws IOException {
        InputStream is = new FileInputStream(zipFile);
        try {
            unzip(is, outDirectory);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Streamを閉じる
     *
     * @param is 対象
     */
    public static void close(InputStream is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (Exception e) {
        }
    }


    public static void close(RandomAccessFile raf) {
        if (raf == null) {
            return;
        }

        try {
            raf.close();
        } catch (Exception e) {
        }
    }

    /**
     * Streamを閉じる
     *
     * @param os 対象
     */
    public static void close(OutputStream os) {
        if (os == null) {
            return;
        }

        try {
            os.close();
        } catch (Exception e) {

        }
    }

    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    public static boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }
}