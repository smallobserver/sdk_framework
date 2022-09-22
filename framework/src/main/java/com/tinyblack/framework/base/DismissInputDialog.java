package com.tinyblack.framework.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * @author yubiao
 */
public class DismissInputDialog extends Dialog {
    public DismissInputDialog(Context context) {
        super(context);
    }

    public DismissInputDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DismissInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void dismiss() {
        View view = this.getCurrentFocus();
        if (view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) this.getContext().getSystemService("input_method");
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        super.dismiss();
    }
}
