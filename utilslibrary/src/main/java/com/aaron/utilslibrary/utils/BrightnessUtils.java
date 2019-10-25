package com.aaron.utilslibrary.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * 亮度调节工具
 */
public class BrightnessUtils {
    /**
     * 设置亮度
     *
     * @param bh
     */
    private static void setBrightness(Activity activity, float bh) {
        if (isAutoBrightness(activity.getContentResolver())) {
            stopAutoBrightness(activity);
        }
        //设置当前activity的屏幕亮度
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        //0到1,调整亮度暗到全亮
        lp.screenBrightness = Float.valueOf(bh / 255f);
        activity.getWindow().setAttributes(lp);

//        //保存为系统亮度方法1
//        android.provider.Settings.System.putInt(getContentResolver(),
//                android.provider.Settings.System.SCREEN_BRIGHTNESS,
//                (int)bh);

        //保存为系统亮度方法2
        Uri uri = Settings.System.getUriFor("screen_brightness");
        Settings.System.putInt(activity.getContentResolver(), "screen_brightness", (int) bh);
        activity.getContentResolver().notifyChange(uri, null);
    }

    /**
     * 检查是否自动调节亮度
     */
    public static boolean isAutoBrightness(ContentResolver aContentResolver) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(aContentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    /**
     * 获取当前屏幕亮度
     */
    public static int getScreenBrightness(Context context) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = context.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

//    /**
//     * 保存到系统亮度
//     **/
//    private void setBrightness(Context context) {
//        try {
//            IPowerManager power = IPowerManager.Stub.asInterface(
//                    ServiceManager.getService("power"));
//            if (power != null) {
//                power.setBacklightBrightness(changeLight);
//            }
//        } catch (RemoteException doe) {
//
//
//        }
//    }

    /**
     * 设置停止自动亮度
     */
    public static void stopAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 开启自动亮度
     */
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }
}
