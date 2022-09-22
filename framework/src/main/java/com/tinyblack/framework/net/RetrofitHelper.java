package com.tinyblack.framework.net;

import android.content.Context;

import com.google.gson.Gson;
import com.tinyblack.framework.net.gson.GsonConverterFactory;
import com.tinyblack.framework.net.interceptor.log.RequestInterceptor;
import com.tinyblack.framework.net.ssl.MutualAuthenticationHelper;
import com.tinyblack.framework.net.ssl.SSLHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * @author yubiao
 */
public class RetrofitHelper {

    private Set<Interceptor> defInterceptors;
    private Context context;
    private OkHttpClient defOkHttpClient;
    private static RetrofitHelper INSTANCE;

    private ScalarsConverterFactory scalarsConverterFactory;
    private Converter.Factory factory;
    private CookieJar cookieJar;

    private RetrofitHelper(Context context) {
        this.context = context;
        this.defInterceptors = new HashSet<>();

    }

    public static synchronized void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitHelper(context);
        }
    }

    public void setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public void setFactory(Converter.Factory factory) {
        this.factory = factory;
    }

    public Converter.Factory getFactory() {
        if (factory == null) {
            factory = GsonConverterFactory.create(new Gson());
        }
        return factory;
    }

    public ScalarsConverterFactory getScalarsConverterFactory() {
        if (scalarsConverterFactory == null) {
            scalarsConverterFactory = ScalarsConverterFactory.create();
        }
        return scalarsConverterFactory;
    }

    public static RetrofitHelper instance() {
        return INSTANCE;
    }

    public void addInterceptor(Interceptor interceptor) {
        if (defInterceptors == null) {
            synchronized (this) {
                defInterceptors = new HashSet<>();
            }
        }
        defInterceptors.add(interceptor);
    }

    public void addInterceptor(Set<Interceptor> interceptors) {
        if (defInterceptors == null) {
            synchronized (this) {
                defInterceptors = new HashSet<>();
            }
        }
        defInterceptors.addAll(interceptors);
    }

    /**
     * create a OkHttpClient if it is null
     *
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        if (defOkHttpClient == null) {
            synchronized (this) {
                if (defOkHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            //默认重试一次，若需要重试N次，则要实现拦截器。
                            .retryOnConnectionFailure(true).connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS);
                    //ssl
//                    builder.sslSocketFactory(SSLHelper.getInstance(), SSLHelper.getTrustManagers());
                    MutualAuthenticationHelper.addSSLCertification(builder);
                    builder.hostnameVerifier((hostname, session) -> true);
                    //添加拦截器
                    if (defInterceptors != null) {
                        //是否包含缓存的拦截器
                        boolean hasCacheInterceptor = false;
//                        CacheInterceptor cacheInterceptor=null;
                        for (Interceptor interceptor : defInterceptors) {
//                            if (interceptor instanceof CacheInterceptor) {
//                                hasCacheInterceptor = true;
//                                cacheInterceptor= (CacheInterceptor) interceptor;
//                            }
                            builder.addInterceptor(interceptor);
                        }
                        //缓存
//                        if (hasCacheInterceptor) {
//                            Cache cache=getCache();
//                            builder.cache(getCache());
//                            cacheInterceptor.setCache(cache);
//                        }
                    }

                    //cookieJar
                    if (cookieJar != null) {
                        builder.cookieJar(cookieJar);
                    }

                    defOkHttpClient = builder.build();

                }
            }
        }
        return defOkHttpClient;
    }

    public void setDefOkHttpClient(OkHttpClient defOkHttpClient) {
        this.defOkHttpClient = defOkHttpClient;
    }

    /**
     * create a Okhttp buider
     * use the def
     *
     * @return
     */
    private OkHttpClient.Builder newBuilder() {
        OkHttpClient okHttpClient = getOkHttpClient();
        return okHttpClient.newBuilder();
    }

    /**
     * 创建okHttpClient实例
     *
     * @param interceptors 自定义拦截器
     * @return
     */
    public OkHttpClient createClient(Interceptor... interceptors) {
        if (interceptors == null || interceptors.length == 0) {
            return getOkHttpClient();
        }
        OkHttpClient.Builder builder = newBuilder();

        for (Interceptor interceptor : interceptors) {
            if (interceptor == null) {
                continue;
            }
            builder.addInterceptor(interceptor);
        }
        //添加http拦截
//        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
//        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        RequestInterceptor requestInterceptor = new RequestInterceptor();
        builder.addInterceptor(requestInterceptor);

        return builder.build();
    }


}
