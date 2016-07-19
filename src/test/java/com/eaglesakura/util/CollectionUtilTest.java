package com.eaglesakura.util;

import com.eaglesakura.lambda.Matcher1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionUtilTest {

    @Test
    public void フィルタが正常に動作する() throws Throwable {
        List<Integer> filter = CollectionUtil.filter(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), new ArrayList<Integer>(), new Matcher1<Integer>() {
            @Override
            public boolean match(Integer it) throws Throwable {
                return (int) it == 10;
            }
        });
        assertNotNull(filter);
        assertEquals(filter.size(), 1);
        assertEquals((int)filter.get(0), 10);
    }
}