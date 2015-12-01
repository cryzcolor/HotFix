package com.hotfixlib;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by wally.yan on 2015/12/1.
 */
public class Utils {

    /**
     * 从assert中复制文件到sd卡
     * @param context
     * @param filename
     * @param des
     */
    public static void copyAssertFileToSD(Context context, String filename, String des) {
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    .toString() + File.separator + des);
            if (file.exists()) {
                return;
            }
            InputStream inputStream = context.getAssets().open(filename);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
