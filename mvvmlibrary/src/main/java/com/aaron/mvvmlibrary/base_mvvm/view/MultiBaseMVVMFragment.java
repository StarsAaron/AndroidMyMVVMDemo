package com.aaron.mvvmlibrary.base_mvvm.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.aaron.mvvmlibrary.base_mvvm.msg.Messenger;
import com.aaron.mvvmlibrary.base_mvvm.viewmodel.BaseViewModel;
import com.aaron.utilslibrary.utils.android.ActivityUtils;

import java.util.ArrayList;

/**
 * 基础Fragment
 * <p>
 * 用法跟BaseActivity相同
 * 一个Fragment绑定多个ViewModel
 */
/*
class CollectListFragment:MultiBaseMVVMFragment<ActivityCollectListBinding>() {
    override fun f_getLayoutId(): Int {
        return R.layout.activity_collect_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        f_registorUIBinding(BR.vm, f_getViewModel(CollectVM::class.java))
        f_registorUIBinding(BR.vm, f_getViewModel(PersonVM::class.java))
        initObserver()
    }

    override fun f_getViewModelProviders(): ViewModelProvider? {
        //使用自定义的ViewModelFactory来创建ViewModel,f_getViewModel调用的时候就会使用该ViewModelProviders
        val factory: AppViewModelFactory? = AppViewModelFactory.getInstance(activity!!.application)
        return ViewModelProviders.of(this, factory)
    }

    private fun initObserver() {
        val vm = f_getViewModel(CollectVM::class.java)
        vm.refreshTrigger.observe(this, Observer {
            Log.i("CollectVM", it.toString())
        })
        val pervm = f_getViewModel(PersonVM::class.java)
        pervm.name.observe(this, Observer {
            Log.i("PersonVM", it)
        })
    }
}
 */
public abstract class MultiBaseMVVMFragment<V extends ViewDataBinding> extends BaseFragment {
    protected V binding;
    private ArrayList<BaseViewModel> xmlViewModelList = new ArrayList<>();

    @Override
    protected View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, f_getLayoutId(), container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除Messenger注册
        for (BaseViewModel viewModel : xmlViewModelList) {
            if (viewModel != null) {
                Messenger.getDefault().unregister(viewModel);
                viewModel.removeRxBus();
            }
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 注入绑定
     * 默认
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
     * 获取ViewModel
     *
     * @param viewModelClass viewmodel的类名
     * @return
     */
    public <T extends BaseViewModel> T f_getViewModel(Class<T> viewModelClass) {
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
