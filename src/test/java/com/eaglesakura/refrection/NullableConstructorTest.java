package com.eaglesakura.refrection;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NullableConstructorTest {
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
