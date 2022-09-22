package com.tinyblack.framework.mvp.internal;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.tinyblack.framework.log.TLog;
import com.tinyblack.framework.mvp.base.MvpView;


/**
 * 和界面生命周期绑定的presenter
 *
 * @author liutao
 * @date 2018/3/20
 */

public class InternalLifecyclePresenter<V extends MvpView> extends InternalMvpBasePresenter<V> implements LifecycleOwner, LifecycleObserver {
    private Lifecycle registry;
    private V v;

    public InternalLifecyclePresenter(@NonNull Lifecycle registry, @NonNull V v) {
        this.registry = registry;
        this.v = v;
        registry.addObserver(this);
        attachView(v);
    }

    @Override
    public boolean isViewAttached() {
        Lifecycle lifecycle = this.getLifecycle();
        if (lifecycle == null) {
            return false;
        }
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return false;
        }
        if (getView() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Lifecycle getLifecycle() {
        return registry;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        detachView();
    }
}
