package com.aaron.netlibrary.net.converter;

/**
 * 作者：Aaron
 * 时间：2019/10/24:14:40
 * 邮箱：
 * 说明：响应格式
 */
class ApiResponse<T> {
    private T data;
    private int errorCode;
    private String errorMsg;

    ApiResponse(T data,int errorCode,String errorMsg){
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
