package com.tinyblack.framework.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.tinyblack.framework.R;
import com.tinyblack.framework.clean.rx.ResControl;
import com.tinyblack.framework.clean.rx.ResControlOwner;
import com.tinyblack.framework.clean.rx.ResManager;
import com.tinyblack.framework.controller.ErrorViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yubiao
 */

public abstract class BaseFragment extends Fragment implements ResControlOwner {

    private boolean isFirstVisible = true;
    protected final String TAG = getClass().getSimpleName();
    protected View errorLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getInflateView();
        if (v == null) {
            v = View.inflate(getContext(), R.layout.sdk_layout_base, null);
            errorLayout = ErrorViewController.createErrorLayout(v);
            FrameLayout content = v.findViewById(R.id.baseFlContainer);
            content.addView(View.inflate(getContext(), getLayout(), null));
            v.findViewById(R.id.btnRetry).setOnClickListener(v1 -> {
                onReload();
                errorLayout.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
            });
        }
        return v;
    }

    protected View getInflateView() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData(savedInstanceState);
        initListener();
    }

    protected abstract int getLayout();

    protected void onReload() {
    }

    protected void initView(View view) {
    }

    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    protected void initListener() {
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
}
