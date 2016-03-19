package com.eaglesakura.util;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReflectionUtilTest {

    @Test
    public void 指定されたAnnotationのFieldを列挙する() {
        List<Field> fields = ReflectionUtil.listAnnotationFields(BaseAnnotationClass.class, SampleAnnotation.class);
        assertNotNull(fields);
        assertEquals(fields.size(), 4);
    }

    @Test
    public void 継承されたClassでFieldを列挙する() {
        List<Field> fields = ReflectionUtil.listAnnotationFields(SubAnnotationClass.class, SampleAnnotation.class);
        assertNotNull(fields);
        assertEquals(fields.size(), 8);
    }

    @Test
    public void 指定されたAnnotationのMethodを列挙する() {
        List<Method> fields = ReflectionUtil.listAnnotationMethods(BaseAnnotationClass.class, SampleAnnotation.class);
        assertNotNull(fields);
        assertEquals(fields.size(), 4);
    }

    @Test
    public void 継承されたClassでMethodを列挙する() {
        List<Method> fields = ReflectionUtil.listAnnotationMethods(SubAnnotationClass.class, SampleAnnotation.class);
        assertNotNull(fields);
        assertEquals(fields.size(), 8);
    }

    @Test
    public void インスタンス生成() {
        assertNotNull(ReflectionUtil.newInstanceOrNull(Object.class));
        assertNotNull(ReflectionUtil.newInstanceOrNull(ArrayList.class));

        assertNotNull(ReflectionUtil.getConstructorOrNull(ArrayList.class));
        assertNotNull(ReflectionUtil.getConstructorOrNull(ArrayList.class, int.class));
        assertNotNull(ReflectionUtil.newInstanceOrNull(ReflectionUtil.getConstructorOrNull(ArrayList.class)));
        assertNotNull(ReflectionUtil.newInstanceOrNull(ReflectionUtil.getConstructorOrNull(ArrayList.class, int.class), 128));
    }

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

    public static class BaseAnnotationClass {
        @SampleAnnotation
        private int value0;

        @SampleAnnotation
        int value1;

        @SampleAnnotation
        protected int value2;

        @SampleAnnotation
        public int value3;

        @SampleAnnotation
        private void method0() {
        }

        @SampleAnnotation
        void method1() {
        }

        @SampleAnnotation
        protected void method2() {
        }

        @SampleAnnotation
        public void method3() {
        }

    }

    public static class SubAnnotationClass extends BaseAnnotationClass {
        @SampleAnnotation
        private int value4;

        @SampleAnnotation
        int value5;

        @SampleAnnotation
        protected int value6;

        @SampleAnnotation
        public int value7;

        @SampleAnnotation
        @Override
        void method1() {
        }

        @SampleAnnotation
        private void method5() {
        }

        @SampleAnnotation
        void method6() {
        }

        @SampleAnnotation
        protected void method7() {
        }

        @SampleAnnotation
        public void method8() {
        }

    }
}
