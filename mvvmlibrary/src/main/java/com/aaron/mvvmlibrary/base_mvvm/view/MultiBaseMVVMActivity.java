package com.aaron.mvvmlibrary.base_mvvm.view;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.aaron.mvvmlibrary.base_mvvm.msg.Messenger;
import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;
import com.aaron.mvvmlibrary.base_mvvm.IBaseView;
import com.aaron.mvvmlibrary.bean.ViewModelData;
import com.aaron.utilslibrary.utils.android.ActivityUtils;

import java.util.ArrayList;

/**
 * 支持DataBinding框架的基Activity
 * 一个Activity绑定多个ViewModel
 * <p>
 * （1）自带Dialog，目前支持持showDialog() f_dismissDialog()方法，只能设置标题文字
 * （2）可选择重写initParam()，f_initData()，f_initViewObservable()方法完成初始化
 * （3）监听viewmodel的显示对话框，取消对话框，Activity跳转，返回按钮事件，结束页面事件触发动作
 * （4）提供Messenger，Rxbus统一管理
 **/
/*
 * 例子: 生成的Binding文件是：Xml布局文件名+Binding
 * class MainActivity : MultiBaseMVVMActivity<ActivityMainBinding>() {
 *
 *     override fun f_isRequestFullScreen(): Boolean {
 *         return true
 *     }
 *
 *     override fun f_getLayoutId(savedInstanceState: Bundle?): Int {
 *         return R.layout.activity_main
 *     }
 *
 *     override fun f_initViewObservable() {
 *         val viewModel = getViewModel(LoginViewModel::class.java)
 *         viewModel.userName.observe(this, Observer {
 *             KLog.i("名字：$it")
 *         })
 *         viewModel.password.observe(this, Observer {
 *             KLog.i("密码：$it")
 *         })
 *     }
 *
 *     override fun f_initViewModelList(viewModelBindinglist: ArrayList<ViewModelData>?) {
 *         viewModelBindinglist?.add(ViewModelData(BR.viewModel,LoginViewModel::class.java))
 *             ?:KLog.w("list: ArrayList<ViewModelData> 为空")
 *     }
 * }
 */
public abstract class MultiBaseMVVMActivity<V extends ViewDataBinding>
        extends BaseActivity implements IBaseView {
    protected V binding;
    private ArrayList<ViewModelData> xmlViewModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        f_initOnCreateFirstParam();
        // 设置绑定映射<BR.variableName,vm extends BaseViewModel>
        f_initViewModelList(xmlViewModelList);
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, f_getLayoutId());
        // 用LiveData取代並加上setLifecycleOwner就讓Data Binding具有lifecycle-aware性質了。
        binding.setLifecycleOwner(this);// 使用livedata实现双向绑定需要调用
        // ViewModel绑定跟初始化
        f_registorUIBinding();
        //页面数据初始化方法
        f_initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        f_initViewObservable();
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    protected void f_registorUIBinding() {
        if (xmlViewModelList.isEmpty()) {
            Log.w("f_registorUIBinding","》》》》 映射列表为空");
            return;
        }

        for (ViewModelData data : xmlViewModelList) {
            BaseViewModel viewModel = getViewModel(data.getViewModelClassName());
            Log.e("f_registorUIBinding","viewModel>>>>>>" + data.getViewModelClassName().getSimpleName() + viewModel);
            data.setViewModel(viewModel);
            //注册RxBus
            viewModel.registerRxBus();

            //关联ViewModel
            binding.setVariable(data.getVariableId(), viewModel);
            //让ViewModel拥有View的生命周期感应
            getLifecycle().addObserver(viewModel);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除Messenger注册
        for (ViewModelData data : xmlViewModelList) {
            BaseViewModel viewModel = data.getViewModel();
            if (viewModel != null) {
                Messenger.getDefault().unregister(viewModel);
                viewModel.removeRxBus();
            }
        }

        // 解除绑定
        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 获取ViewModel
     *
     * @param viewModelClass viewmodel的类名
     * @return
     */
    public <T extends BaseViewModel> T getViewModel(Class<T> viewModelClass) {
        ViewModelProvider viewModelProvider = f_getViewModelProviders();
        if (viewModelProvider != null) {
            return viewModelProvider.get(viewModelClass);
        }
        return ViewModelProviders.of(this).get(viewModelClass);
    }

    /**
     * 可重写该方法提供自定义的ViewModelProvider
     *
     * @return
     */
    public ViewModelProvider f_getViewModelProviders() {
        return null;
    }

    /**
     * 绑定ViewModel
     *
     * @param viewModelBindinglist 设置绑定映射，key为xml文件中的变量名，value为ViewModel
     *                             传入格式为 <BR.variableName,vm extends BaseViewModel>
     */
    public abstract void f_initViewModelList(ArrayList<ViewModelData> viewModelBindinglist);

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
