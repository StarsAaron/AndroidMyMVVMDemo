package com.aaron.mvvmlibrary.bean;

import androidx.lifecycle.MutableLiveData;

import com.aaron.mvvmlibrary.bean.DialogData;

import java.util.Map;

/**
 * 作者：Aaron
 * 时间：2019/9/28:18:22
 * 邮箱：
 * 说明：ViewModel跟View之间界面处理相关的LiveData
 */
public class BaseUILiveDataEvent {
    // 一些界面相关的观察者
    private MutableLiveData<DialogData> showDialogEvent;
    private MutableLiveData<Void> dismissDialogEvent;
    private MutableLiveData<Map<String, Object>> startActivityEvent;
    private MutableLiveData<Map<String, Object>> startContainerActivityEvent;
    private MutableLiveData<Void> finishEvent;
    private MutableLiveData<Void> onBackPressedEvent;
    private MutableLiveData<Boolean> exitAppEvent;

    public MutableLiveData<DialogData> getShowDialogEvent() {
        if (showDialogEvent == null) {
            showDialogEvent = new MutableLiveData();
        }
        return showDialogEvent;
    }

    public MutableLiveData<Void> getDismissDialogEvent() {
        if (dismissDialogEvent == null) {
            dismissDialogEvent = new MutableLiveData();
        }
        return dismissDialogEvent;
    }

    public MutableLiveData<Map<String, Object>> getStartActivityEvent() {
        if (startActivityEvent == null) {
            startActivityEvent = new MutableLiveData();
        }
        return startActivityEvent;
    }

    public MutableLiveData<Map<String, Object>> getStartContainerActivityEvent() {
        if (startContainerActivityEvent == null) {
            startContainerActivityEvent = new MutableLiveData();
        }
        return startContainerActivityEvent;
    }

    public MutableLiveData<Void> getFinishEvent() {
        if (finishEvent == null) {
            finishEvent = new MutableLiveData();
        }
        return finishEvent;
    }

    public MutableLiveData<Void> getOnBackPressedEvent() {
        if (onBackPressedEvent == null) {
            onBackPressedEvent = new MutableLiveData();
        }
        return onBackPressedEvent;
    }

    public MutableLiveData<Boolean> getExitAppEvent() {
        if (exitAppEvent == null) {
            exitAppEvent = new MutableLiveData();
        }
        return exitAppEvent;
    }
}
