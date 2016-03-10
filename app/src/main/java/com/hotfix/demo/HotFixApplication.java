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
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        File dexPath = new File(getFilesDir(), "hack_dex.jar");
        Utils.copyAssertFileToSD(this, "hack_dex.jar");
        HotFix.loadPatch(this, dexPath.getAbsolutePath());

        dexPath = new File(getFilesDir(), "patch_dex.jar");
        Utils.copyAssertFileToSD(this, "patch_dex.jar");
        HotFix.loadPatch(this, dexPath.getAbsolutePath());
    }
}
