package com.eaglesakura.collection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by eaglesakura on 2016/09/05.
 */
public class StringFlagTest {

    @Test
    public void フラグが生成できる() throws Throwable {
        StringFlag flag = new StringFlag();
        flag.add("this");
        flag.add(2L);

        assertEquals(flag.toString(), ",2,this,");
    }

    @Test
    public void フラグが配列から生成できる() throws Throwable {
        StringFlag flag = new StringFlag(new Object[]{"this", 2L});

        assertEquals(flag.toString(), ",2,this,");
    }

    @Test
    public void 追加された順序によらず同じフラグ文字列が生成される() throws Throwable {
        StringFlag flag = new StringFlag();
        flag.add(2L);
        flag.add("this");
        flag.add("this");

        assertEquals(flag.toString(), ",2,this,");
    }

    @Test
    public void 検索文字列を生成できる() throws Throwable {
        StringFlag flag = new StringFlag();
        flag.add("this");
        assertEquals(flag.toLikeQuery(), "%,this,%");
    }

    @Test
    public void 複数個のフラグを適用した検索文字列を生成できる() throws Throwable {
        StringFlag flag = new StringFlag();
        flag.add("this");
        flag.add(2L);
        assertEquals(flag.toLikeQuery(), "%,2,%,this,%");
    }

    @Test
    public void フラグリストを解析する() throws Throwable {
        StringFlag flag = StringFlag.parse(",2,this,");
        assertTrue(flag.contains(2L));
        assertTrue(flag.contains("this"));
        assertFalse(flag.contains(","));
    }

    @Test
    public void フラグリストを加算する() throws Throwable {
        StringFlag flag = StringFlag.parse(",this,").add(StringFlag.parse(",2,"));
        assertTrue(flag.contains(2L));
        assertTrue(flag.contains("this"));
        assertFalse(flag.contains(","));
    }

    @Test
    public void フラグの和を取得する() throws Throwable {
        assertEquals(StringFlag.or(",this,", ",2,"), ",2,this,");
        assertEquals(StringFlag.or(",2,this,", ",2,"), ",2,this,");
        assertEquals(StringFlag.or(",2,this,", null), ",2,this,");
        assertEquals(StringFlag.or(null, null), "");
        assertEquals(StringFlag.or("2", "this"), ",2,this,");
    }
}