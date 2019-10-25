package com.aaron.netlibrary.net.interceptor.example;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 向请求头里添加公共参数例子
 * <p>
 * 不可用
 */
public class RequestParamsInterceptor implements Interceptor {
    private Map<String, String> mHeaderParamsMap = new HashMap<>();

    public RequestParamsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("ParamsInterceptor", "add common params");
        Request oldRequest = chain.request();

        /**
         * 添加新的参数，添加到url 中
         HttpUrl.Builder builder = oldRequest.url().newBuilder()
         .addQueryParameter("platform", "android")
         .addQueryParameter("version", "1.0.0")
         .scheme(oldRequest.url().scheme())
         .host(oldRequest.url().host())
         */
        // 新的请求
        Request.Builder requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());
        //添加公共参数,添加到header中
        if (mHeaderParamsMap.size() > 0) {
            for (Map.Entry<String, String> params : mHeaderParamsMap.entrySet()) {
                requestBuilder.header(params.getKey(), params.getValue());
            }
        }
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    public static class Builder {
        RequestParamsInterceptor mRequestParamsInterceptor;

        public Builder() {
            mRequestParamsInterceptor = new RequestParamsInterceptor();
        }

        public Builder addHeaderParams(String key, String value) {
            mRequestParamsInterceptor.mHeaderParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParams(String key, int value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, float value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, long value) {
            return addHeaderParams(key, String.valueOf(value));
        }

        public Builder addHeaderParams(String key, double value) {
            return addHeaderParams(key, String.valueOf(value));
        }


        public RequestParamsInterceptor build() {
            return mRequestParamsInterceptor;
        }
    }
}
