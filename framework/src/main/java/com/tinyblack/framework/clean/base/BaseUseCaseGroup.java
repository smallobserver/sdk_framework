package com.tinyblack.framework.clean.base;

import com.tinyblack.framework.clean.rx.ErrorOrCompleteAction;
import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResControlOwner;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.clean.rx.RxResObject;
import com.tinyblack.framework.util.eventBus.LoadingEvent;
import com.tinyblack.framework.util.eventBus.RxBus;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 组合usecase
 * 第一个参数是ResControlOwner,一般是由使用UseCaseGroup的类来实习
 *
 * @author yubiao
 */
public class BaseUseCaseGroup<R extends BaseReqParameter, C extends BaseCallback> implements UseCaseGroup<R, C>, ResControlOwner {
    protected C callback;
    protected R req;

    private List<ResControl> RES = new ArrayList<>();


    public BaseUseCaseGroup(@Nullable ResControlOwner<Object> resControlOwner, Object... objects) {
        if (resControlOwner != null) {
            resControlOwner.addResource(this);
        }
        if (objects != null) {
            for (Object o : objects) {
                addResource(o);
            }
        }
    }

    protected ErrorOrCompleteAction getErrorOrCompleteAction() {
        return new ErrorOrCompleteAction() {
            @Override
            public void run(Disposable disposable) {
                if (disposable != null) {
                    releaseResource();
                }
            }
        };
    }

    @Override
    public void release() {
        releaseResource();
    }

    @Override
    public void addResControl(ResControl resControl) {
        ResManager.instance().add(RES, resControl);
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
    public void execute(R req, C callback) {
        this.callback = callback;
        this.req = req;
    }

    @Override
    public void execute(R req) {
        this.req = req;
    }

    @Override
    public void setRspCallback(C callback) {
        this.callback = callback;
    }
}
