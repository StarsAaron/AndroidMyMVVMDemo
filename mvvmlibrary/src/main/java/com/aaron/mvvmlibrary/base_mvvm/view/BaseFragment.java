package com.aaron.mvvmlibrary.base_mvvm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.aaron.mvvmlibrary.R;
import com.aaron.nicedialoglibrary.NiceDialog;

import java.util.Objects;
import java.util.Random;

/*
Dialog相关方法 》》》
f_showProgressDialog() 展示进度框
f_showDialogWithTitle(String title) 显示只有标题对话框
f_showDialogWithAction(String title, View.OnClickListener okListener) 显示带按钮的对话框
f_dismissDialog() 取消信息对话框

startActivity相关方法 》》》
f_startActivity(Class<?> clz)
f_startActivity(Class<?> clz, Bundle bundle)
f_startActivityFinish(Class<? extends Activity> cls)
f_startActivityFinish(Intent intent)
f_finish()

启动容器Activity相关方法 》》》
f_startContainerActivity(String canonicalName)
f_startContainerActivity(String canonicalName, Bundle bundle)

startActivityForResult 相关方法 》》》
f_startActivityForResult(Class<? extends Activity> cls, ActivityCallback callback)
f_startActivityForResult(Intent intent, ActivityCallback callback)
f_startActivityForResult(Intent intent, @Nullable Bundle options, ActivityCallback callback)

获取控件相关 》》》
f_findViewById(@IdRes int id)
f_findActivityViewById(@IdRes int id)

f_getSystemService(@NonNull Class<T> serviceClass)  获取系统服务
f_getAttachActivity() 获取绑定的 Activity，防止出现 getActivity 为空
f_getLayoutId() 引入布局
f_isBackPressed() ContainerActivity 中用于返回按钮的逻辑
f_onKeyDown(int keyCode, KeyEvent event) Fragment 返回键被按下时回调
 */

/**
 * 作者：Aaron
 * 时间：2019/10/22:14:36
 * 邮箱：
 * 说明：
 */
public abstract class BaseFragment extends Fragment {
    /**
     * Activity对象
     */
    private FragmentActivity mActivity;
    private ActivityCallback mActivityCallback;
    private int mActivityRequestCode;
    private NiceDialog dialog;
    /**
     * 根布局
     */
    private View mRootView;

    /**
     * 获得全局的 Activity
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = createView(inflater,container,savedInstanceState);
//        if(mRootView != null){
//            return mRootView;
//        }
        if (mRootView == null && f_getLayoutId() > 0) {
            mRootView = inflater.inflate(f_getLayoutId(), null);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return null;
    }

    /**
     * 获取绑定的 Activity，防止出现 getActivity 为空
     */
    public FragmentActivity f_getAttachActivity() {
        return mActivity;
    }

    /**
     * f_startActivity 方法优化
     */
    public void f_startActivity(Class<? extends Activity> cls) {
        startActivity(new Intent(mActivity, cls));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void f_startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mActivity, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转之后关闭当前页面
     *
     * @param cls
     */
    public void f_startActivityFinish(Class<? extends Activity> cls) {
        f_startActivityFinish(new Intent(mActivity, cls));
    }

    /**
     * 跳转之后关闭当前页面
     *
     * @param intent
     */
    public void f_startActivityFinish(Intent intent) {
        startActivity(intent);
        f_finish();
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void f_startContainerActivity(String canonicalName) {
        f_startContainerActivity(canonicalName, null);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     * @param bundle        跳转所携带的信息
     */
    public void f_startContainerActivity(String canonicalName, Bundle bundle) {
        Intent intent = new Intent(mActivity, ContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }

    /**
     * f_startActivityForResult 方法优化
     */
    public void f_startActivityForResult(Class<? extends Activity> cls,ActivityCallback callback) {
        f_startActivityForResult(new Intent(mActivity, cls), null, callback);
    }

    public void f_startActivityForResult(Intent intent, ActivityCallback callback) {
        f_startActivityForResult(intent, null, callback);
    }

    public void f_startActivityForResult(Intent intent, Bundle options, ActivityCallback callback) {
        // 回调还没有结束，所以不能再次调用此方法，这个方法只适合一对一回调，其他需求请使用原生的方法实现
        if (mActivityCallback == null) {
            mActivityCallback = callback;
            // 随机生成请求码，这个请求码在 0 - 255 之间
            mActivityRequestCode = new Random().nextInt(255);
            startActivityForResult(intent, mActivityRequestCode, options);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mActivityCallback != null && mActivityRequestCode == requestCode) {
            mActivityCallback.onActivityResult(resultCode, data);
            mActivityCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 销毁对话框资源
        f_dismissDialog();
    }

    /**
     * 展示进度框
     */
    public void f_showProcessDialog() {
        f_dismissDialog();
        dialog = new NiceDialog(getActivity())
                .setLayoutId(R.layout.dialog_progress)
                .setWidth(58)
                .setHeight(58)
                .setOutCancel(false)
                .show(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
    }

    /**
     * 显示只有标题对话框
     */
    public void f_showDialogWithTitle(String title) {
        f_dismissDialog();
        dialog = NiceDialog.createDialogWithConfirmButton(getActivity()
                , Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                , title, view -> f_dismissDialog());
    }

    /**
     * 显示带按钮的对话框
     *
     * @param title
     * @param okListener
     */
    public void f_showDialogWithAction(String title, View.OnClickListener okListener) {
        f_dismissDialog();
        dialog = NiceDialog.createDialogWithConfirmButton(getActivity()
                , Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                , title, okListener);
    }

    /**
     * 取消Dialog显示
     */
    public void f_dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 根据资源 id 获取一个 View 对象
     */
    protected <V extends View> V f_findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    protected <V extends View> V f_findActivityViewById(@IdRes int id) {
        return mActivity.findViewById(id);
    }

    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    public void f_finish() {
        mActivity.finish();
        mActivity = null;
    }

    /**
     * 获取系统服务
     */
    public <T> T f_getSystemService(@NonNull Class<T> serviceClass) {
        return ContextCompat.getSystemService(mActivity, serviceClass);
    }

    /**
     * Fragment 返回键被按下时回调
     */
    public boolean f_onKeyDown(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件，回传给 Activity
        return false;
    }

    /**
     * 引入布局
     */
    protected abstract int f_getLayoutId();

    /**
     * ContainerActivity 中用于返回按钮的逻辑
     * @return
     */
    public boolean f_isBackPressed() {
        return false;
    }

}
