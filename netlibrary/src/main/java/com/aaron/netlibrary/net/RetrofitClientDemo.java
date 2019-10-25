//package com.aaron.netlibrary.net;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.aaron.netlibrary.net.interceptor.CacheInterceptor;
//import com.aaron.netlibrary.net.interceptor.LoggingInterceptor;
//
//import java.io.File;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import okhttp3.Cache;
//import okhttp3.ConnectionPool;
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * RetrofitClient封装单例类, 实现网络请求
// * <p>
// * RetrofitClientDemo.getInstance().getService(DemoApiService.class)
// * .login(name,password)
// * .subscribeOn(Schedulers.io())
// * .observeOn(AndroidSchedulers.mainThread())
// * .subscribe(subscriber);
// */
//public class RetrofitClientDemo {
//    //超时时间
//    private static final int DEFAULT_TIMEOUT = 20;
//    //缓存时间
//    private static final int CACHE_TIMEOUT = 10 * 1024 * 1024;
//    //服务端根路径
//    public static String baseUrl = "https://www.oschina.net/";
//    private static Context mContext = Utils.getContext();
//    private final Map<String, Object> mServiceMap = new ConcurrentHashMap();
//    private Cache cache = null;
//    private File httpCacheDirectory;
//
//    private RetrofitClientDemo() {
//        createRetrofit(baseUrl);
//    }
//
//    private static class SingletonHolder {
//        private static RetrofitClientDemo INSTANCE = new RetrofitClientDemo();
//    }
//
//    public static RetrofitClientDemo getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    /**
//     * 修改默认URL地址
//     *
//     * @param urlstr
//     */
//    private void initBaseUrl(String urlstr) {
//        this.baseUrl = urlstr;
//    }
//
//    /**
//     * 创建Retrofit实例对象
//     *
//     * @param url 链接地址
//     * @return
//     */
//    private Retrofit createRetrofit(String url) {
//        if (TextUtils.isEmpty(url)) {
//            url = baseUrl;
//        }
//
////        if (httpCacheDirectory == null) {
////            httpCacheDirectory = new File(mContext.getCacheDir(), "goldze_cache");
////        }
//
////        try {
////            if (cache == null) {
////                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
////            }
////        } catch (Exception e) {
////            KLog.e("Could not create http cache", e);
////        }
////        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
//                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
////                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext))) // cookie缓存
////                .cache(cache) // 缓存
////                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) // ssl
//                .addInterceptor(new CacheInterceptor(mContext)); // 缓存拦截器
//        if (BuildConfig.DEBUG) { // 日志拦截器
//            builder.addInterceptor(new LoggingInterceptor());
//        }
//        return new Retrofit.Builder()
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl(url)
//                .build();
//    }
//
////    <T> ObservableTransformer<BaseResponse<T>, T> applySchedulers() {
////        return observable -> observable.subscribeOn(Schedulers.io())
////                .unsubscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .flatMap(result -> {
////                    switch (result.getCode()) {
////                        case HttpCode.CODE_SUCCESS:
////                            return createData(result.getData());
////                        case HttpCode.CODE_TOKEN_INVALID: {
////                            throw new TokenInvalidException();
////                        }
////                        case HttpCode.CODE_ACCOUNT_INVALID: {
////                            throw new AccountInvalidException();
////                        }
////                        default: {
////                            throw new ServerResultException(result.getCode(), result.getMsg());
////                        }
////                    }
////                });
////    }
//
//    private <T> ObservableSource<? extends T> createData(T t) {
//        return Observable.create(emitter -> {
//            try {
//                emitter.onNext(t);
//                emitter.onComplete();
//            } catch (Exception e) {
//                emitter.onError(e);
//            }
//        });
//    }
//
//    /**
//     * 获取API接口实例
//     *
//     * @param clazz API 实例类
//     * @param <T>   API 接口类类型
//     * @return
//     */
//    public <T> T getService(Class<T> clazz) {
//        return getService(clazz, baseUrl);
//    }
//
//    /**
//     * 获取API接口实例
//     *
//     * @param clazz API 实例类
//     * @param host  链接地址
//     * @param <T>   API 接口类类型
//     * @return
//     */
//    public <T> T getService(Class<T> clazz, String host) {
//        T value;
//        if (mServiceMap.containsKey(host)) {
//            Object obj = mServiceMap.get(host);
//            if (obj == null) {
//                value = createRetrofit(host).create(clazz);
//                mServiceMap.put(host, value);
//            } else {
//                value = (T) obj;
//            }
//        } else {
//            value = createRetrofit(host).create(clazz);
//            mServiceMap.put(host, value);
//        }
//        return value;
//    }
//}
