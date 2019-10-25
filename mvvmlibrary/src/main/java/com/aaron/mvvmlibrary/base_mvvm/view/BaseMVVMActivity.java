package com.aaron.mvvmlibrary.base_mvvm.view;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.aaron.mvvmlibrary.base_mvvm.msg.Messenger;
import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;
import com.aaron.mvvmlibrary.base_mvvm.IBaseView;
import com.aaron.utilslibrary.utils.android.ActivityUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 支持DataBinding框架的基Activity
 * <p>
 * （1）自带Dialog，目前支持持showDialog() f_dismissDialog()方法，只能设置标题文字
 * （2）可选择重写initParam()，f_initData()，f_initViewObservable()方法完成初始化
 * （3）监听viewmodel的显示对话框，取消对话框，Activity跳转，返回按钮事件，结束页面事件触发动作
 * （4）提供Messenger，Rxbus统一管理
 */
public abstract class BaseMVVMActivity<V extends ViewDataBinding, VM extends BaseViewModel>
        extends BaseActivity implements IBaseView {
    protected V binding;
    protected VM viewModel;
    private int viewModelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        f_initOnCreateFirstParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        f_initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        f_initViewObservable();
        //注册RxBus
        viewModel.registerRxBus();
    }

    //注册ViewModel与View的契约UI回调事件
    private void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getBaseUILiveDataEvent().getShowDialogEvent().observe(this, dialogData -> {
            if (dialogData.getIsProcessDialog() == 0) {
                f_showProgressDialog();
            } else if (dialogData.getIsProcessDialog() == 1) {
                f_showDialogWithTitle(dialogData.getTitle());
            } else if (dialogData.getIsProcessDialog() == 2) {
                f_showDialogWithAction(dialogData.getTitle(), dialogData.getOkListener());
            }
        });
        //加载对话框消失
        viewModel.getBaseUILiveDataEvent().getDismissDialogEvent().observe(this, params -> f_dismissDialog());
        //跳入新页面
        viewModel.getBaseUILiveDataEvent().getStartActivityEvent().observe(this, params -> {
            Class<?> clz = (Class<?>) params.get(BaseViewModel.ParameterField.CLASS);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
            f_startActivity(clz, bundle);
        });
        //跳入ContainerActivity
        viewModel.getBaseUILiveDataEvent().getStartContainerActivityEvent().observe(this, params -> {
            String canonicalName = (String) params.get(BaseViewModel.ParameterField.CANONICAL_NAME);
            Bundle bundle = (Bundle) params.get(BaseViewModel.ParameterField.BUNDLE);
            f_startContainerActivity(canonicalName, bundle);
        });
        //关闭界面
        viewModel.getBaseUILiveDataEvent().getFinishEvent().observe(this, params -> finish());
        //关闭上一层
        viewModel.getBaseUILiveDataEvent().getOnBackPressedEvent().observe(this, params -> onBackPressed());
        // 退出应用
        viewModel.getBaseUILiveDataEvent().getExitAppEvent().observe(this, params -> {
                    if (params) {
                        ActivityUtils.removeAllActivity();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除Messenger注册
        Messenger.getDefault().unregister(viewModel);
        if (viewModel != null) {
            viewModel.removeRxBus();
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 注入绑定
     * <p>
     * 在Activity的onCreate方法中调用
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, f_getLayoutId());
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
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        // 用LiveData取代並加上setLifecycleOwner就讓Data Binding具有lifecycle-aware性質了。
        binding.setLifecycleOwner(this);// 使用livedata实现双向绑定需要调用

        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
    }

    /**
     * 刷新布局
     */
    public void f_refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM f_initViewModel() {
        return null;
    }

    /**
     * 创建默认ViewModel，在viewmodel为空的时候调用
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T f_createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
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

    /**
     * 初始化ViewModel的id
     * <p>
     * xml布局文件中Variable定义的name
     * BR.name
     *
     * @return BR的id
     */
    public abstract int f_initVariableId();
}
