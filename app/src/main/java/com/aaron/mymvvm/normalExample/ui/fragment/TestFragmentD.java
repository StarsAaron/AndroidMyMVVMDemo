package com.aaron.mymvvm.normalExample.ui.fragment;

import android.widget.Button;

import com.aaron.baselibrary.BaseLazyFragment;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.ui.activity.DialogActivity;
import com.aaron.mymvvm.normalExample.ui.activity.HomeActivity;
import com.aaron.mymvvm.normalExample.ui.activity.PersonalDataActivity;
import com.aaron.mymvvm.normalExample.ui.activity.SettingActivity;
import com.aaron.mymvvm.normalExample.ui.activity.StatusActivity;
import com.aaron.mymvvm.normalExample.ui.activity.WebActivity;

public final class TestFragmentD extends BaseLazyFragment<HomeActivity> {

    public static TestFragmentD newInstance() {
        return new TestFragmentD();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_d;
    }

    @Override
    protected void initView() {
        Button btn_test_dialog = findActivityViewById(R.id.btn_test_dialog);
        btn_test_dialog.setOnClickListener(v -> startActivity(DialogActivity.class));

        Button btn_test_hint = findActivityViewById(R.id.btn_test_hint);
        btn_test_hint.setOnClickListener(v -> startActivity(StatusActivity.class));

        Button btn_test_personal = findActivityViewById(R.id.btn_test_personal);
        btn_test_personal.setOnClickListener(v -> startActivity(PersonalDataActivity.class));

        Button btn_test_setting = findActivityViewById(R.id.btn_test_setting);
        btn_test_setting.setOnClickListener(v -> startActivity(SettingActivity.class));

        Button btn_test_browser = findActivityViewById(R.id.btn_test_browser);
        btn_test_browser.setOnClickListener(v -> startActivity(WebActivity.class));

//        Button btn_test_crash = findActivityViewById(R.id.btn_test_crash);
//        btn_test_crash.setOnClickListener(v -> startActivity(StatusActivity.class));
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
    }
}