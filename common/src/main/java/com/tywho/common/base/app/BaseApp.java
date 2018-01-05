package com.tywho.common.base.app;

import android.annotation.SuppressLint;
import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tywho.common.utils.Foreground;
import com.tywho.common.utils.SysUtils;
import com.tywho.common.utils.show.LogUtils;
import com.tywho.config.CommonConfig;
import com.tywho.dblibrary.helper.DbCore;

/**
 * Created by limit on 2017/3/30.
 */

public class BaseApp extends Application implements Thread.UncaughtExceptionHandler {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @SuppressLint("NewApi")
    public void init() {
        // 捕捉崩溃异常
        Thread.setDefaultUncaughtExceptionHandler(this);
        //内存溢出监控
        LeakCanary.install(this);
        //初始化默认信息
        configInit();

        //判断app是否处于前台
        Foreground.init(this);

        //数据库初始化
        DbCore.init(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        LogUtils.e(CommonConfig.APP_LOG, "错误：" + ex.getMessage(), ex);
        ex.printStackTrace();
        //崩溃，退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
//        RxBus.getDefault().post(10110,true);
    }

    private void configInit() {
        //初始化app信息
        CommonConfig.APPLICATION = this;
        CommonConfig.isDebug = SysUtils.isDebug(this);
        CommonConfig.API_URL = SysUtils.getConfigStr(this, "API_SERVER_URL");
        CommonConfig.WEB_URL = SysUtils.getConfigStr(this,"WEB_SERVER_URL");
        CommonConfig.VERSION = SysUtils.getBuildConfigValue(this, "VERSION_NAME");
        CommonConfig.VERSIONCODE = SysUtils.getBuildConfigValue(this, "VERSION_CODE");
        CommonConfig.APPTYPE = "android";
        CommonConfig.DEVICEID = SysUtils.getDeviceId(this);
        CommonConfig.CHANNEL = SysUtils.getBuildConfigValue(this, "FLAVOR");
        CommonConfig.MOBILEVERSION = SysUtils.getBuildVersion();
        CommonConfig.MOBILEMODEL = SysUtils.getPhoneModel();
        CommonConfig.MOBILEBRAND = SysUtils.getPhoneBrand();
        CommonConfig.SCREENWIDTH = SysUtils.getScreenWidth(this) + "";
        CommonConfig.SCREENHEIGHT = SysUtils.getScreenHeight(this) + "";
    }
}
