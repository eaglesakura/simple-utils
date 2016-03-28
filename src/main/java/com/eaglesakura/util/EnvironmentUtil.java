package com.eaglesakura.util;

public class EnvironmentUtil {
    private static Boolean sRobolectric;
    private static Boolean sAndroid;

    public static boolean isRunningRobolectric() {
        if (sRobolectric == null) {
            try {
                sRobolectric = Class.forName("org.robolectric.Robolectric") != null;
            } catch (Exception e) {
                sRobolectric = false;
            }
        }

        return sRobolectric;
    }

    public static boolean isRunningAndroid() {
        if (sAndroid == null) {
            try {
                sAndroid = Class.forName("android.app.Application") != null;
            } catch (Exception e) {
                sAndroid = false;
            }
        }
        return sAndroid;
    }
}
