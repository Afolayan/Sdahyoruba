package com.jcedar.sdahyoruba.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Afolayan on 23/1/2016.
 */
public class SdahYoruba extends MultiDexApplication{


    public SdahYoruba() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
