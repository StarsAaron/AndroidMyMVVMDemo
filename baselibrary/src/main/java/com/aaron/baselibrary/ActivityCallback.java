package com.aaron.baselibrary;

import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * 作者：Aaron
 * 时间：2019/10/22:16:39
 * 邮箱：
 * 说明：Activity 回调接口
 */
public interface ActivityCallback {
    /**
     * 结果回调
     *
     * @param resultCode        结果码
     * @param data              数据
     */
    void onActivityResult(int resultCode, @Nullable Intent data);
}
