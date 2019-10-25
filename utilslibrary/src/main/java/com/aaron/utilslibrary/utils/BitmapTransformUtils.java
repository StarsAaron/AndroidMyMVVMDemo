package com.aaron.utilslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Bitmap 获取转换相关的工具类
 */
public class BitmapTransformUtils {
    private BitmapTransformUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * Bitmap 转成 Bytes
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2ByteArray(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap 转成 输入流
     *
     * @param bm
     * @return
     */
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Drawable 转 Bytes
     *
     * @param drawable
     * @return
     */
    public static byte[] Drawable2ByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        return stream.toByteArray();
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap Drawable2Bitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * Bitmap 转 Drawable
     *
     * @param c
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drawable(Context c, final Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(c.getResources(), bitmap);
        return bd;
    }

    /**
     * 输入流转 Byte 数据
     *
     * @param inputStream
     * @return
     */
    public static byte[] InputStream2ByteArray(InputStream inputStream) {
        byte[] data = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            byte[] result = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(result)) != -1) {
                outputStream.write(result, 0, len);
            }
            data = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File saveFileFromByteArray(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 资源名称获取资源的ID
     *
     * @param activity
     * @param iconName
     * @return
     */
    public static int getResIDByResName(Activity activity, String iconName) {
        Resources resources = activity.getResources();
        int indentify = resources.getIdentifier(activity.getPackageName()
                + ":drawable/" + iconName, null, null);
        return indentify;
    }

    /**
     * Byte 转 Bitmap
     *
     * @param data
     * @return
     */
    public static Bitmap ByteArray2Bitmap(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    /**
     * Byte数组 转 Bitmap，带期望宽高参数
     *
     * @param data
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap ByteArray2Bitmap(byte[] data, int reqWidth, int reqHeight) {
        // 声明解码的参数设置First decode with inJustDecodeBounds=true to check dimensions
        final Options options = new Options();
        //为了在解码的过程中避免申请内存空间，但是可以获得原始图片的宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
//	    BitmapFactory.decodeResource(data, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * InputStream转Bitmap，带期望宽高参数
     *
     * @param inputStream
     * @param reqWidth    期望宽
     * @param reqHeight   期望高
     * @return
     */
    public static Bitmap inputStream2Bitmap(InputStream inputStream, int reqWidth, int reqHeight) {
        byte[] data = InputStream2ByteArray(inputStream);//可不能直接放入BitmapFactory.decodeByteArray中
        final Options options = new Options();
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);//计算压缩比
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 计算压缩比
     *
     * @param options
     * @param reqWidth  期望宽
     * @param reqHeight 期望高
     * @return
     */
    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;// 图片实际的高
        final int width = options.outWidth;// 图片实际的宽
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 伸缩Bitmap
     *
     * @param filePath
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleBitmap(String filePath, int w, int h) {
        Options o = new Options();
        o.inJustDecodeBounds = true;
        Bitmap tmp = BitmapFactory.decodeFile(filePath, o);
        o.inJustDecodeBounds = false;
        int width = (int) Math.ceil(o.outWidth / (float) w);
        int height = (int) Math.ceil(o.outHeight / (float) h);
        if (width > 1 && height > 1) {
            if (height > width) {
                o.inSampleSize = height;
            } else {
                o.inSampleSize = width;
            }
        }
        tmp = BitmapFactory.decodeFile(filePath, o);
        return tmp;
    }

    /**
     * 伸缩Bitmap
     *
     * @param context
     * @param imageResId 图片资源ID
     * @param w
     * @param h
     * @return
     */
    public static Bitmap getScaledBitmap(Context context, int imageResId, int w, int h) {
        Options decodeOptions = new Options();
        decodeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageResId, decodeOptions);
        decodeOptions.inJustDecodeBounds = false;
        int width = (int) Math.ceil(decodeOptions.outWidth / (float) w);
        int height = (int) Math.ceil(decodeOptions.outHeight / (float) h);
        if (width > 1 && height > 1) {
            if (height > width) {
                decodeOptions.inSampleSize = height;
            } else {
                decodeOptions.inSampleSize = width;
            }
        }
        return BitmapFactory.decodeResource(context.getResources(), imageResId, decodeOptions);
    }

    /**
     * 保存Bitmap到文件
     *
     * @param bitmap
     * @param file
     * @return
     */
    public static boolean saveBitmap2File(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
                out.flush();
                out.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从文件获取Bitmap
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getBitmapFromFile(File file) throws FileNotFoundException {
        if (file == null)
            return null;
        Options o = new Options();
        o.inJustDecodeBounds = true;
        Bitmap tmp = BitmapFactory.decodeFile(file.getAbsolutePath(), o);
        o.inJustDecodeBounds = false;

        int rate = (int) (o.outHeight / (float) o.outWidth);
        if (rate <= 0) {
            rate = 1;
        }
        o.inSampleSize = rate;
        o.inPurgeable = true;
        o.inInputShareable = true;

        tmp = BitmapFactory.decodeFile(file.getAbsolutePath(), o);

        return tmp;
    }

    /**
     * 从Assets 文件夹获取Bitmap
     *
     * @param context
     * @param name
     * @return
     */
    public static Bitmap getBitmapFromAssets(Context context, String name) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(name);
            if (inputStream == null) {
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取圆角Bitmap
     *
     * @param bitmap
     * @param pixels 圆角半径
     * @return
     */
    public static Bitmap createRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            return bitmap;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 生成圆形带边框的Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap createRoundBitmap(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            left = 0;
            bottom = width;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        //画白色圆圈
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2 - 4 / 2, paint);
        return output;
    }

    /**
     * 旋转 Bitmap
     *
     * @param bitmap
     * @param degree 旋转角度
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree); /* 翻转90度 */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 加边框
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap) {
        if (null == srcBitmap) {
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 4;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int reflectionWidth = srcBitmap.getWidth();
        int reflectionHeight = srcBitmap.getHeight() / 2;

        if (0 == srcWidth || srcHeight == 0) {
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {
            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                    srcHeight / 2, srcWidth, srcHeight / 2, matrix, false);

            if (null == reflectionBitmap) {
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(reflectionWidth,
                    srcHeight + reflectionHeight + REFLECTION_GAP,
                    Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP,
                    null);

            // srcBitmap.recycle();
            reflectionBitmap.recycle();

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    Mode.DST_IN));

            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
