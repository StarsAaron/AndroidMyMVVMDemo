package com.aaron.mymvvm.normalExample.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import com.aaron.utilslibrary.BaseDialog;
import com.aaron.baselibrary.BaseActivity;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.helper.CacheDataManager;
import com.aaron.mymvvm.mvvmExample.LoginActivity;
import com.aaron.mymvvm.normalExample.dialog.MenuDialog;
import com.aaron.mymvvm.normalExample.dialog.UpdateDialog;
import com.aaron.mymvvm.normalExample.utils.AppConfig;
import com.aaron.utilslibrary.utils.ToastUtils;
import com.aaron.utilslibrary.utils.android.ActivityUtils;
import com.aaron.widgetlibrary.layout.SettingBar;
import com.aaron.widgetlibrary.view.SwitchButton;

/**
 * desc   : 设置界面
 * <p>
 * 自定义 SettingBar 使用
 */
public final class SettingActivity extends BaseActivity
        implements SwitchButton.OnCheckedChangeListener, View.OnClickListener {
    private SettingBar mSbSettingLanguage;
    private SettingBar mSbSettingUpdate;
    private SettingBar mSbSettingAgreement;
    private SettingBar mSbSettingAbout;
    private SwitchButton mSbSettingSwitch;
    private SettingBar mSbSettingAuto;
    private SettingBar mSbSettingCache;
    private SettingBar mSbSettingExit;


    @Override
    protected int f_getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void f_initView(Bundle savedInstanceState) {
        mSbSettingLanguage = (SettingBar) findViewById(R.id.sb_setting_language);
        mSbSettingLanguage.setOnClickListener(this);
        mSbSettingUpdate = (SettingBar) findViewById(R.id.sb_setting_update);
        mSbSettingUpdate.setOnClickListener(this);
        mSbSettingAgreement = (SettingBar) findViewById(R.id.sb_setting_agreement);
        mSbSettingAgreement.setOnClickListener(this);
        mSbSettingAbout = (SettingBar) findViewById(R.id.sb_setting_about);
        mSbSettingAbout.setOnClickListener(this);
        mSbSettingSwitch = (SwitchButton) findViewById(R.id.sb_setting_switch);
        mSbSettingAuto = (SettingBar) findViewById(R.id.sb_setting_auto);
        mSbSettingAuto.setOnClickListener(this);
        mSbSettingCache = (SettingBar) findViewById(R.id.sb_setting_cache);
        mSbSettingCache.setOnClickListener(this);
        mSbSettingExit = (SettingBar) findViewById(R.id.sb_setting_exit);
        mSbSettingExit.setOnClickListener(this);

        // 获取应用缓存大小
        mSbSettingCache.setRightText(CacheDataManager.getTotalCacheSize(this));
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sb_setting_language:
                // 底部选择框
                new MenuDialog.Builder(this)
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setList(R.string.setting_language_simple, R.string.setting_language_complex)
                        .setListener(new MenuDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String string) {
                                WebActivity.start(SettingActivity.this, "https://github.com/getActivity/MultiLanguages");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                            }
                        })
                        .setGravity(Gravity.BOTTOM)
                        .setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
                        .show();
                break;
            case R.id.sb_setting_update:
                // 本地的版本码和服务器的进行比较
                if (20 > AppConfig.getVersionCode()) {

                    new UpdateDialog.Builder(this)
                            // 版本名
                            .setVersionName("v 2.0")
                            // 文件大小
                            .setFileSize("10 M")
                            // 是否强制更新
                            .setForceUpdate(false)
                            // 更新日志
                            .setUpdateLog("到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥")
                            // 下载 url
                            .setDownloadUrl("https://raw.githubusercontent.com/getActivity/AndroidProject/master/AndroidProject.apk")
                            .show();
                } else {
                    ToastUtils.showShort(R.string.update_no_update);
                }
                break;
            case R.id.sb_setting_agreement:
                WebActivity.start(this, "https://github.com/getActivity/Donate");
                break;
            case R.id.sb_setting_auto:
                // 自动登录
                mSbSettingSwitch.setChecked(!mSbSettingSwitch.isChecked());
                break;
            case R.id.sb_setting_cache:
                // 清空缓存
                CacheDataManager.clearAllCache(this);
                // 重新获取应用缓存大小
                mSbSettingCache.setRightText(CacheDataManager.getTotalCacheSize(this));
                break;
            case R.id.sb_setting_exit:
                // 退出登录
                f_startActivity(LoginActivity.class);
                // 进行内存优化，销毁掉所有的界面
                ActivityUtils.removeAllActivity();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        ToastUtils.showShort("" + isChecked);
    }
}