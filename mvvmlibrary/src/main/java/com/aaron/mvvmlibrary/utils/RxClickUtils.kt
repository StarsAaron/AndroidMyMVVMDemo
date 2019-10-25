package com.aaron.mvvmlibrary.utils

import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 作者：Aaron
 * 时间：2018/9/21:8:57
 * 邮箱：
 * 说明：防止点击过快
compositeDisposable.add(RxUtils.clickView(btnClose as View) {
Toast.makeText(applicationContext, "点击", Toast.LENGTH_SHORT).show()
})
 */
object RxUtils {
    fun clickView(@NonNull view: View, action: () -> Unit): Disposable {
        return Observable.create(ObservableOnSubscribe<View> { emitter ->
            view.setOnClickListener {
                //订阅没取消
                if (!emitter.isDisposed) {
                    //发送消息
                    emitter.onNext(it)
                }
            }
        }).throttleFirst(1, TimeUnit.SECONDS)
            .subscribe { action() }
    }
}
