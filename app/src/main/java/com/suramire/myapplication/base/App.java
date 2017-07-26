package com.suramire.myapplication.base;

import android.app.Application;

/**
 * Created by Suramire on 2017/7/20.
 */

public class App extends Application {
    public static App app;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getContext(){
        return app;
    }
}
