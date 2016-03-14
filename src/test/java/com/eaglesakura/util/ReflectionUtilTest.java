package com.eaglesakura.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilTest {

    @Test
    public void インスタンス継承チェック() {
        assertTrue(ReflectionUtil.instanceOf(new ArrayList(), List.class));
        assertTrue(ReflectionUtil.instanceOf(true, Boolean.class));
        assertFalse(ReflectionUtil.instanceOf(true, boolean.class));
        assertFalse(ReflectionUtil.instanceOf(this, List.class));
        assertFalse(ReflectionUtil.instanceOf(this, null));
    }

    @Test
    public void List継承チェック() {
        assertFalse(ReflectionUtil.isListInterface(Object.class));
        assertTrue(ReflectionUtil.isListInterface(List.class));
        assertTrue(ReflectionUtil.isListInterface(ArrayList.class));
        assertTrue(ReflectionUtil.isListInterface(LinkedList.class));
    }

}
