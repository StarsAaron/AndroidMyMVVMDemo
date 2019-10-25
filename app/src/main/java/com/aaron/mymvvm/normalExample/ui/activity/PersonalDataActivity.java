package com.aaron.mymvvm.normalExample.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;
import com.aaron.utilslibrary.BaseDialog;
import com.aaron.baselibrary.BaseActivity;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.dialog.AddressDialog;
import com.aaron.mymvvm.normalExample.dialog.InputDialog;
import com.aaron.widgetlibrary.layout.SettingBar;

/**
 * desc   : 个人资料
 */
public final class PersonalDataActivity extends BaseActivity implements View.OnClickListener {
    private String mAvatarUrl;
    private AppCompatImageView mIvPersonDataAvatar;
    private FrameLayout mFlPersonDataHead;
    private SettingBar mSbPersonDataId;
    private SettingBar mSbPersonDataName;
    private SettingBar mSbPersonDataAddress;
    private SettingBar mSbPersonDataPhone;

    @Override
    protected int f_getLayoutId() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void f_initView(Bundle savedInstanceState) {
        mIvPersonDataAvatar = (AppCompatImageView) findViewById(R.id.iv_person_data_avatar);
        mIvPersonDataAvatar.setOnClickListener(this);
        mFlPersonDataHead = (FrameLayout) findViewById(R.id.fl_person_data_head);
        mFlPersonDataHead.setOnClickListener(this);
        mSbPersonDataId = (SettingBar) findViewById(R.id.sb_person_data_id);
        mSbPersonDataId.setOnClickListener(this);
        mSbPersonDataName = (SettingBar) findViewById(R.id.sb_person_data_name);
        mSbPersonDataName.setOnClickListener(this);
        mSbPersonDataAddress = (SettingBar) findViewById(R.id.sb_person_data_address);
        mSbPersonDataAddress.setOnClickListener(this);
        mSbPersonDataPhone = (SettingBar) findViewById(R.id.sb_person_data_phone);
        mSbPersonDataPhone.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_person_data_avatar:
                break;
            case R.id.fl_person_data_head:
                break;
            case R.id.sb_person_data_name:
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle(getString(R.string.personal_data_name_hint))
                        .setContent(mSbPersonDataName.getRightText())
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener(new InputDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (!mSbPersonDataName.getRightText().equals(content)) {
                                    mSbPersonDataName.setRightText(content);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        })
                        .show();
                break;
            case R.id.sb_person_data_address:
                new AddressDialog.Builder(this)
                        //.setTitle("选择地区")
                        // 设置默认省份
                        .setProvince("广东省")
                        // 设置默认城市（必须要先设置默认省份）
                        .setCity("广州市")
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener(new AddressDialog.OnListener() {

                            @Override
                            public void onSelected(BaseDialog dialog, String province, String city, String area) {
                                String address = province + city + area;
                                if (!mSbPersonDataAddress.getRightText().equals(address)) {
                                    mSbPersonDataAddress.setRightText(address);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        })
                        .show();
                break;
            case R.id.sb_person_data_phone:
                // 先判断有没有设置过手机号
                if (true) {
                    f_startActivity(PhoneVerifyActivity.class);
                } else {
                    f_startActivity(PhoneResetActivity.class);
                }
                break;
            default:
                break;
            case R.id.sb_person_data_id:
                break;
        }
    }
}