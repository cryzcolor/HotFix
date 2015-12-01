package com.hotfixlib;

import android.content.Context;

/**
 * Created by wally.yan on 2015/11/30.
 */
public class HotFix {

    public static void init(Context context) {
        try {
            Object mPackageInfo = ReflectionUtils.getFieldValue(context, "mBase.mPackageInfo");
            HotFixClassLoader classLoader = new HotFixClassLoader(context.getClassLoader(), context);
            ReflectionUtils.setFieldValue(mPackageInfo, "mClassLoader", classLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
