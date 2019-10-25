package com.aaron.mymvvm.normalExample.ui.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;

/**
 * 作者：Aaron
 * 时间：2019/10/21:17:48
 * 邮箱：
 * 说明：
 */
public class TestAViewModel extends BaseViewModel{
    public MutableLiveData<String> name = new MutableLiveData<String>("");
    public MutableLiveData<String> editTextMsg = new MutableLiveData<String>("");

    public TestAViewModel(@NonNull Application application) {
        super(application);
    }
}
