package com.tinyblack.framework.base;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tinyblack.framework.mvp.MvpActivity;

/**
 * @author yubiao
 */
public abstract class EditTextActivity extends MvpActivity {

    /**
     * 解决点击EditText点击外部区域软键盘隐藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //需要隐藏软键盘
            if (isShouldHideInput(v, ev)) {
                hideKeyboard(v);
                clearEditTextCursor();
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 隐藏小键盘
     */
    protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showKeyBoard(EditText v) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(v, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 清除EditText光标
     */
    protected abstract void clearEditTextCursor();

    /**
     * 判断当前点击的位置是否为EditText
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        //如果点击的view是EditText
        if (v instanceof EditText) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
