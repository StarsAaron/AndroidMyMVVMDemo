package com.aaron.mymvvm.mvvmExample

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.aaron.mvvmlibrary.base_mvvm.view.MultiBaseMVVMActivity
import com.aaron.mvvmlibrary.bean.ViewModelData
import com.aaron.mymvvm.BR
import com.aaron.mymvvm.R
import com.aaron.mymvvm.databinding.ActivityMain2Binding
import com.aaron.utilslibrary.RequestPermissionsActivity
import com.aaron.utilslibrary.utils.KLog
import com.aaron.utilslibrary.utils.ToastUtils

class Main2Activity : MultiBaseMVVMActivity<ActivityMain2Binding>() {
    /**
     * 获取布局 ID
     *
     * @return 布局layout的id
     */
    override fun f_getLayoutId(): Int {
        return R.layout.activity_main2
    }

    override fun f_isRequestFullScreen(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(RequestPermissionsActivity.lacksPermissions(this,"android.permission.WRITE_EXTERNAL_STORAGE")){
            RequestPermissionsActivity.requestPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")
        }

        f_registorUIBinding(BR.viewModel,f_getViewModel(LoginViewModel::class.java))
        f_initViewObservable()
    }

    fun f_initViewObservable() {
        val viewModel = f_getViewModel(LoginViewModel::class.java)
        KLog.e("viewModel>>>>>>$viewModel")
        viewModel.userName.observe(this, Observer {
            ToastUtils.showShort("名字：$it")
            KLog.i("名字：$it")
        })
        viewModel.password.observe(this, Observer {
            ToastUtils.showShort("密码：$it")
            KLog.i("密码：$it")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RequestPermissionsActivity.PERMISSION_REQUEST_CODE
            && resultCode == RequestPermissionsActivity.PERMISSIONS_DENIED){
            ToastUtils.showShort("请授予相关权限")
            finish()
        }
    }
}
