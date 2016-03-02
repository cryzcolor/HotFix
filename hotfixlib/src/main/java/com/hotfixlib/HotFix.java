package com.hotfixlib;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by wally.yan on 2015/11/30.
 */
public class HotFix {

    /**
     * 加载path
     *
     * @param context
     * @param patchDexFile
     */
    public static void loadPatch(Context context, String patchDexFile) {
        if (patchDexFile != null && new File(patchDexFile).exists()) {
            try {
                if (hasDexClassLoader()) {
                    injectAboveEqualApiLevel14(context, patchDexFile);
                } else {
                    injectBelowApiLevel14(context, patchDexFile);
                }
            } catch (Throwable th) {
            }
        }
    }

    private static boolean hasDexClassLoader() {
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 4.0以下系统
     *
     * @param context
     * @param dexPath
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @TargetApi(14)
    private static void injectBelowApiLevel14(Context context, String dexPath)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, context.getDir("dex", 0).getAbsolutePath(), dexPath, context.getClassLoader());
        ReflectionUtils.setField(pathClassLoader, PathClassLoader.class, "mPaths",
                Utils.appendArray(ReflectionUtils.getField(pathClassLoader, PathClassLoader.class, "mPaths"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class,
                        "mRawDexPath")
                ));
        ReflectionUtils.setField(pathClassLoader, PathClassLoader.class, "mFiles",
                Utils.combineArray(ReflectionUtils.getField(pathClassLoader, PathClassLoader.class, "mFiles"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class,
                        "mFiles")
                ));
        ReflectionUtils.setField(pathClassLoader, PathClassLoader.class, "mZips",
                Utils.combineArray(ReflectionUtils.getField(pathClassLoader, PathClassLoader.class, "mZips"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class,
                        "mZips")));
        ReflectionUtils.setField(pathClassLoader, PathClassLoader.class, "mDexs",
                Utils.combineArray(ReflectionUtils.getField(pathClassLoader, PathClassLoader.class, "mDexs"), ReflectionUtils.getField(dexClassLoader, DexClassLoader.class,
                        "mDexs")));
    }

    /**
     * 4.0以及以上系统
     *
     * @param context
     * @param dexPath
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void injectAboveEqualApiLevel14(Context context, String dexPath)
            throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        Object baseDexElements = getDexElements(getPathList(pathClassLoader));
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, context.getDir("dex", 0).getAbsolutePath(), dexPath, pathClassLoader);
        Object newDexElements = getDexElements(getPathList(dexClassLoader));
        Object allDexElements = Utils.combineArray(newDexElements, baseDexElements);
        Object pathList = getPathList(pathClassLoader);
        ReflectionUtils.setField(pathList, pathList.getClass(), "dexElements", allDexElements);
    }

    /**
     * 反射获取pathList
     *
     * @param obj
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getPathList(Object obj) throws ClassNotFoundException, NoSuchFieldException,
            IllegalAccessException {
        return ReflectionUtils.getField(obj, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 反射获取dexElements
     *
     * @param obj
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getDexElements(Object obj) throws NoSuchFieldException, IllegalAccessException {
        return ReflectionUtils.getField(obj, obj.getClass(), "dexElements");
    }


}
