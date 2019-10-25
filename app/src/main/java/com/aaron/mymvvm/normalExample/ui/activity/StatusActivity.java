package com.aaron.mymvvm.normalExample.ui.activity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import com.aaron.utilslibrary.BaseDialog;
import com.aaron.baselibrary.BaseActivity;
import com.aaron.mymvvm.R;
import com.aaron.mymvvm.normalExample.dialog.MenuDialog;
import com.aaron.mymvvm.normalExample.utils.StatusManager;

/**
 *    desc   : 加载使用案例
 */
public final class StatusActivity extends BaseActivity {
    private final StatusManager mStatusManager = new StatusManager();

    @Override
    protected int f_getLayoutId() {
        return R.layout.activity_status;
    }

    @Override
    protected void f_initView(Bundle savedInstanceState) {
        new MenuDialog.Builder(this)
                .setCancelable(false)
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setList("加载中", "请求错误", "空数据提示", "自定义提示")
                .setListener(new MenuDialog.OnListener() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, Object object) {
                        switch (position) {
                            case 0:
                                mStatusManager.showLoading(StatusActivity.this);
                                f_postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mStatusManager.showComplete();
                                    }
                                }, 2000);
                                break;
                            case 1:
                                mStatusManager.showError(f_getContentView());
                                break;
                            case 2:
                                mStatusManager.showEmpty(f_getContentView());
                                break;
                            case 3:
                                mStatusManager.showLayout(f_getContentView(),ContextCompat.getDrawable(StatusActivity.this, R.drawable.icon_hint_address), "还没有添加地址");
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {}
                })
                .show();
    }
}