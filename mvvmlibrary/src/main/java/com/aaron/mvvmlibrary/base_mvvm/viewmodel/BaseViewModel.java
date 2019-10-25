package com.aaron.mvvmlibrary.base_mvvm.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.aaron.mvvmlibrary.base_mvvm.IBaseViewModel;
import com.aaron.mvvmlibrary.bean.BaseUILiveDataEvent;
import com.aaron.mvvmlibrary.bean.DialogData;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 基础的ViewModel，继承AndroidViewModel
 * 实现的功能：
 * （1）提供显示对话框，取消对话框，Activity跳转，返回按钮事件，结束页面
 * 事件的LiveData，调用相关方法就可以
 * （2）在所有订阅中调用Observable的doOnSubscribe(CurrentViewModel.this)目的用于
 * 获取Disposable对象，统一管理所有订阅
 * <p>
 * 实现Consumer<Disposable>接口，调用Observable的doOnSubscribe(CurrentViewModel.this)目的用
 * 于获取Disposable对象，统一管理所有订阅，在ViewModel销毁调用onCleared()的时候清除所有订阅。
 * model.loadMore()
 * .doOnSubscribe(CurrentViewModel.this) //请求与ViewModel周期同步
 * .subscribe()
 * <p>
 * （3）ViewModel 实现IBaseViewModel 接口监听生命周期，在Activity或Fragment中getLifecycle().addObserver(viewModel);注册
 */
public class BaseViewModel extends AndroidViewModel implements IBaseViewModel, Consumer<Disposable> {
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable mCompositeDisposable;
    private BaseUILiveDataEvent baseUILiveDataEvent;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * 获取界面事件相关处理类
     *
     * @return
     */
    public BaseUILiveDataEvent getBaseUILiveDataEvent() {
        if (baseUILiveDataEvent == null) {
            baseUILiveDataEvent = new BaseUILiveDataEvent();
        }
        return baseUILiveDataEvent;
    }

    /**
     * 添加Disposable对象到CompositeDisposable，统一管理
     *
     * @param disposable
     */
    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //ViewModel销毁时会执行，同时取消所有异步任务
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    /**
     * 注册RxBus,订阅事件
     * 在Activity的onCreate方法中调用
     */
    public void registerRxBus() {
        // 订阅事件
    }

    /**
     * 移除RxBus订阅
     */
    public void removeRxBus() {

    }

    /**
     * 展示进度框
     */
    public void showProgressDialog() {
        DialogData dialogData = new DialogData();
        dialogData.setIsProcessDialog(0);
        baseUILiveDataEvent.getShowDialogEvent().postValue(dialogData);
    }

    /**
     * 显示只有标题对话框
     */
    public void showDialogWithTitle(String title) {
        DialogData dialogData = new DialogData();
        dialogData.setTitle(title);
        dialogData.setIsProcessDialog(1);
        baseUILiveDataEvent.getShowDialogEvent().postValue(dialogData);
    }

    /**
     * 显示带按钮的对话框
     *
     * @param title
     * @param okListener
     */
    public void showDialogWithAction(String title, View.OnClickListener okListener) {
        DialogData dialogData = new DialogData();
        dialogData.setTitle(title);
        dialogData.setIsProcessDialog(2);
        dialogData.setOkListener(okListener);
        baseUILiveDataEvent.getShowDialogEvent().postValue(dialogData);
    }

    @MainThread
    public void dismissDialog() {
        baseUILiveDataEvent.getDismissDialogEvent().postValue(null);
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        baseUILiveDataEvent.getExitAppEvent().postValue(true);
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        baseUILiveDataEvent.getStartActivityEvent().postValue(params);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startContainerActivity(String canonicalName) {
        startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void startContainerActivity(String canonicalName, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CANONICAL_NAME, canonicalName);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        baseUILiveDataEvent.getStartContainerActivityEvent().postValue(params);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        baseUILiveDataEvent.getFinishEvent().postValue(null);
    }

    /**
     * 返回上一层
     */
    public void onBackPressed() {
        baseUILiveDataEvent.getOnBackPressedEvent().postValue(null);
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        addSubscribe(disposable);
    }

    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String CANONICAL_NAME = "CANONICAL_NAME";
        public static String BUNDLE = "BUNDLE";
    }
}
