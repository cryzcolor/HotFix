package com.hotfixlib;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

/**
 * Created by wally.yan on 2015/12/1.
 */
public class Utils {

    /**
     * 从assert中复制文件到sd卡
     *
     * @param context
     * @param filename
     */
    public static void copyAssertFileToSD(Context context, String filename) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(context.getDir("dex", Context.MODE_PRIVATE), filename);
            if (file.exists()) {
                file.delete();
            }
            inputStream = context.getAssets().open(filename);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 合并数组
     *
     * @param obj
     * @param obj2
     * @return
     */
    public static Object combineArray(Object obj, Object obj2) throws IllegalArgumentException {
        Class componentType = obj.getClass().getComponentType();
        Class componentType2 = obj2.getClass().getComponentType();
        if (componentType != componentType2) {
            throw new IllegalArgumentException("合并双方的类型不相同，需要的是：" + componentType.getName() + ",合并的是：" + componentType2.getName());
        }
        int length = Array.getLength(obj2);
        Object newInstance = Array.newInstance(componentType, Array.getLength(obj) + length);
        System.arraycopy(obj2, 0, newInstance, 0, length);
        System.arraycopy(obj, 0, newInstance, length, Array.getLength(obj));
        return newInstance;
    }

    /**
     * 把item添加在数组第一位置
     *
     * @param array
     * @param item
     * @return
     */
    public static Object appendArray(Object array, Object item) throws IllegalArgumentException {
        Class componentType = array.getClass().getComponentType();
        Class itemComponentType = item.getClass().getComponentType();
        if (componentType != itemComponentType) {
            throw new IllegalArgumentException("添加的成员类型不正确，需要的是：" + componentType.getName() + ",添加的是：" + itemComponentType.getName());
        }
        int length = Array.getLength(array);
        Object newInstance = Array.newInstance(componentType, length + 1);
        Array.set(newInstance, 0, item);
        for (int i = 1; i < length + 1; i++) {
            Array.set(newInstance, i, Array.get(array, i - 1));
        }
        return newInstance;
    }
}
