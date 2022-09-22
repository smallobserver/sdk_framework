package com.tinyblack.framework.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;


import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResControlOwner;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.lifecycle.LifecycleFrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bopeng
 */
public abstract class BaseLayout extends LifecycleFrameLayout implements ResControlOwner {

    protected View rootView;
    //view是否初始化成功
    protected boolean initView;

    public BaseLayout(Context context) {
        this(context, null);
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ResManager.instance().registerTo(RES_CONTROL);
        handleAttrs(context, attrs, defStyleAttr);
        init(false);
    }

    /**
     * 两种情况,当view被创建是，如果子类提供了layoutId,那么，initView是成功的
     * 当子类没有提供layoutId,那么在finishInflate结束的时候，应该也是成功的
     *
     * @param finishInflate
     */
    private void init(boolean finishInflate) {
        initView = initView();
        if (initView || finishInflate) {
            initData();
            initListener();
        }
    }

    /**
     * 当子类没有使用layoutId来提供view是，那么构造方法中是无法findview到对象的
     * 需要在onFinishInflate 执行的时候去初始化
     * onFinishInflate 执行时间在 这个类被findview的时候
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!initView) {
            init(true);
        }
    }

    protected void initData() {

    }

    protected void initListener() {

    }

    protected void handleAttrs(Context context, AttributeSet attributeSet, int defStyleAttr) {

    }

    /**
     * @return 如果返回了false, 那么initView会回调两次，第二次是在onFinishInflate
     * 如果返回了true,那么只会回调一次
     */
    protected boolean initView() {
        int resLayoutId = getLayout();
        if (resLayoutId != 0) {
            rootView = LayoutInflater.from(getContext()).inflate(getLayout(), this, false);
        }
        if (rootView == null) {
            return false;
        }
        addView(rootView);
        return true;
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        super.onDestroy(owner);
        releaseResource();
    }

    @LayoutRes
    protected int getLayout() {
        return 0;
    }

    private List<ResControl> RES_CONTROL = new ArrayList<>();

    @Override
    public void addResControl(@NonNull ResControl resControl) {
        RES_CONTROL.add(resControl);
    }

    @Override
    public void addResource(@NonNull Object o) {
        ResManager.instance().add(RES_CONTROL, o);
    }

    @Override
    public void releaseResource() {
        ResManager.instance().release(RES_CONTROL);
    }
}
