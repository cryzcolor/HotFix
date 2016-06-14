package com.hotfix;

import java.lang.reflect.Field;

/**
 * Created by wally.yan on 2015/11/30.
 */
public class ReflectionUtils {

    /**
     *反射获取属性
     * @param obj 类对象
     * @param cls
     * @param str 属性名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getField(Object obj, Class cls, String str)
            throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        return declaredField.get(obj);
    }

    /**
     * 反射设置属性
     * @param obj 类对象
     * @param cls
     * @param str  属性名称
     * @param obj2 属性对象
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setField(Object obj, Class cls, String str, Object obj2)
            throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cls.getDeclaredField(str);
        declaredField.setAccessible(true);
        declaredField.set(obj, obj2);
    }
}
