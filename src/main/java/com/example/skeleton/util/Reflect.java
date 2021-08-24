package com.example.skeleton.util;

public class Reflect {
    static public Object GetFieldValue(Object obj, String field) throws NoSuchFieldException, IllegalAccessException {
        var f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        return f.get(obj);
    }

    static public void SetFieldValue(Object obj, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        var f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, value);
    }
}
