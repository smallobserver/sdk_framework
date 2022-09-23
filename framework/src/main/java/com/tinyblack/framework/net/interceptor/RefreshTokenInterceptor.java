package com.tinyblack.framework.net.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tinyblack.framework.controller.TokenController;
import com.tinyblack.framework.log.TLog;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 默认的 token 及刷新 token 的拦截器
 *
 * @author yubiao
 */
public class RefreshTokenInterceptor implements Interceptor {
    private final int ERROR_CODE = 401;
    private final String AUTHOR = "Authorization";
    private final int REFRESH_TOKEN_WAIT_TIME = 30;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Headers headers = chain.request().headers();
        Request.Builder builder = chain.request().newBuilder().headers(headers);
        //登录态
        String token = TokenController.token;
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader(AUTHOR, "Bearer " + token);
        }

        //发起请求
        Response response = chain.proceed(builder.build());
        int code = response.code();
        //需要刷新 token
        if (code == ERROR_CODE) {
            TLog.e("token过期,进行重试");
            long startTime = System.currentTimeMillis();
            AtomicReference<String> newToken = new AtomicReference<>(null);
            //子线程请求刷新 token, 主线程阻塞等待,最多30秒
            Disposable subscribe = Observable.just("").map((Function<String, String>) s -> {
                //请求刷新
                String refreshToken = TokenController.refreshTokenCallback.onRefreshToken();
                if (refreshToken == null) {
                    refreshToken = "";
                }
                return refreshToken;
            }).subscribeOn(Schedulers.io()).subscribe(newToken::set, throwable -> {
                TLog.e("刷新token发生异常:" + throwable.getMessage(), throwable);
                newToken.set("");
            });

            int countDown = REFRESH_TOKEN_WAIT_TIME;
            do {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDown--;
            } while (newToken.get() == null && countDown > 0);
            //请求完成,或者等待完毕
            if (!subscribe.isDisposed()) {
                TLog.e("释放了刷新 token");
                subscribe.dispose();
            }

            long endTime = System.currentTimeMillis();
            TLog.e("refreshToken 花费" + (endTime - startTime) / 1000 + "s 新token为$newToken");
            //新token不为空,更新 token
            String newTokenValue = newToken.get();
            if (!TextUtils.isEmpty(newTokenValue)) {
                TokenController.token = newTokenValue;
                Request.Builder newBuilder = chain.request().newBuilder().headers(headers);
                //更新token
                newBuilder.addHeader(AUTHOR, "Bearer " + newTokenValue);
                return chain.proceed(builder.build());
            }
        }
        return response;
    }


}
