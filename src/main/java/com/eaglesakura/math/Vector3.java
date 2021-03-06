package com.eaglesakura.math;

/**
 * XYZの3次元ベクトルを管理する
 */
public final class Vector3 {
    /**
     * X成分。
     */
    public float x = 0.0f;
    /**
     * Y成分。
     */
    public float y = 0.0f;
    /**
     * Z成分。
     */
    public float z = 0.0f;

    /**
     * ゼロ初期化したベクトルを作成する。
     */
    public Vector3() {
    }

    /**
     * 値を指定して初期化する。
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * コピーを行う。
     */
    public Vector3(Vector3 origin) {
        set(origin);
    }

    /**
     * 値のコピーを行う。
     */
    public void set(Vector3 origin) {
        x = origin.x;
        y = origin.y;
        z = origin.z;
    }

    /**
     * ベクトル乗算を行う。
     */
    public void mul(float _x, float _y, float _z) {
        x *= _x;
        y *= _y;
        z *= _z;
    }

    /**
     * 値のコピーを行う。
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 内積を取得する。
     */
    public float dot(Vector3 v) {
        return (x * v.x) + (y * v.y) + (z * v.z);
    }

    /**
     * 内積を取得する。
     */
    public float dot(float _x, float _y, float _z) {
        return (x * _x) + (y * _y) + (z * _z);
    }

    /**
     * 外積を取得する。
     */
    public Vector3 cross(Vector3 v, Vector3 result) {
        result.set((y * v.z) - (z * v.y), (z * v.x) - (x * v.z), (x * v.y) - (y * v.x));
        return result;
    }

    /**
     * 外積を求め、このインスタンスに格納する。
     */
    public void cross(float _x, float _y, float _z) {
        set((y * _z) - (z * _y), (z * _x) - (x * _z), (x * _y) - (y * _x));
    }

    /**
     * v0 - v1を計算し、このインスタンスに格納する。
     */
    public void sub(Vector3 v0, Vector3 v1) {
        x = v0.x - v1.x;
        y = v0.y - v1.y;
        z = v0.z - v1.z;
    }

    /**
     * ベクトル要素を加算する。
     */
    public void add(Vector3 v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    /**
     * ベクトルの要素を加算する。
     */
    public void add(float _x, float _y, float _z) {
        x += _x;
        y += _y;
        z += _z;
    }

    /**
     * 長さを取得する。
     */
    public float length() {
        return (float) Math.sqrt((double) ((x * x) + (y * y) + (z * z)));
    }

    /**
     * ベクトル間の距離を取得する。
     */
    public float length(Vector3 v) {
        float _x = x - v.x;
        float _y = y - v.y;
        float _z = z - v.z;
        return (float) Math.sqrt((double) ((_x * _x) + (_y * _y) + (_z * _z)));
    }

    /**
     * ベクトルの長さを正規化する。
     */
    public void normalize() {
        final float len = length();
        x /= len;
        y /= len;
        z /= len;
    }

    /**
     * 2つのベクトルを保管する。<BR>
     * leapは０．０ｆ～１．０ｆである必要がある。
     */
    public static void leap(Vector3 v0, Vector3 v1, float leap, Vector3 result) {
        result.x = (v1.x * leap) + (v0.x * (1.0f - leap));
        result.y = (v1.y * leap) + (v0.y * (1.0f - leap));
        result.z = (v1.z * leap) + (v0.z * (1.0f - leap));
    }

    /**
     * 整合性確認。
     */
    @Override
    public boolean equals(Object obj) {
        Vector3 v = (Vector3) obj;
        return x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public float max() {
        return Math.max(Math.max(x, y), z);
    }

    public float min() {
        return Math.min(Math.min(x, y), z);
    }

    /**
     * 文字列変換を行う。
     */
    @Override
    public String toString() {
        return "V( " + x + ", " + y + ", " + z + " )";
    }
}
