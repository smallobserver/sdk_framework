package com.tinyblack.framework.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 通用的拦截器
 *
 * @author PLU
 * @date 2016/6/8
 */
public class CommonInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder = interceptBuilder(builder);
        request = build(builder.build());
        Response response = chain.proceed(request);
        int code = response.code();
        //200-400为正确范围
        if (code >= 200 && code < 400) {
            onSuccess(response, request);
        } else {
            onFail(response, request);
        }
        return response;
    }

    protected void onSuccess(Response response, Request request) {
    }

    public Request.Builder interceptBuilder(Request.Builder builder) {
        return builder;
    }

    public Request build(Request request) {
        return request;
    }

    public void onFail(Response response, Request request) {
    }

}
