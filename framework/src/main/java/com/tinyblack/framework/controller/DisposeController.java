package com.tinyblack.framework.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author yubiao
 */
public class DisposeController {

    private final List<Disposable> DISPOSABLE_CONTROL = new LinkedList<>();
    private final Map<String, Disposable> TAG_CONTROL = new HashMap<>();

    public void controlDisposable(Disposable disposable) {
        DISPOSABLE_CONTROL.add(disposable);
    }

    public void controlDisposable(Disposable disposable, String tag) {
        releaseDisposable(tag);
        TAG_CONTROL.put(tag, disposable);
    }

    public void releaseDisposable(Disposable disposable) {
        DISPOSABLE_CONTROL.remove(disposable);
        checkDisposable(disposable);
    }

    public void releaseDisposable(String tag) {
        Disposable disposable = TAG_CONTROL.remove(tag);
        checkDisposable(disposable);
    }


    public void release() {
        if (DISPOSABLE_CONTROL.size() > 0) {
            for (Disposable disposable : DISPOSABLE_CONTROL) {
                checkDisposable(disposable);
            }
            DISPOSABLE_CONTROL.clear();
            TAG_CONTROL.clear();
        }
    }

    private void checkDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
