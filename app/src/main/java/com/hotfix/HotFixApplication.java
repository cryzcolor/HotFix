package com.hotfix;

import android.app.Application;

import com.hotfixlib.HotFix;

/**
 * Created by wally.yan on 2015/11/30.
 */
public class HotFixApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HotFix.init(this);
    }
}
