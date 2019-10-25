package com.aaron.netlibrary.net.interceptor.example;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求加密响应解密拦截器例子
 * 不可用，自己写
 */
public class EncryptInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //这个是请求的url，也就是咱们前面配置的baseUrl
        String url = request.url().toString();
        //这个是请求方法 
        String method = request.method();
//      request = encrypt(request);
        //模拟的加密方法 
        Response response = chain.proceed(request);
//        response = decrypt(response);
        return response;
    }


}

