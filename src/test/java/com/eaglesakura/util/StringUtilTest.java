package com.eaglesakura.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class StringUtilTest {

    @Test
    public void base64変換が行える() throws Exception {
        byte[] buffer = "this is test".getBytes();
        String encoded = StringUtil.toString(buffer);
        assertNotNull(encoded);
        assertNotEquals(encoded, "");
        byte[] decoded = StringUtil.toByteArray(encoded);
        assertArrayEquals(buffer, decoded);
    }

    @Test
    public void ハッシュを計算する() throws Exception {
        {
            final Set<Long> ALL_HASH = new HashSet<>();
            final int LOOP_NUM = 4096;
            for (int i = 0; i < LOOP_NUM; ++i) {
                ALL_HASH.add(StringUtil.getHash64(RandomUtil.randString()));
            }
            assertEquals(ALL_HASH.size(), LOOP_NUM);
        }
        {
            final Set<Long> ALL_HASH = new HashSet<>();
            final int LOOP_NUM = 4096;
            for (int i = 0; i < LOOP_NUM; ++i) {
                String str = StringUtil.format("Value[%07d]", i);
                long hash = StringUtil.getHash64(str);
                LogUtil.log("Str(%s) Hash(%X)", str, hash);
                ALL_HASH.add(hash);
            }
            assertEquals(ALL_HASH.size(), LOOP_NUM);
        }
    }
}
