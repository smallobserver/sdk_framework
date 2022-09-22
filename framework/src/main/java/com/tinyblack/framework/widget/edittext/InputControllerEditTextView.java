package com.tinyblack.framework.widget.edittext;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author yubiao
 */
public class InputControllerEditTextView extends AppCompatEditText {
    public InputControllerEditTextView(Context context) {
        super(context);
    }

    public InputControllerEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputControllerEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private InputFilter filter;

    public void setInputFilter(InputFilter filter) {
        this.filter = filter;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (filter != null) {
            String editable = text.toString();
            String str = filter.textFilter(editable);
            if (!editable.equals(str)) {
                setText(str);
                //设置新的光标所在位置
                setSelection(str.length());
            }
        }
    }

    /**
     * //    private static String stringFilter(String str) throws PatternSyntaxException {
     * //        // 只允许字母、数字和汉字
     * //        String regEx = "[^a-zA-Z0-9u4E00-u9FA5]";
     * //        Pattern p = Pattern.compile(regEx);
     * //        Matcher m = p.matcher(str);
     * //        return m.replaceAll("").trim();
     * //    }
     */

    public void setInputCharacterAndNumber() {
        setInputFilter(new InputFilter() {
            @Override
            public String textFilter(String str) throws PatternSyntaxException {
                // 只允许字母、数字和汉字
                String regEx = "[^a-zA-Z0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(str);
                return m.replaceAll("").trim();
            }
        });
    }
}
