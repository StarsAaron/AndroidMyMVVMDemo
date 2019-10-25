package com.aaron.mymvvm.normalExample.ui.fragment;

import android.view.View;
import androidx.lifecycle.Observer;
import com.aaron.mvvmlibrary.base_mvvm.view.MultiBaseMVVMFragment;
import com.aaron.mvvmlibrary.bean.ViewModelData;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.databinding.FragmentTestABinding;
import com.aaron.utilslibrary.utils.KLog;
import java.util.ArrayList;

public final class TestFragmentA extends MultiBaseMVVMFragment<FragmentTestABinding> {

    public static TestFragmentA newInstance() {
        return new TestFragmentA();
    }

    @Override
    public void f_initData() {
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.textView.setText(binding.editText.getText());
            }
        });
    }

    @Override
    public void f_initViewObservable() {
        TestAViewModel testAViewModel = f_getViewModel(TestAViewModel.class);
        testAViewModel.editTextMsg.observe(this,new Observer<String>(){
            @Override
            public void onChanged(String s) {
                KLog.i("f_initViewObservable >>>editTextMsg: "+testAViewModel.editTextMsg.getValue());
                testAViewModel.name.setValue(s);
            }
        });
        testAViewModel.name.observe(this,new Observer<String>(){
            @Override
            public void onChanged(String s) {
                KLog.i("f_initViewObservable >>>name: "+testAViewModel.name.getValue());
            }
        });
    }

    /**
     * 绑定ViewModel
     *
     * @param viewModelBindinglist 设置绑定映射，key为xml文件中的变量名，value为ViewModel
     *                             传入格式为 <BR.variableName,vm extends BaseViewModel>
     */
    @Override
    public void f_initViewModelList(ArrayList<ViewModelData> viewModelBindinglist) {
        ViewModelData viewModelData = new ViewModelData(com.aaron.mymvvm.BR.testviewmodel, TestAViewModel.class);
        viewModelBindinglist.add(viewModelData);
    }

    /**
     * 初始化根布局
     * @return 布局layout的id
     */
    @Override
    public int f_getLayoutId() {
        return R.layout.fragment_test_a;
    }

}