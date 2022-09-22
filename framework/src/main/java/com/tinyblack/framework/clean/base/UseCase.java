package com.tinyblack.framework.clean.base;


import com.tinyblack.framework.clean.rx.SimpleSubscriber;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;

/**
 * @author: liutao
 */
public interface UseCase<R extends BaseReqParameter, C extends BaseCallback, T> {

    /**
     * 创建转换
     *
     * @return
     */
    ObservableTransformer<T, T> buildTransformer();

    /**
     * 创建请求
     *
     * @param param
     * @param callback
     * @return
     */
    Observable<T> buildObservable(R param, C callback);

    /**
     * 创建监听
     *
     * @param param
     * @param callback
     * @return
     */
    SimpleSubscriber<T> buildSubscriber(R param, C callback);

    /**
     * 释放
     */
    void release();

    /**
     * 执行
     *
     * @param param
     * @param callback
     */
    void execute(R param, C callback);

    void execute(R param, C callback,boolean showLoading);
}
