package com.aaron.utilslibrary.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * 系统信息的工具类
 */
public class RamInfoUtils {

    /**
     * 获取正在运行的进程的个数
     *
     * @return 进程数量
     */
    public static int getRunningProcessCount(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses().size();
    }

    /**
     * 获取手机剩余内存 ram
     *
     * @param context
     * @return 单位是byte
     */
    public static long getAvailRam(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        return outInfo.availMem; //byte为单位的long类型的可用内存大小
    }

    /**
     * 获取手机总内存 ram
     *
     * @param context
     * @return 单位是byte
     */
    public static long getTotalRam(Context context) {
//      下面的api  totalmem只能在16以上版本下使用。
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
        try {
            File file = new File("/proc/meminfo");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            //MemTotal:         516452 kB
            String line = br.readLine();
            //字符串  一组字符--串
            StringBuffer sb = new StringBuffer();
            for (char c : line.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            return Integer.parseInt(sb.toString()) * 1024l;  //byte
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 显示存储的剩余空间
     */
    public long getAvailableSize(Context context) {
        long romSize = getAvailSpace(Environment.getDataDirectory().getAbsolutePath());//手机内部存储大小
        long sdSize = getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());//外部存储大小
        Log.i("内存可用空间: ", Formatter.formatFileSize(context, romSize));
        Log.i("SD卡可用空间:", Formatter.formatFileSize(context, sdSize));
        return romSize;
    }

    /**
     * 获取某个目录的可用空间
     */
    public long getAvailSpace(String path) {
        StatFs statfs = new StatFs(path);
        long size = statfs.getBlockSize();//获取分区的大小
        long count = statfs.getAvailableBlocks();//获取可用分区块的个数
        return size * count;
    }

    /**
     * 获取系统进程信息
     *
     * @param context
     * @return
     */
    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcessInfo(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getRunningAppProcesses();
    }

    /**
     * 获取手机IP
     *
     * @return
     */
    public static String getIP() throws SocketException {
        String ip = "";
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> ips = ni.getInetAddresses();
            while (ips.hasMoreElements()) {
                ip = ips.nextElement().getHostAddress();
            }
        }
        return ip;
    }

    /**
     * 获取手机Mac地址
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        /*
         需要权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
         */
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取机器ID
     *
     * @param context
     * @return
     */
    public static String getDevId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String devid = telephonyManager.getDeviceId();
        if (!TextUtils.isEmpty(devid))
            return devid;

        devid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(devid))
            return devid;
        return null;
    }

    /**
     * 获取手机可用的cpu数
     *
     * @return
     */
    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        try {
            if (context == null) {
                return "";
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getSubscriberId();
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }
        return "";
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            if (context == null) {
                return "";
            }
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            if (imei != null && !imei.equals("")) {
                return imei;
            }
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }

        return "";
    }

    /**
     * 获取手机号码
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getLine1Number() == null || telephonyManager.getLine1Number().length() < 11) {
            return null;
        } else {
            return telephonyManager.getLine1Number();
        }
    }
}
