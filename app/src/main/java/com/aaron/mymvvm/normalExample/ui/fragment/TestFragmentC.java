package com.aaron.mymvvm.normalExample.ui.fragment;

import android.widget.Button;

import com.aaron.baselibrary.BaseLazyFragment;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.mvvmExample.LoginActivity;
import com.aaron.mymvvm.mvvmExample.Main2Activity;
import com.aaron.mymvvm.normalExample.ui.activity.HomeActivity;


public final class TestFragmentC extends BaseLazyFragment<HomeActivity> {

    public static TestFragmentC newInstance() {
        return new TestFragmentC();
    }

    protected int getLayoutId() {
        return R.layout.fragment_test_c;
    }

    protected void initView() {
        Button btn = findActivityViewById(R.id.btn_test_state_black);
        btn.setOnClickListener(view -> startActivity(LoginActivity.class));
        Button btn2 = findActivityViewById(R.id.btn_test_state_white);
        btn2.setOnClickListener(view -> startActivity(Main2Activity.class));
    }

    protected void initData() {
    }
}