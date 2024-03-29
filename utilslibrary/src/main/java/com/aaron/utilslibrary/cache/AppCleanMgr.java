package com.aaron.utilslibrary.cache;

import android.content.Context;
import android.os.Environment;

import com.aaron.utilslibrary.utils.FileUtils;
import com.aaron.utilslibrary.utils.KLog;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 主要功能： 提供App数据清理工作的类
 */
public class AppCleanMgr {

    /**
     * 清除本应用内部缓存数据(/data/data/com.xxx.xxx/cache)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanInternalCache(Context context) {
        FileUtils.deleteFilesByDirectory(context.getCacheDir());
        KLog.i("AppCleanMgr->>cleanInternalCache", "清除本应用内部缓存(/data/data/" + context.getPackageName() + "/cache)");
    }


    /**
     * 清除本应用外部缓存数据(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileUtils.deleteFilesByDirectory(context.getExternalCacheDir());
            KLog.i("AppCleanMgr->>cleanExternalCache", "清除本应用外部缓存数据(/mnt/sdcard/android/data/" + context.getPackageName() + "/cache)");
        }
    }


    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanDatabases(Context context) {
        FileUtils.deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
        KLog.i("AppCleanMgr->>cleanDatabases", "清除本应用所有数据库");
    }


    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanSharedPreference(Context context) {
        FileUtils.deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
        KLog.i("AppCleanMgr->>cleanSharedPreference", "清除本应用cleanSharedPreference数据信息");
    }


    /**
     * 根据名字清除本应用数据库
     *
     * @param context 上下文
     * @param dbName
     * @return void
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
        KLog.i("AppCleanMgr->>cleanDatabaseByName", "根据名字清除本应用数据库");
    }


    /**
     * 清除本应用files文件(/data/data/com.xxx.xxx/files)
     *
     * @param context 上下文
     * @return void
     */
    public static void cleanFiles(Context context) {
        FileUtils.deleteFilesByDirectory(context.getFilesDir());
        KLog.i("AppCleanMgr->>cleanFiles", "清除data/data/" + context.getPackageName() + "/files下的内容信息");
    }


    /**
     * 清除本应用所有的数据
     *
     * @param context 上下文
     * @return int
     */
    public static int cleanApplicationData(Context context) {
        //清除本应用内部缓存数据
        cleanInternalCache(context);
        //清除本应用外部缓存数据
        cleanExternalCache(context);
        //清除本应用SharedPreference
        cleanSharedPreference(context);
        //清除本应用files文件
        cleanFiles(context);
        KLog.i("AppCleanMgr->>cleanApplicationData", "清除本应用所有的数据");
        return 1;
    }


    /**
     * 获取App应用缓存的大小
     *
     * @param context 上下文
     * @return String
     */
    public static String getAppClearSize(Context context) {
        long clearSize = 0;
        String fileSizeStr = "";
        DecimalFormat df = new DecimalFormat("0.00");
        //获得应用内部缓存大小
        clearSize = FileUtils.getFileSize(context.getCacheDir());
        //获得应用SharedPreference缓存数据大小
        clearSize += FileUtils.getFileSize(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
        //获得应用data/data/com.xxx.xxx/files下的内容文件大小
        clearSize += FileUtils.getFileSize(context.getFilesDir());
        //获取应用外部缓存大小
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            clearSize += FileUtils.getFileSize(context.getExternalCacheDir());
        }
        if (clearSize > 5000) {
            //转换缓存大小Byte为MB
            fileSizeStr = df.format((double) clearSize / 1048576) + "MB";
        }
        KLog.i("AppCleanMgr->>getAppClearSize", "获取App应用缓存的大小");
        return fileSizeStr;
    }


}
