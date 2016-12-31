package com.example.administrator.netspeedtest;

import android.app.Application;

import com.example.administrator.netspeedtest.utils.Utils;

/**
 * Created by Administrator on 2016/12/26.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        //测试git命令 git add .
    }
}
