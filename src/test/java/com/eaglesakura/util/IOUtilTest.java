package com.eaglesakura.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class IOUtilTest {
    @Test
    public void listFilesでnullが返却されない() {
        assertNotEquals(IOUtil.listFiles(new File("src")).length, 0);   //  存在するディレクトリは正しく返す
        assertEquals(IOUtil.listFiles(new File("build.gradle")).length, 0); // ファイルを指定しても問題ない
        assertEquals(IOUtil.listFiles(new File("taetkaewtwqetq")).length, 0);   // 存在しないディレクトリも問題ない
    }
}
