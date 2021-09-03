package com.cryptofication.classes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class ContextApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public void onCreate() {
        super.onCreate();
        ContextApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ContextApplication.context;
    }
}
