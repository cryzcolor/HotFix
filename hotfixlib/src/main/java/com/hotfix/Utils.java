package com.hotfix;

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
            File file = new File(context.getFilesDir(), filename);
            if (file.exists()) {
                file.delete();
            }
            inputStream = context.getAssets().open(filename);
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 合并数组
     *
     * @param firstArray
     * @param secondArray
     * @return
     */
    public static Object combineArray(Object firstArray, Object secondArray) {
        Class<?> localClass = firstArray.getClass().getComponentType();
        int firstArrayLength = Array.getLength(firstArray);
        int allLength = firstArrayLength + Array.getLength(secondArray);
        Object result = Array.newInstance(localClass, allLength);
        for (int k = 0; k < allLength; ++k) {
            if (k < firstArrayLength) {
                Array.set(result, k, Array.get(firstArray, k));
            } else {
                Array.set(result, k, Array.get(secondArray, k - firstArrayLength));
            }
        }
        return result;
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
        int length = Array.getLength(array);
        Object newInstance = Array.newInstance(componentType, length + 1);
        Array.set(newInstance, 0, item);
        for (int i = 1; i < length + 1; i++) {
            Array.set(newInstance, i, Array.get(array, i - 1));
        }
        return newInstance;
    }
}
