package com.aaron.mymvvm.normalExample.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.aaron.baselibrary.BaseActivity;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.helper.InputTextHelper;
import com.aaron.utilslibrary.utils.ToastUtils;
import com.aaron.widgetlibrary.view.CountdownView;


/**
 * desc   : 校验手机号
 */
public final class PhoneVerifyActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 当前手机号：%s
     */
    private AppCompatTextView mPhoneView;
    /**
     * 输入验证码
     */
    private AppCompatEditText mCodeView;
    private CountdownView mCountdownView;
    /**
     * 下一步
     */
    private AppCompatButton mCommitView;

    @Override
    protected int f_getLayoutId() {
        return R.layout.activity_phone_verify;
    }

    @Override
    protected void f_initView(Bundle savedInstanceState) {
        mPhoneView = (AppCompatTextView) findViewById(R.id.tv_phone_verify_phone);
        mPhoneView.setOnClickListener(this);
        mCodeView = (AppCompatEditText) findViewById(R.id.et_phone_verify_code);
        mCodeView.setOnClickListener(this);
        mCountdownView = (CountdownView) findViewById(R.id.cv_phone_verify_countdown);
        mCountdownView.setOnClickListener(this);
        mCommitView = (AppCompatButton) findViewById(R.id.btn_phone_verify_commit);
        mCommitView.setOnClickListener(this);

        InputTextHelper.with(this)
                .addView(mCodeView)
                .setMain(mCommitView)
                .setListener(new InputTextHelper.OnInputTextListener() {

                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return mCodeView.getText().toString().length() == 4;
                    }
                })
                .build();
        mPhoneView.setText(String.format(getString(R.string.phone_verify_current_phone), "18888888888"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_phone_verify_countdown:
                // 获取验证码
                if (mPhoneView.getText().toString().length() != 11) {
                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                    ToastUtils.showShort(R.string.common_phone_input_error);
                } else {
                    ToastUtils.showShort(R.string.common_code_send_hint);
                }
                break;
            case R.id.btn_phone_verify_commit:
                // 修改手机号
                f_startActivityFinish(PhoneResetActivity.class);
                break;
            default:
                break;
            case R.id.tv_phone_verify_phone:
                break;
            case R.id.et_phone_verify_code:
                break;
        }
    }
}