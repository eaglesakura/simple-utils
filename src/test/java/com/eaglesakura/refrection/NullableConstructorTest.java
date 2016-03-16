package com.eaglesakura.refrection;

import com.eaglesakura.util.ReflectionUtil;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NullableConstructorTest {

    public enum TestEnum {
        Value0,
        Value1,
    }

    @Test
    public void valueOfでEnumを取得する() {
        assertEquals(ReflectionUtil.valueOf(TestEnum.class, "Value0"), TestEnum.Value0);
        assertNull(ReflectionUtil.valueOf(TestEnum.class, "Fail"));
        assertEquals(ReflectionUtil.valueOf(TestEnum.class, "Fail", TestEnum.Value1), TestEnum.Value1);
    }

    @Test
    public void 引数なしコンストラクタでインスタンスが生成できる() {
        NullableConstructor<ArrayList> constructor = NullableConstructor.get(ArrayList.class);
        assertNotNull(constructor);
        assertNotNull(constructor.getConstructor());
        assertNotNull(constructor.newInstance());
    }

    @Test
    public void 引数ありコンストラクタでインスタンスが生成できる() {
        NullableConstructor<ArrayList> constructor = NullableConstructor.get(ArrayList.class, int.class);
        assertNotNull(constructor);
        assertNotNull(constructor.getConstructor());
        assertNotNull(constructor.newInstance(128));
    }

    @Test
    public void コンストラクタの引数がマッチしない場合はnullを返却する() throws Throwable {
        NullableConstructor<ArrayList> constructor = NullableConstructor.get(ArrayList.class, int.class);
        assertNotNull(constructor);
        assertNotNull(constructor.getConstructor());
        assertNull(constructor.newInstance());
    }
}
