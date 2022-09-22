package com.tinyblack.framework.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tinyblack.framework.R;

/**
 * 初始化错误样式
 *
 * @author yubiao
 */
public class ErrorViewController {
    public static int ERROR_LAYOUT = R.layout.sdk_view_network_error;


    public static View createErrorLayout(View container) {
        FrameLayout errorLayout = container.findViewById(R.id.baseNetworkError);
        View inflate = LayoutInflater.from(container.getContext()).inflate(ERROR_LAYOUT, null, false);
        errorLayout.addView(inflate, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return errorLayout;
    }

}
