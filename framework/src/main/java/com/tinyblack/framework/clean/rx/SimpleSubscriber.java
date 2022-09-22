package com.tinyblack.framework.clean.rx;

/**
 * 新增onSafeXXX方法，防止getView为Null
 * 使用simpleSubsci时，请重写onSafeXXX系列的方法。
 *
 * 如果在onNext,Error,Completed中需要调用getView
 * 使用带Object参数的构造方法,
 * date : 07/14
 * @author yubiao
 */
public class SimpleSubscriber<T> extends ConsumerSet<T> {

    public SimpleSubscriber(){

    }

    @Override
    public void onSafeError(Throwable e){

    }

    @Override
    public void onSafeNext(T t){

    }


    @Override
    public void onSafeComplete(){

    }
}
