package com.tinyblack.framework.widget.edittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * @author yubiao
 */
public class EnterNoEditTextView extends InputControllerEditTextView implements TextView.OnEditorActionListener {

    public EnterNoEditTextView(Context context) {
        super(context);
        setOnEditorActionListener(this);
    }

    public EnterNoEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnEditorActionListener(this);
    }

    public EnterNoEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }
}
