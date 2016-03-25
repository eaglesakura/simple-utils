package com.eaglesakura.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EncodeUtilTest {
    @Test
    public void URLエンコードとデコードが正常に行える() throws Exception {
        String[] CHECK_TABLE = {
                "ABC",
                "あいうえお",
                "諸行無常",
                "a@eltaweaoiu"
        };

        for (String value : CHECK_TABLE) {
            String encode = EncodeUtil.toUrl(value);
            String decode = EncodeUtil.fromUrl(encode);
            assertEquals(value, decode);
        }
    }
}
