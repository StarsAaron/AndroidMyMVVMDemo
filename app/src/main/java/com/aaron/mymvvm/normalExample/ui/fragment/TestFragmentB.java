package com.aaron.mymvvm.normalExample.ui.fragment;

import com.aaron.baselibrary.BaseLazyFragment;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.ui.activity.HomeActivity;
import com.aaron.utilslibrary.utils.ToastUtils;
import com.aaron.widgetlibrary.view.CountdownView;
import com.aaron.widgetlibrary.view.SwitchButton;


/**
 *    desc   : 项目自定义控件展示
 */
public final class TestFragmentB extends BaseLazyFragment<HomeActivity>
        implements SwitchButton.OnCheckedChangeListener {
    SwitchButton mSwitchButton;
    CountdownView mCountdownView;

    public static TestFragmentB newInstance() {
        return new TestFragmentB();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_b;
    }

    @Override
    protected void initView() {
        mSwitchButton = findActivityViewById(R.id.sb_test_switch);
        mSwitchButton.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
    }

    /**
     * {@link SwitchButton.OnCheckedChangeListener}
     */
    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        ToastUtils.showShort(String.valueOf(isChecked));
    }
}