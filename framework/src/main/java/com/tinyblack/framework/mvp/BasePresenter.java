package com.tinyblack.framework.mvp;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;


import com.tinyblack.framework.controller.DisposeController;
import com.tinyblack.framework.mvp.base.MvpView;
import com.tinyblack.framework.mvp.internal.InternalLifecyclePresenter;
import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.clean.rx.ResObject;
import com.tinyblack.framework.clean.rx.SuperResControlOwner;

import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;


/**
 * 基础presenter
 *
 * @author yubiao
 */

public class BasePresenter<V extends MvpView> extends InternalLifecyclePresenter<V> implements SuperResControlOwner<Object> {
    protected final String TAG = getClass().getSimpleName();

    private final List<ResControl> RES_CONTROL = new LinkedList<>();

    private final DisposeController disposeController = new DisposeController();

    public BasePresenter(@NonNull Lifecycle registry, @NonNull V v) {
        super(registry, v);
        ResManager.instance().registerTo(RES_CONTROL);
    }

    @Override
    public void addResControl(@NonNull ResControl resControl) {
        RES_CONTROL.add(resControl);
    }

    @Override
    public void addResource(Object o) {
        ResManager.instance().add(RES_CONTROL, o);
    }

    @Override
    public void releaseResource() {
        ResManager.instance().release(RES_CONTROL);
    }

    public void controlDisposable(Disposable disposable) {
        disposeController.controlDisposable(disposable);
    }

    public void controlDisposable(Disposable disposable, String tag) {
        disposeController.controlDisposable(disposable, tag);
    }

    public void releaseDisposable(String tag) {
        disposeController.releaseDisposable(tag);
    }

    public void releaseDisposable(Disposable disposable) {
        disposeController.releaseDisposable(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseResource();
        disposeController.release();
    }


    @Override
    public boolean isViewAttached() {
        return super.isViewAttached();
    }

    @Override
    public void addResource(ResObject resObject) {
        ResManager.instance().add(RES_CONTROL, resObject);
    }

    @Override
    public void releaseResource(ResObject resObject) {
        ResManager.instance().release(RES_CONTROL, resObject);
    }
}
