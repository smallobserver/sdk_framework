package com.tinyblack.framework.mvp.internal;

import androidx.annotation.Nullable;

import com.tinyblack.framework.mvp.MvpPresenter;
import com.tinyblack.framework.mvp.base.MvpView;


/**
 * 此处使用强引用,需要注意释放时机
 *
 * @author liutao
 * @date 2018/3/20
 */

public abstract class InternalMvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {
    protected V mView;


    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Nullable
    public V getView() {
        return mView;
    }
}
