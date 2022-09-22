package com.tinyblack.framework.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.tinyblack.framework.R;
import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResControlOwner;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.util.AndroidComponentUiTools;
import com.tinyblack.framework.util.systembar.Eyes;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * Created by ljd
 *
 * @author yubiao
 */

public abstract class BaseDialogFragment extends DialogFragment implements ResControlOwner {

    private boolean hasAttach;
    private boolean mIsFullScreen = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.sdk_framework_android_component_dialogStyle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new DismissInputDialog(getActivity(), getTheme());
        //关闭dialog隐藏键盘
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = getInflateView();
        if (v == null) {
            v = View.inflate(getContext(), getLayout(), null);
        }
        initView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Activity activity = getActivity();
            Window window = activity != null ? activity.getWindow() : null;
            if (window != null) {
                int color = window.getStatusBarColor();
                if (Color.WHITE == color) {
                    Eyes.setStatusBarLightMode(window, true);
                }
            }
        }
    }

    public void setWindowFullscreen() {
        if (mIsFullScreen) {
            final Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            window.addFlags(WindowManager.LayoutParams.SCREEN_ORIENTATION_CHANGED);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(-1, -2);
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        setWindowFullscreen();
        initData(savedInstanceState);
        initListener();
        hasAttach = true;
    }

    protected boolean hasAttachView() {
        return hasAttach;
    }

    protected void initListener() {
    }

    protected void initData(Bundle bundle) {

    }

    protected void initView(View v) {
        if (isHideNavigateBar()) {
            if (getDialog() != null && getDialog().getWindow() != null) {
                AndroidComponentUiTools.hideNavigateBar(getDialog().getWindow().getDecorView());
            }
        }
    }

    @LayoutRes
    protected abstract int getLayout();

    public View getInflateView() {
        return null;
    }

    protected void onDismiss() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isNeedDismiss()) {
            dismissAllowingStateLoss();
        }
    }

    protected boolean isNeedDismiss() {
        return false;
    }

    protected boolean isHideNavigateBar() {
        if (getActivity() == null) {
            return false;
        }
        return getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    protected void setFullScreen(boolean mIsFullScreen) {
        this.mIsFullScreen = mIsFullScreen;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException ignore) {
            //  容错处理,不做操作
        }

    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (getActivity() != null && getActivity().isFinishing()) {
            return;
        }
        super.dismissAllowingStateLoss();
    }
}
