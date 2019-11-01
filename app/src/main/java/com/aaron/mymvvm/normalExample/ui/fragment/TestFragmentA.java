package com.aaron.mymvvm.normalExample.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import com.aaron.mvvmlibrary.base_mvvm.view.MultiBaseMVVMFragment;
import com.aaron.mvvmlibrary.bean.ViewModelData;
import com.aaron.mymvvm.BR;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.databinding.FragmentTestABinding;
import com.aaron.utilslibrary.utils.KLog;
import java.util.ArrayList;

public final class TestFragmentA extends MultiBaseMVVMFragment<FragmentTestABinding> {

    public static TestFragmentA newInstance() {
        return new TestFragmentA();
    }

    @Override
    public int f_getLayoutId() {
        return R.layout.fragment_test_a;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        f_registorUIBinding(com.aaron.mymvvm.BR.testviewmodel,f_getViewModel(TestAViewModel.class));

        f_initViewObservable();

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.textView.setText(binding.editText.getText());
            }
        });
    }

    private void f_initViewObservable() {
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
}