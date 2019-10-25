package com.aaron.mvvmlibrary.bean;

import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;

/**
 * 作者：Aaron
 * 时间：2019/10/16:11:23
 * 邮箱：
 * 说明：ViewModel绑定数据
 */
public class ViewModelData {
    private int variableId; // 绑定的xml布局文件中的变量名对应的BR值 BR.XX
    private Class viewModelClassName; // ViewModel的类
    private BaseViewModel viewModel; // ViewModel的实例

    public ViewModelData(){}

    public ViewModelData(int variableId,Class viewModelClassName){
        if(BaseViewModel.class.isAssignableFrom(viewModelClassName)){
            this.viewModelClassName = viewModelClassName;
        }else{
            throw new TypeNotPresentException("绑定的ViewModel要求是BaseViewModel的子类",null);
        }
        this.variableId = variableId;
    }

    public int getVariableId() {
        return variableId;
    }

    public Class getViewModelClassName() {
        return viewModelClassName;
    }

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    public void setVariableId(int variableId) {
        this.variableId = variableId;
    }

    public void setViewModelClassName(Class viewModelClassName) {
        this.viewModelClassName = viewModelClassName;
    }

    public void setViewModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
