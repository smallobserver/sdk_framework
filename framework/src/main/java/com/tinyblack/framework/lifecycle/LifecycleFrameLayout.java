package com.tinyblack.framework.lifecycle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.tinyblack.framework.log.TLog;


/**
 * @author liutao
 * @date 2018/3/21
 */

public class LifecycleFrameLayout extends FrameLayout implements DefaultLifecycleObserver {
    private Lifecycle mRegistry;


    public LifecycleFrameLayout(Context context) {
        this(context, null);
    }

    public LifecycleFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LifecycleFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void registryObserver(@NonNull Lifecycle lifecycle) {
        this.mRegistry = lifecycle;
        this.mRegistry.addObserver(this);
    }

    public void onCreate() {
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        TLog.i("LifecycleObserver the mView  onDestroy : " + getClass().getSimpleName());
        if (getLifecycle() != null) {
            getLifecycle().removeObserver(this);
        }
    }

    @Nullable
    public Lifecycle getLifecycle() {
        return this.mRegistry;
    }

}
