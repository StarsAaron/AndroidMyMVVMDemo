package com.aaron.utilslibrary.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志文件写入
 */
public class LogToFileUtils {
    private static LogToFileUtils mLog;
    /**
     * 日志保存路径
     */
    public static final String LOG_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.flog/";
    /**
     * 日志开关
     */
    private static final boolean log_switch = true;

    private static final String bm_tag = "bitmapDemo";

    public LogToFileUtils() {
        throw new UnsupportedOperationException("不支持使用new的方式创建该类对象，请使用静态方法instance()获取实例");
    }

    public static boolean getLogSwitch() {
        return log_switch;
    }

    public static LogToFileUtils instance() {
        if (mLog == null) {
            mLog = new LogToFileUtils();
        }
        return mLog;
    }

    public static void d(String tag, String msg) {
        if (log_switch)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (log_switch)
            Log.e(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (log_switch)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (log_switch)
            Log.w(tag, msg);
    }

    public static void bm(String msg) {
        Log.i(bm_tag, msg);
    }

    public static void huajiang(String msg) {
        if (log_switch)
            Log.d("huajiang", msg);
    }

    public static void songDownload(String msg) {
        if (log_switch) {
            Log.d("songDownload", msg);
        }
    }

    public static void mvLog(String msg) {
        if (log_switch) {
            Log.d("makeMv", msg);
        }
    }

    public static void thduan(String msg) {
        if (log_switch) {
            Log.d("thduan", msg);
        }
    }

    public static void httpReqLog(String msg) {
        if (log_switch) {
            Log.d("httpReqLog", msg);
        }
    }

    public void addLog(Throwable e) {
        addLog(getExceptionStackTrace(e));
    }

    /**
     * 插入日志
     */
    public void addLog(String logStr) {
        boolean lonTag = false;
        if (new File(LOG_SAVE_PATH).exists())
            lonTag = true;
        else
            lonTag = false;
        if (lonTag) {
            File file = checkLogFileIsExist();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, true);
                fos.write((new Date().toLocaleString() + "	" + logStr)
                        .getBytes("gbk"));
                fos.write("\r\n".getBytes("gbk"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
                file = null;
            }
        }
    }

    /**
     * 检查日志文件是否存在
     */
    private File checkLogFileIsExist() {
//		if (!Utils.checkSDCardStatus()) {
//			return null;
//		}
        File file = new File(LOG_SAVE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        file = new File(LOG_SAVE_PATH + dateStr + ".txt");
        if (!isLogExist(file)) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        sdf = null;
        return file;
    }

    /**
     * 检查当天日志文件是否存在
     *
     * @param file
     * @return
     */
    private boolean isLogExist(File file) {
        File tempFile = new File(LOG_SAVE_PATH);
        File[] files = tempFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[0].getName().trim().equalsIgnoreCase(file.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印异常堆栈信息
     *
     * @param e
     * @return
     */
    public static String getExceptionStackTrace(Throwable e) {
        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return "";
    }

    /**
     * 打印当前程序占用的内存
     */
    public static void printMem(String when) {
        //程序可用的最大内存
        float maxMem = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        //程序当前占用的内存
        float totalMem = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        //freeMem != maxMem - totalMem
        //我理解 freeMem应该是 当前分配给该程序的内存 - totalMem， 当前分配给程序的内存时动态的(在小于maxMem范围内)
        //同virtualbox安装的ubuntu虚拟机占用内存类似，设置个最大内存，但实际占用内存时动态分配的
        float freeMem = Runtime.getRuntime().freeMemory() / 1024 / 1024;

        bm(when + ": maxMem | totalMem | freeMem : " +
                maxMem + "M|" + totalMem + "M|" + freeMem + "M");
    }
}
