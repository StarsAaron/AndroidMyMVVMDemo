package com.aaron.netlibrary.net.converter;

import androidx.lifecycle.LiveData;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * 作者：Aaron
 * 时间：2019/10/24:14:25
 * 邮箱：
 * 说明：转换LiveData
 *
 * Retrofit.Builder()
 *         .baseUrl("https://www.wanandroid.com/")
 *         .client(clientBuilder.build())
 *         .addCallAdapterFactory(LiveDataCallAdapterFactory())
 *         .build()
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if(getRawType(returnType) != LiveData.class){
            return null;
        }

        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType)returnType);
        Class rawType = getRawType(observableType);
        if (rawType != ApiResponse.class) {
            throw new IllegalArgumentException("type must be ApiResponse");
        }
        if(!(observableType instanceof ParameterizedType)){
            throw new IllegalArgumentException("resource must be parameterized");
        }
        return new LiveDataCallAdapter(observableType);
    }
}
