package org.elastos.hive.demo;

import android.support.multidex.MultiDexApplication;

import org.elastos.hive.demo.utils.Utils;

public class HiveApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        Utils.init(this);
        super.onCreate();
    }
}
