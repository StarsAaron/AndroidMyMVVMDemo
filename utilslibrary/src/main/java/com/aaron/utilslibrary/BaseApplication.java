package com.aaron.utilslibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;

import com.aaron.utilslibrary.utils.Utils;
import com.aaron.utilslibrary.utils.android.ActivityUtils;

/**
 * 基础功能封装初始化
 * <p>
 * （1）管理activity生命周期
 * （2）初始化工具类，实质是初始化工具类要用到的上下文Context
 */
public abstract class BaseApplication extends Application {
    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (isAppMainProcess(appMainProcessName())) {
            onceInit();
            // 注册Activity生命周期监听
            registerActivityLifecycle(this);
            //初始化工具类
            Utils.init(this);
            //        initCrash();
        }
    }

    /**
     * 判断是不是UI主进程，因为有些东西只能在UI主进程初始化
     */
    public final boolean isAppMainProcess(String appMainProcessName) {
        if(TextUtils.isEmpty(appMainProcessName)){
            return false;
        }
        try {
            int pid = android.os.Process.myPid();
            String process = getProcessNameByPID(this, pid);
            if (TextUtils.isEmpty(process)) {
                return true;
            } else if (appMainProcessName.equalsIgnoreCase(process)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 根据Pid得到进程名
     */
    public final String getProcessNameByPID(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    /**
     * 多个进程只在主进程初始化一次的内容
     */
    public abstract void onceInit();

    /**
     * 返回主进程名，用于解决多线程多次初始化问题
     * @return
     */
    public abstract String appMainProcessName();

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    public static synchronized void registerActivityLifecycle(@NonNull Application application) {

        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityUtils.addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityUtils.removeActivity(activity);
            }
        });


    }

// 在子类application中配置崩溃处理
//    private void initCrash() {
//        CaocConfig.Builder.create()
//                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
//                .enabled(true) //是否启动全局异常捕获
//                .showErrorDetails(true) //是否显示错误详细信息
//                .showRestartButton(true) //是否显示重启按钮
//                .trackActivities(true) //是否跟踪Activity
//                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
//                .errorDrawable(R.drawable.customactivityoncrash_error_image) //错误图标
//                .restartActivity(LoginActivity.class) //重新启动后的activity
////                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
////                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
//                .apply();
//    }

    /**
     * 获得当前app运行的Application
     */
    public static Application getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return sInstance;
    }

    /**
     * 结束应用
     */
    public static void exitApp(){
        ActivityUtils.removeAllActivity();
//        exit(0);
    }
}
