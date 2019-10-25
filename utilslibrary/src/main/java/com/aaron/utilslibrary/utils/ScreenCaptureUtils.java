package com.aaron.utilslibrary.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 作者：Aaron
 * 时间：2018/11/26:15:18
 * 邮箱：
 * 说明：5.0 截图
 * <p>
 * 需要先申请权限
 * startActivityForResult(mMpMngr.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION)
 * <p>
 * 在onActivityResult中获取返回的Intent
 * override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
 * super.onActivityResult(requestCode, resultCode, data)
 * <p>
 * if (requestCode == REQUEST_MEDIA_PROJECTION) {
 * if (resultCode == Activity.RESULT_OK) {
 * (application as MultiThreadApplication).resultCode = resultCode
 * (application as MultiThreadApplication).resultIntent = data
 * (application as MultiThreadApplication).mpmngr = mMpMngr
 * }
 * }
 * }
 * <p>
 * 因为要保存截图，在6.0需要动态权限申请android.Manifest.permission.WRITE_EXTERNAL_STORAGE
 * <p>
 * 获取单例
 * ScreenCaptureUtils.getInstance(Context context, int reqCode, Intent data)
 * <p>
 * 截图
 * shotter.startScreenShot()
 * 注意：在不用的时候记得destory
 */
public class ScreenCaptureUtils {
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private int statusBarHeight1 = -1;
    private String mLocalUrl = "";
    private Rect mRect; //rect是我在项目里面需要截图的区域，一个图标。

    private ScreenCaptureUtils() {
    }

    public static ScreenCaptureUtils getInstance(Context context, int reqCode, Intent data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            throw new RuntimeException("不支持低版本截图");
        }
        Builder.init(context, reqCode, data);
        return Builder.instance;
    }

    private static class Builder {
        static ScreenCaptureUtils instance = new ScreenCaptureUtils();

        static void init(Context context, int reqCode, Intent data) {
            instance.initMediaProjectionManager(context, reqCode, data);
        }
    }

    private void initMediaProjectionManager(Context context, int reqCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjection = getMediaProjectionManager(context).getMediaProjection(reqCode, data);
            mImageReader = ImageReader.newInstance(
                    getScreenWidth(),
                    getScreenHeight(),
                    PixelFormat.RGBA_8888,//此处必须和下面 buffer处理一致的格式 ，RGB_565在一些机器上出现兼容问题。
                    1);

            mLocalUrl = context.getExternalFilesDir("screenshot").getAbsoluteFile()
                    + "/"
                    + SystemClock.currentThreadTimeMillis() + ".png";
            getStatusBatHeight(context);
        }
    }

    public void setFilePath(String path) {
        this.mLocalUrl = path;
    }

    public void setRect(Rect mRect) {
        this.mRect = mRect;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private MediaProjectionManager getMediaProjectionManager(Context context) {
        return (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                getScreenWidth(),
                getScreenHeight(),
                getScreenDpi(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

//    private void screenshot() { // 获取屏幕另外一种方式
//        View dView = getWindow().getDecorView();
//        dView.setDrawingCacheEnabled(true);
//        dView.buildDrawingCache();
//        Bitmap bmp = dView.getDrawingCache();
//        if (bmp != null) {
//            try { // 获取内置SD卡路径
//                String sdCardPath = Environment.getExternalStorageDirectory().getPath(); // 图片文件路径
//                String filePath = sdCardPath + File.separator + "screenshot.png";
//                File file = new File(filePath);
//                FileOutputStream os = new FileOutputStream(file);
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
//                os.flush();
//                os.close();
//            } catch (Exception e) {
//            }
//        }
//    }

    /**
     * 截图
     *
     * @return 是否成功
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean startScreenShot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i("4444", "startScreenShot");
            virtualDisplay();
            Image image = mImageReader.acquireLatestImage();
            int count = 0;
            while (null == image && count < 5) {
                count++;
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                image = mImageReader.acquireLatestImage();
            }
            if (count >= 5) {
                return false;
            }
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            if (null != mRect)
                bitmap = Bitmap.createBitmap(bitmap, mRect.left, mRect.top - statusBarHeight1, mRect.width(), mRect.height());
            else
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();

            File fileImage = null;
            if (bitmap != null) {
                try {
                    fileImage = new File(mLocalUrl);
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (mVirtualDisplay != null) {
                mVirtualDisplay.release();
            }
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void destory() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
        mImageReader.close();
        if (null != mMediaProjection) {
            mMediaProjection.stop();
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private int getScreenDpi() {
        return Resources.getSystem().getDisplayMetrics().densityDpi;
    }

    private void getStatusBatHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) { //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
            float scale = context.getResources().getDisplayMetrics().density;
            statusBarHeight1 = (int) (statusBarHeight1 * scale + 0.5f);
        }
    }
}