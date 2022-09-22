package com.tinyblack.framework.util.eventBus;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * @author yubiao
 */
public class RxBus {
    private static volatile RxBus instance;

    private Subject<Object> rxBus;

    public RxBus() {
        rxBus = PublishSubject.create().toSerialized();
    }

    public static RxBus getRxBus() {
        if (instance == null) {
            //加上线程同步锁
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * 重写发送带Tag值得可以进行比对
     */
    public void post(Object event) {
        rxBus.onNext(event);
    }

    /**
     * 创建接受事件的方法
     */
    public <T> Observable<T> toEvent(Class<T> eventType) {
        return rxBus.ofType(eventType);
    }

    /**
     * 接收主线程回调
     *
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toEventOnMain(Class<T> eventType) {
        return rxBus.ofType(eventType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
