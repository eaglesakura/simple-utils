package com.eaglesakura.refrection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

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

    @Test(expected = Error.class)
    public void コンストラクタの引数がマッチしない場合はエラーを投げる() throws Throwable {
        NullableConstructor<ArrayList> constructor = NullableConstructor.get(ArrayList.class, int.class);
        assertNotNull(constructor);
        assertNotNull(constructor.getConstructor());

        constructor.newInstance(); // ここでエラーが投げられるため、中断される

        fail(); // ここに到達したら失敗である
    }

    @Test
    public void 無効なコンストラクタでも例外を投げない() {
        NullableConstructor<ArrayList> constructor = NullableConstructor.get(ArrayList.class, Random.class);
        assertNotNull(constructor);
        assertNull(constructor.getConstructor());
        assertNotNull(constructor.newInstance(128));
    }
}
