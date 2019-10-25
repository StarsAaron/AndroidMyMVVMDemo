package com.aaron.netlibrary.net.converter;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：Aaron
 * 时间：2019/10/24:14:34
 * 邮箱：
 * 说明：
 */
public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {
    private Type responseType;

    LiveDataCallAdapter(Type responseType){
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<T> adapt(final Call<T> call) {
        return new LiveData<T>() {
            private AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if(started.compareAndSet(false,true)){
                    call.enqueue(new Callback<T>() {
                        @Override
                        public void onResponse(Call<T> call, Response<T> response) {
                            postValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<T> call, Throwable t) {
                            T value = (T)new ApiResponse<T>(null,-1, t.getMessage());
                            postValue(value);
                        }
                    });
                }
            }
        };
    }
}
