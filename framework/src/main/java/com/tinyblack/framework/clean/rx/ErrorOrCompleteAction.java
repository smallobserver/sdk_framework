package com.tinyblack.framework.clean.rx;


import org.jetbrains.annotations.Nullable;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * 用来做onError 和onComplete的统一回调,当前只针对usecase
 *
 * @author yubiao
 */
public abstract class ErrorOrCompleteAction<T extends Throwable> implements Consumer<T>, Action {
    private Disposable disposable;

    public abstract void run(@Nullable Disposable disposable);

    public void setDisposable(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void accept(T t) throws Exception {
        run(disposable);
    }

    @Override
    public void run() throws Exception {
        run(disposable);
    }
}
