package com.aaron.mymvvm
import com.aaron.mymvvm.mvvmExample.LoginActivity
import com.aaron.mymvvm.normalExample.utils.AppConfig
import com.aaron.utilslibrary.BaseApplication
import com.aaron.utilslibrary.crash.CaocConfig
import com.aaron.utilslibrary.utils.KLog

class AppApplication : BaseApplication() {
    /**
     * 多个进程只在主进程初始化一次的内容
     */
    override fun onceInit() {
        //是否开启打印日志
        KLog.init(BuildConfig.DEBUG)
        initCrash()
        // 要使用com.aaron.mvvmlibrary包里面的工具类要继承BaseApplication，或者
        // 调用Utils.init(application);初始化上下文对象，否则会出现不可估计的错误
    }

    /**
     * 返回主进程名，用于解决多线程多次初始化问题
     * @return
     */
    override fun appMainProcessName(): String {
        return AppConfig.getPackageName()
    }

    private fun initCrash() {
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
            .enabled(true) //是否启动全局异常捕获
            .showErrorDetails(true) //是否显示错误详细信息
            .showRestartButton(true) //是否显示重启按钮
            .trackActivities(true) //是否跟踪Activity
            .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
            .errorDrawable(R.mipmap.ic_launcher) //错误图标
            .restartActivity(LoginActivity::class.java) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
            .apply()
    }
}