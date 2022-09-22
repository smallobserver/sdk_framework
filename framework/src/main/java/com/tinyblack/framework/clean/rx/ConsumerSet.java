package com.tinyblack.framework.clean.rx;


import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * @author liutao
 * @date 2018/3/8
 */

public abstract class ConsumerSet<T> {
    /**
     * 这里接收数据项
     */
    Consumer<T> onNextConsumer = this::onSafeNext;

    /**
     * 安全解析
     *
     * @param t
     */
    protected abstract void onSafeNext(T t);

    /**
     * 这里接收onError
     */
    Consumer<Throwable> onErrorConsumer = this::onSafeError;

    /**
     * @param throwable
     */
    protected abstract void onSafeError(Throwable throwable);

    /**
     * 这里接收onComplete
     */
    Action completeAction = this::onSafeComplete;

    protected abstract void onSafeComplete();


    public Action getCompleteAction() {
        return completeAction;
    }

    public Consumer<T> getOnNextConsumer() {
        return onNextConsumer;
    }

    public Consumer<Throwable> getOnErrorConsumer() {
        return onErrorConsumer;
    }
}
