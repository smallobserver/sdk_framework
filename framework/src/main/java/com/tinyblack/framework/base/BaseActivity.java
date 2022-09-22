package com.tinyblack.framework.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.FragmentActivity;

import com.tinyblack.framework.R;
import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResControlOwner;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.controller.ClickController;
import com.tinyblack.framework.controller.ErrorViewController;
import com.tinyblack.framework.widget.SDKFrameworkTitleBar;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * @author yubiao
 */

public abstract class BaseActivity extends FragmentActivity implements ResControlOwner<Object> {

    private ImageView rightIcon;
    protected View errorLayout;
    protected SDKFrameworkTitleBar SDKFrameworkTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView(savedInstanceState);
        initView();
        initData(savedInstanceState);
        initListener();
    }

    public abstract @LayoutRes
    int getLayout();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData(Bundle savedInstanceState);

    public void initContentView(Bundle savedInstanceState) {
        View v = View.inflate(this, R.layout.sdk_layout_base, null);
        errorLayout = ErrorViewController.createErrorLayout(v);
        FrameLayout content = v.findViewById(R.id.baseFlContainer);
        SDKFrameworkTitleBar = v.findViewById(R.id.baseTitleBar);
        rightIcon = SDKFrameworkTitleBar.getRightIcon();
        content.addView(View.inflate(this, getLayout(), null));
        v.findViewById(R.id.btnRetry).setOnClickListener(v1 -> {
            //过滤掉快速连点
            if (ClickController.isFastClick()) {
                return;
            }
            onReload();
            errorLayout.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        });
        if (useTitleBar()) {
            SDKFrameworkTitleBar.setVisibility(View.VISIBLE);
            SDKFrameworkTitleBar.setTitle(getPageName());
            int result = 24;
            int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resId > 0) {
                result = getResources().getDimensionPixelSize(resId);
            } else {
                result = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, result, Resources.getSystem().getDisplayMetrics());
            }
            SDKFrameworkTitleBar.setPadding(0, result, 0, 0);
            SDKFrameworkTitleBar.setBackListener(imageView -> {
                onBackPressed();
                return null;
            });
        }
        setContentView(v);
    }

    protected boolean useTitleBar() {
        return false;
    }

    protected void onReload() {
    }

    protected String getPageName() {
        return "";
    }

    protected ImageView getRightIcon() {
        return rightIcon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseResource();
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
