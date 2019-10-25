package com.aaron.mymvvm.mvvmExample

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.lifecycle.Observer
import com.aaron.mvvmlibrary.base_mvvm.view.BaseMVVMActivity
import com.aaron.mymvvm.BR
import com.aaron.mymvvm.R
import com.aaron.mymvvm.databinding.ActivityLoginBinding

/**
 * 登录页面
 */
class LoginActivity : BaseMVVMActivity<ActivityLoginBinding, LoginViewModel>() {
    /**
     * 获取布局 ID
     *
     * @return 布局layout的id
     */
    override fun f_getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun f_isRequestFullScreen(): Boolean {
        return true
    }

    override fun f_initVariableId(): Int {
        return BR.loginViewModel
    }

    override fun f_initViewObservable() {
        viewModel.passwordShowSwitch.observe(this, Observer {
            if (it == true) {
                //密码可见
                //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
                binding.ivSwichPasswrod.setImageResource(R.mipmap.ic_login_show_psw)
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                //密码不可见
                binding.ivSwichPasswrod.setImageResource(R.mipmap.ic_login_no_show_psw)
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        })
    }

    override fun f_initData() {

    }

    override fun f_initOnCreateFirstParam() {
    }

}
