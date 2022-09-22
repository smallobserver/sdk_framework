package com.tinyblack.framework.mvp;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;

import com.tinyblack.framework.base.BaseFragment;
import com.tinyblack.framework.mvp.base.MvpView;

/**
 * @author yubiao
 */
public abstract class MvpFragment extends BaseFragment implements MvpView {

    @Nullable
    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }
}
