package com.aaron.mvvmlibrary.base_mvvm.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import com.aaron.mvvmlibrary.base_mvvm.msg.Messenger;
import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;
import com.aaron.mvvmlibrary.base_mvvm.IBaseView;
import com.aaron.utilslibrary.utils.android.ActivityUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * 基础Fragment
 * <p>
 * 用法跟BaseActivity相同
 *
 * @param <V>
 * @param <VM>
 */
public abstract class BaseMVVMFragment<V extends ViewDataBinding, VM extends BaseViewModel>
        extends BaseFragment implements IBaseView {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f_initOnCreateFirstParam();
    }

    @Override
    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, f_getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除Messenger注册
        Messenger.getDefault().unregister(viewModel);
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //私有的初始化Databinding和ViewModel方法
        f_initViewDataBinding();
        //私有的ViewModel与View的契约事件回调逻辑
        f_registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        f_initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        f_initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
    }

    /**
     * 注入绑定
     * <p>
     * 默认
     */
    private void f_initViewDataBinding() {
        viewModelId = f_initVariableId();
        viewModel = f_initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) f_createViewModel(this, modelClass);
        }
        // 用LiveData取代並加上setLifecycleOwner就讓Data Binding具有lifecycle-aware性質了。
        binding.setVariable(viewModelId, viewModel);
        binding.setLifecycleOwner(this);

        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private void f_registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getBaseUILiveDataEvent().getShowDialogEvent().observe(this, dialogData -> {
            if (dialogData.getIsProcessDialog() == 0) {
                f_showProcessDialog();
            } else if (dialogData.getIsProcessDialog() == 1) {
                f_showDialogWithTitle(dialogData.getTitle());
            } else if (dialogData.getIsProcessDialog() == 2) {
                f_showDialogWithAction(dialogData.getTitle(), dialogData.getOkListener());
            }
        });
        //加载对话框消失
        viewModel.getBaseUILiveDataEvent().getDismissDialogEvent().observe(this, v -> f_dismissDialog());
        //跳入新页面
        viewModel.getBaseUILiveDataEvent().getStartActivityEvent().
                observe(this, params -> {
                    Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
                    Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                    f_startActivity(clz, bundle);
                });
        //跳入ContainerActivity
        viewModel.getBaseUILiveDataEvent().
                getStartContainerActivityEvent().
                observe(this, params -> {
                    String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
                    Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
                    f_startContainerActivity(canonicalName, bundle);
                });
        //关闭界面
        viewModel.getBaseUILiveDataEvent().getFinishEvent().observe(this, v -> getActivity().finish());
        //关闭上一层
        viewModel.getBaseUILiveDataEvent().getOnBackPressedEvent().observe(this, v -> getActivity().onBackPressed());
        // 退出应用
        viewModel.getBaseUILiveDataEvent().getExitAppEvent().observe(this, params -> {
            if (params) {
                ActivityUtils.removeAllActivity();
            }
        });
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T f_createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int f_initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM f_initViewModel() {
        return null;
    }

    /**
     * 在onCreate方法首先执行
     */
    @Override
    public void f_initOnCreateFirstParam() {
    }

    /**
     * 初始化页面数据
     */
    @Override
    public void f_initData() {
    }

    /**
     * 初始化观察者
     */
    @Override
    public void f_initViewObservable() {
    }
}
