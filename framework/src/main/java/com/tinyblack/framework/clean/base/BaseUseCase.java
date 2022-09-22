package com.tinyblack.framework.clean.base;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.tinyblack.framework.clean.RepositoryMgr;
import com.tinyblack.framework.clean.rx.ConsumerSet;
import com.tinyblack.framework.clean.rx.ErrorOrCompleteAction;
import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResControlOwner;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.clean.rx.ResObject;
import com.tinyblack.framework.clean.rx.RxResObject;
import com.tinyblack.framework.clean.rx.SuperResControlOwner;
import com.tinyblack.framework.log.TLog;
import com.tinyblack.framework.util.eventBus.LoadingEvent;
import com.tinyblack.framework.util.eventBus.RxBus;
import com.tinyblack.framework.widget.Loading;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 必带参数ResControlOwner,一般是由使用UseCaseGroup的类来实现
 * 当然,如果你希望手动管理usecase的添加，可以传NULL
 *
 * @param <D> 数据 存储库 传递
 * @param <R> 请求参数
 * @param <C> 数据 回调
 * @param <T> 返回体
 * @author yubiao
 */
public abstract class BaseUseCase<D extends DataRepository, R extends BaseReqParameter, C extends BaseCallback, T> implements UseCase<R, C, T>, SuperResControlOwner {
    protected D dataRepository;
    private List<ResControl> RES = new ArrayList<>();

    public BaseUseCase(@Nullable ResControlOwner<Object> controlOwner) {
        if (controlOwner != null) {
            controlOwner.addResource(this);
        }
        Class<DataRepository> clazz = (Class<DataRepository>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        dataRepository = (D) RepositoryMgr.instance().getRepository(clazz);
        ResManager.instance().registerTo(RES);
    }

    @Override
    public ObservableTransformer<T, T> buildTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void release() {
        releaseResource();
    }

    @Override
    public void execute(R req, C callback) {
        execute(buildTransformer(), req, callback, true);
    }

    @Override
    public void execute(R req, C callback, boolean showLoading) {
        execute(buildTransformer(), req, callback, showLoading);
    }


    private void execute(ObservableTransformer<T, T> transformer, R req, C callback, boolean showLoading) {
        ConsumerSet<T> consumerSet = buildSubscriber(req, callback);
        Consumer<T> onNextConsumer = consumerSet.getOnNextConsumer();
        Consumer<Throwable> onErrorConsumer = consumerSet.getOnErrorConsumer();
        Action completeAction = consumerSet.getCompleteAction();
        ErrorOrCompleteAction t = getErrorOrCompleteAction(showLoading);
        Disposable disposable = buildObservable(req, callback)
                .compose(transformer)
                .doOnSubscribe(disposable1 -> {
                    if(showLoading){
                        RxBus.getRxBus().post(new LoadingEvent(true,null));
                    }
                })
                .doOnComplete(t)
                .doOnError(t)
                .subscribe(onNextConsumer, onErrorConsumer, completeAction);
        t.setDisposable(disposable);

        addResource(disposable);
    }

    protected ErrorOrCompleteAction getErrorOrCompleteAction(boolean showLoading) {
        return new ErrorOrCompleteAction() {
            @Override
            public void run(Disposable disposable) {
                if(showLoading){
                    RxBus.getRxBus().post(new LoadingEvent(false,null));
                }
                if (disposable != null) {
                    releaseResource(new RxResObject(RxResObject.DELETE));
                }
            }
        };
    }


    @Override
    public void addResControl(ResControl resControl) {
        RES.add(resControl);
    }

    @Override
    public void addResource(Object o) {
        ResManager.instance().add(RES, o);
    }

    @Override
    public void releaseResource() {
        ResManager.instance().release(RES);
    }


    @Override
    public void addResource(ResObject resObject) {
        ResManager.instance().add(RES, resObject);
    }

    @Override
    public void releaseResource(ResObject object) {
        ResManager.instance().release(RES, object);
    }
}
