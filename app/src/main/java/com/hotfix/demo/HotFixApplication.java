package com.hotfix.demo;

import android.app.Application;
import android.content.Context;

import com.hotfix.HotFix;
import com.hotfix.Utils;

import java.io.File;

/**
 * Created by wally.yan on 2015/11/30.
 */
public class HotFixApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "hackdex_dex.jar");
        Utils.copyAssertFileToSD(getApplicationContext(), "hack_dex.jar");
        HotFix.loadPatch(getApplicationContext(), dexPath.getAbsolutePath());

        try {
            this.getClassLoader().loadClass("com.hackdex.HackDex");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "patch_dex.jar");
        Utils.copyAssertFileToSD(this.getApplicationContext(), "patch_dex.jar");
        HotFix.loadPatch(this, dexPath.getAbsolutePath());
    }
}
