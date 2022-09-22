package com.tinyblack.framework.clean.rx;


import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * rx 控制器,负责rx的释放
 *
 * @author liutao
 * @date 2018/3/20
 */

public class RxControl implements ResControl {
    private CompositeDisposable compositeDisposable;


    @Override
    public boolean canDeal(ResObject disposable) {
        if (disposable == null) {
            return false;
        }
        Object o = disposable.getResource();
        if (o == null) {
            return false;
        }
        if (o instanceof Class) {
            if (o == Disposable.class) {
                return true;
            }
        }
        if (o instanceof Disposable) {
            return true;
        }

        return false;
    }

    @Override
    public void addResource(ResObject disposable) {
        if (this.compositeDisposable == null) {
            this.compositeDisposable = new CompositeDisposable();
        }
        if (disposable.getResource() instanceof Disposable) {
            this.compositeDisposable.add((Disposable) disposable.getResource());
        }
    }

    @Override
    public void releaseResource() {
        if (this.compositeDisposable != null) {
            this.compositeDisposable.dispose();
            this.compositeDisposable = null;
        }
    }

    @Override
    public void releaseResource(ResObject resObject) {
        if (resObject == null) {
            return;
        }
        Object o = resObject.getResource();
        String cmd = RxResObject.REMOVE;
        if (resObject instanceof RxResObject) {
            String key = ((RxResObject) resObject).getCmd();
            //只删除
            if (RxResObject.DELETE.equals(key)) {
                cmd = key;
            }
        }
        if (o instanceof Disposable) {
            if (this.compositeDisposable != null) {
                if (cmd.equals(RxResObject.DELETE)) {
                    this.compositeDisposable.delete((Disposable) o);
                } else {
                    this.compositeDisposable.remove((Disposable) o);
                }
            }
        }
    }

}
