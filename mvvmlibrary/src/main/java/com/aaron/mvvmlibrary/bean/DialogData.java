package com.aaron.mvvmlibrary.bean;

import android.view.View;

/**
 * 作者：Aaron
 * 时间：2019/9/26:22:34
 * 邮箱：
 * 说明：界面事件传递的封装类，用于显示定制的Dialog
 */
public class DialogData {
    private String title;
    private int isProcessDialog;// 0 进度 1 带标题 2 带按钮监听
    private View.OnClickListener okListener;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsProcessDialog() {
        return isProcessDialog;
    }

    public void setIsProcessDialog(int isProcessDialog) {
        this.isProcessDialog = isProcessDialog;
    }

    public View.OnClickListener getOkListener() {
        return okListener;
    }

    public void setOkListener(View.OnClickListener okListener) {
        this.okListener = okListener;
    }
}
