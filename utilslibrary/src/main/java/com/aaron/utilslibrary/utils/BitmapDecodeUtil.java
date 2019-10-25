package com.aaron.utilslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.TypedValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Bitmap解码转换工具、
 * <p>
 * 一、decodeBitmap:对Bitmap不压缩，但是会根据屏幕的密度合适的进行缩放压缩
 * 二、compressBimtap:对Bitmap进行超过最大宽高的压缩，同时也会根据屏幕的密度合适的进行缩放压缩。
 */
public class BitmapDecodeUtil {
    private static final int DEFAULT_DENSITY = 240;
    private static final float SCALE_FACTOR = 0.75f;
    private static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.RGB_565;

    /**
     * 获取优化的BitmapFactory.Options
     * 根据屏幕密度
     *
     * @param context
     * @return
     */
    private static BitmapFactory.Options getBitmapOptions(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inPreferredConfig = DEFAULT_BITMAP_CONFIG;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            Field field = null;
            try {
                field = BitmapFactory.Options.class.getDeclaredField("inNativeAlloc");
                field.setAccessible(true);
                field.setBoolean(options, true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int displayDensityDpi = context.getResources().getDisplayMetrics().densityDpi;
        float displayDensity = context.getResources().getDisplayMetrics().density;
        if (displayDensityDpi > DEFAULT_DENSITY && displayDensity > 1.5f) {
            int density = (int) (displayDensityDpi * SCALE_FACTOR);
            options.inDensity = density;
            options.inTargetDensity = density;
        }
        return options;
    }

    /**
     * 通过资源ID解码Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap decodeBitmap(Context context, int resId) {
        checkParam(context);
        return BitmapFactory.decodeResource(context.getResources(), resId, getBitmapOptions(context));
    }

    /**
     * 通过资源路径解码Bitmap
     *
     * @param context
     * @param pathName
     * @return
     */
    public static Bitmap decodeBitmap(Context context, String pathName) {
        checkParam(context);
        return BitmapFactory.decodeFile(pathName, getBitmapOptions(context));
    }

    /**
     * 通过资源输入流解码Bitmap
     *
     * @param context
     * @param is
     * @return
     */
    public static Bitmap decodeBitmap(Context context, InputStream is) {
        checkParam(context);
        checkParam(is);
        return BitmapFactory.decodeStream(is, null, getBitmapOptions(context));
    }

    /**
     * 通过资源ID获取压缩Bitmap
     *
     * @param context
     * @param resId
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap compressBitmap(Context context, int resId, int maxWidth, int maxHeight) {
        checkParam(context);
        final TypedValue value = new TypedValue();
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(resId, value);
            return compressBitmap(context, is, maxWidth, maxHeight);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 通过资源路径获取压缩Bitmap
     *
     * @param context
     * @param pathName
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap compressBitmap(Context context, String pathName, int maxWidth, int maxHeight) {
        checkParam(context);
        InputStream is = null;
        try {
            is = new FileInputStream(pathName);
            return compressBitmap(context, is, maxWidth, maxHeight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 通过资源流获取压缩Bitmap
     *
     * @param context
     * @param is
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap compressBitmap(Context context, InputStream is, int maxWidth, int maxHeight) {
        checkParam(context);
        checkParam(is);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opt);
        int height = opt.outHeight;
        int width = opt.outWidth;
        int sampleSize = computeSampleSize(width, height, maxWidth, maxHeight);
        BitmapFactory.Options options = getBitmapOptions(context);
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 计算SampleSize
     *
     * @param width
     * @param height
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    private static int computeSampleSize(int width, int height, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        if (height > maxHeight || width > maxWidth) {
            final int heightRate = Math.round((float) height / (float) maxHeight);
            final int widthRate = Math.round((float) width / (float) maxWidth);
            inSampleSize = heightRate < widthRate ? heightRate : widthRate;
        }
        if (inSampleSize % 2 != 0) {
            inSampleSize -= 1;
        }
        return inSampleSize <= 1 ? 1 : inSampleSize;
    }

    /**
     * 检查对象是否为空
     *
     * @param param
     */
    private static void checkParam(Object param) {
        if (param == null)
            throw new NullPointerException();
    }
}
