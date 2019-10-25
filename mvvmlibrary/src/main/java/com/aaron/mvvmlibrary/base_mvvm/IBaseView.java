package com.aaron.mvvmlibrary.base_mvvm;

/**
 * 视图基础逻辑
 */
public interface IBaseView {
    /**
     * 初始化界面传递参数
     */
    void f_initOnCreateFirstParam();
    /**
     * 初始化数据
     */
    void f_initData();

    /**
     * 初始化界面观察者的监听
     */
    void f_initViewObservable();
}
