package com.aaron.mvvmlibrary.base_mvvm.view;

import android.os.Bundle;
import android.util.Log;
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
import com.aaron.mvvmlibrary.base_mvvm.IBaseView;
import com.aaron.mvvmlibrary.bean.ViewModelData;
import com.aaron.utilslibrary.utils.android.ActivityUtils;
import java.util.ArrayList;

/**
 * 基础Fragment
 * <p>
 * 用法跟BaseActivity相同
 * 一个Fragment绑定多个ViewModel
 *
 * @param <V>
 */

/*
class BuyFragment : MultiBaseMVVMFragment<FragmentBuyBinding>() {

    override fun f_getLayoutId(inflater: LayoutInflater?,container: ViewGroup?,
	       savedInstanceState: Bundle?): Int {
        return R.layout.fragment_buy
    }

    override fun f_getViewModelProviders(): ViewModelProvider {
		// AppViewModelFactory 自定义的factory
        val factory: AppViewModelFactory? = activity?.application?.let {
            AppViewModelFactory.getInstance(it)
        }
        return ViewModelProviders.of(this, factory)
    }

    override fun f_initViewModelList(viewModelBindinglist: java.util.ArrayList<ViewModelData>?) {
        viewModelBindinglist?.add(ViewModelData(BR.buyViewModel, BuyViewModel::class.java))
        viewModelBindinglist?.add(ViewModelData(BR.positionViewModel,PositionViewModel::class.java))
    }
    ...
}
 */
public abstract class MultiBaseMVVMFragment<V extends ViewDataBinding> extends BaseFragment implements IBaseView {
    protected V binding;
    private ArrayList<ViewModelData> xmlViewModelList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        f_initOnCreateFirstParam();
    }

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
        for (ViewModelData data : xmlViewModelList) {
            BaseViewModel viewModel = data.getViewModel();
            if (viewModel != null) {
                Messenger.getDefault().unregister(viewModel);
                viewModel.removeRxBus();
            }
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置绑定映射<BR.variableName,vm extends BaseViewModel>
        f_initViewModelList(xmlViewModelList);
        //私有的初始化Databinding和ViewModel方法
        f_registorUIBinding();
        //页面数据初始化方法
        f_initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        f_initViewObservable();
    }

    /**
     * 注入绑定
     * 默认
     */
    private void f_registorUIBinding() {
        if (xmlViewModelList.isEmpty()) {
            Log.w("f_registorUIBinding","》》》》 映射列表为空");
            return;
        }
        for (ViewModelData data : xmlViewModelList) {
            BaseViewModel viewModel = f_getViewModel(data.getViewModelClassName());
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
     * 绑定ViewModel
     *
     * @param viewModelBindinglist 设置绑定映射，key为xml文件中的变量名，value为ViewModel
     *                             传入格式为 <BR.variableName,vm extends BaseViewModel>
     */
    public abstract void f_initViewModelList(ArrayList<ViewModelData> viewModelBindinglist);

}
