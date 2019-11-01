package com.aaron.mvvmlibrary.base_mvvm.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.aaron.mvvmlibrary.base_mvvm.msg.Messenger;
import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;
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
class CollectListActivity : MultiBaseMVVMActivity<ActivityCollectListBinding>() {
    val adapter = CollectArticleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 一个页面可以绑定多个Viewmodel,只需调用f_registorUIBinding方法
        f_registorUIBinding(BR.vm,f_getViewModel(CollectVM::class.java))
        f_registorUIBinding(BR.vm,f_getViewModel(OtherVM::class.java))
        initRecyclerView()
    }

    // 可以不重写该方法，会使用默认的ViewModelProvider
    override fun f_getViewModelProviders(): ViewModelProvider? {
        //使用自定义的ViewModelFactory来创建ViewModel,f_getViewModel调用的时候就会使用该ViewModelProviders
        val factory: AppViewModelFactory? = AppViewModelFactory.getInstance(application)
        return ViewModelProviders.of(this, factory)
    }

    override fun f_getLayoutId(): Int {
        return R.layout.activity_collect_list
    }
}
 */
public abstract class MultiBaseMVVMActivity<V extends ViewDataBinding>
        extends BaseActivity {
    protected V binding;
    private ArrayList<BaseViewModel> xmlViewModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, f_getLayoutId());
        // 用LiveData取代並加上setLifecycleOwner就讓Data Binding具有lifecycle-aware性質了。
        binding.setLifecycleOwner(this);// 使用livedata实现双向绑定需要调用
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    protected <T extends BaseViewModel> void f_registorUIBinding(int variableId, T viewModel) {
        xmlViewModelList.add(viewModel);
        //注册RxBus
        viewModel.registerRxBus();

        //关联ViewModel
        binding.setVariable(variableId, viewModel);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除Messenger注册
        for (BaseViewModel viewModel : xmlViewModelList) {
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
    public <T extends ViewModel> T f_getViewModel(Class<T> viewModelClass) {
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

}
