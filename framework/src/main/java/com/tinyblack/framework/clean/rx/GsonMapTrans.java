package com.tinyblack.framework.clean.rx;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.functions.Function;


/**
 * gson解析bean的RX操作符
 *
 * @author liutao
 * @date 2018/3/26
 */

public class GsonMapTrans<T> implements ObservableTransformer<String, T> {
    private Gson gson;
    private Type type;

    public GsonMapTrans(Type type) {
        gson = new Gson();
        this.type = type;
    }

    @Override
    public ObservableSource<T> apply(Observable<String> upstream) {
        return upstream.map(s -> gson.fromJson(s, type));
    }
}
