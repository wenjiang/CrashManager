/*
 * 深圳市有信网络技术有限公司
 * Copyright (c) 2016 All Rights Reserved.
 */

package com.wenjiang.crashmanager;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 异常捕捉器
 */
public class CrashManager implements UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler; // 系统默认ERROR处理器
    private static CrashManager crashHandler;
    private String AndroidDeviceModel; // 手机型号

    public CrashManager() {
    }

    public static CrashManager getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashManager();
        }
        return crashHandler;
    }

    public void init(String AndroidDeviceModel) {
        this.AndroidDeviceModel = AndroidDeviceModel;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this); // 设置默认的错误处理器为自己
    }

    /**
     * ERROR处理器
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handlerException(ex) && mDefaultHandler != null) {
            // 如果用户没处理交给系统默认处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 如果用户以处理
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 输出异常栈
     * 
     * @param e
     * @return
     * @author: xiaozhenhua
     * @data:2013-1-9 下午4:52:10
     */
    private boolean handlerException(Throwable e) {
        if (e == null) {
            return false;
        }

        // 判断有信APK是否存在，如果存在则进行删除
        // UserData.getInstance().deleteAPK();

//        StackTraceElement[] stack = e.getStackTrace();
//        StringBuffer sbf = new StringBuffer();
//        sbf.append("device：" + this.AndroidDeviceModel + "\n");
//        // 栈头
//        sbf.append("error_head:" + e.toString() + "\n");
//        DLog.d(TAG, "ERROR_HEAD:" + e.toString());
//        // 栈体
//        for (int i = 0; i < stack.length; i++) {
//            DLog.d(TAG, "ERROR:" + stack[i]);
//            sbf.append(stack[i] + "\n");
//        }
        Log.e("CrashManager", "device: " + AndroidDeviceModel + ", crash handle exception!!!" + e);
        return false;
    }
}
