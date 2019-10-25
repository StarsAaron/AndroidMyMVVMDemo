package com.aaron.mymvvm.normalExample.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.aaron.baselibrary.BaseActivity;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.helper.InputTextHelper;
import com.aaron.utilslibrary.utils.ToastUtils;
import com.aaron.widgetlibrary.view.CountdownView;
import com.aaron.widgetlibrary.view.RegexEditText;

/**
 * desc   : 更换手机号
 */
public final class PhoneResetActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 输入手机号码
     */
    private RegexEditText mPhoneView;
    /**
     * 输入验证码
     */
    private AppCompatEditText mCodeView;
    private CountdownView mCountdownView;
    /**
     * 立即绑定
     */
    private AppCompatButton mCommitView;

    @Override
    protected int f_getLayoutId() {
        return R.layout.activity_phone_reset;
    }

    @Override
    protected void f_initView(Bundle savedInstanceState) {
        InputTextHelper.with(this)
                .addView(mPhoneView)
                .addView(mCodeView)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mPhoneView.getText().toString().length() == 11 && mCodeView.getText().toString().length() == 4;
                    }
                })
                .build();
        mPhoneView = (RegexEditText) findViewById(R.id.et_phone_reset_phone);
        mPhoneView.setOnClickListener(this);
        mCodeView = (AppCompatEditText) findViewById(R.id.et_phone_reset_code);
        mCodeView.setOnClickListener(this);
        mCountdownView = (CountdownView) findViewById(R.id.cv_phone_reset_countdown);
        mCountdownView.setOnClickListener(this);
        mCommitView = (AppCompatButton) findViewById(R.id.btn_phone_reset_commit);
        mCommitView.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_phone_reset_countdown:
                // 获取验证码
                if (mPhoneView.getText().toString().length() != 11) {
                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                    ToastUtils.showShort(R.string.common_phone_input_error);
                } else {
                    ToastUtils.showShort(R.string.common_code_send_hint);
                }
                break;
            case R.id.btn_phone_reset_commit:
                // 更换手机号
                ToastUtils.showShort(R.string.phone_reset_commit_succeed);
                finish();
                break;
            default:
                break;
            case R.id.et_phone_reset_phone:
                break;
            case R.id.et_phone_reset_code:
                break;
        }
    }
}