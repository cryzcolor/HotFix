package com.hotfixlib;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by wally.yan on 2015/11/30.
 */
public class HotFixClassLoader extends ClassLoader {
    private Context context;

    public HotFixClassLoader(ClassLoader parentLoader, Context context) {
        super(parentLoader);
        this.context = context;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class cls = findLoadedClass(className);
        Log.i("1111",className);
        if ("com.hotfix.Test".equals(className)) {
            cls = loadFixClass(context);
        }
        if (cls != null) {
            return cls;
        }
        return super.loadClass(className, resolve);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        Log.i("find",className);
        return super.findClass(className);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        Class cls = findLoadedClass(className);
        Log.i("22222",className);
        if (cls == null && "com.hotfix.Test".equals(className)) {
            cls = loadFixClass(context);
        }
        if (cls != null) {
            return cls;
        }
        return super.loadClass(className);
    }

    private Class loadFixClass(Context context) {
        if (context == null) {
            return null;
        }
        Utils.copyAssertFileToSD(context, "test_dex.jar", "test_dex.jar");
        File file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "test_dex.jar");
        final File optimizedDexOutputPath = context.getDir("dex", Context.MODE_PRIVATE);
        DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(),
                optimizedDexOutputPath.getAbsolutePath(), null, context.getClassLoader());
        try {
            return classLoader.loadClass("com.hotfix.Test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
