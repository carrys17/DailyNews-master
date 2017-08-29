package com.example.shang.dailynews_master.app;

import android.app.Application;

import com.example.shang.dailynews_master.BuildConfig;

import org.xutils.x;

/**
 * Created by shang on 2017/7/27.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
