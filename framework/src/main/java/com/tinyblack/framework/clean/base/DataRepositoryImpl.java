package com.tinyblack.framework.clean.base;


import com.tinyblack.framework.data.DataCache;
import com.tinyblack.framework.data.cache.DiskCache;
import com.tinyblack.framework.data.cache.MemoryCache;
import com.tinyblack.framework.data.cache.SPCache;
import com.tinyblack.framework.net.RetrofitHelper;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * @author yubiao
 */
public abstract class DataRepositoryImpl implements DataRepository {

    protected <T> T createService(String url, Class<T> serviceClass, retrofit2.Converter.Factory factory, Interceptor... interceptors) {
        OkHttpClient okHttpClient = RetrofitHelper.instance().createClient(interceptors);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                //避免出现双括号
                .addConverterFactory(RetrofitHelper.instance().getScalarsConverterFactory())
                .addConverterFactory(factory)
                .client(okHttpClient)
                .build();
        return retrofit.create(serviceClass);
    }

    protected <T> T createService(String url, Class<T> serviceClass, Interceptor... interceptors) {
        retrofit2.Converter.Factory factory = RetrofitHelper.instance().getFactory();
        return createService(url, serviceClass, factory, interceptors);
    }
    protected <T> T createService( Class<T> serviceClass, Interceptor... interceptors) {
        retrofit2.Converter.Factory factory = RetrofitHelper.instance().getFactory();
        return createService(baseUrl(), serviceClass, factory, interceptors);
    }

    protected <T> T createService(String url, Class<T> serviceClass) {
        return createService(url, serviceClass, (Interceptor) null);
    }

    protected <T> T createService(Class<T> serviceClass) {
        return createService(baseUrl(), serviceClass);
    }

    public abstract String baseUrl();

    @Override
    public MemoryCache getMemoryCache() {
        return DataCache.instance().getMemoryCache();
    }

    @Override
    public DiskCache getDiskCache() {
        return DataCache.instance().getDiskCache();
    }

    @Override
    public SPCache getSpCache() {
        return DataCache.instance().getSpCache();
    }
}