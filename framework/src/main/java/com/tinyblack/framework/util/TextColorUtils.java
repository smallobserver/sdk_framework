package com.tinyblack.framework.util;

import android.text.Html;
import android.widget.TextView;

/**
 * 替换字体颜色
 *
 * @author yubiao
 */
public class TextColorUtils {
    private static final String COLOR_FORMAT = "<font color='%1$s'>%2$s</font>";

    public static String colorFormatToString(String content, String color) {
        return String.format(COLOR_FORMAT, color, content);
    }

    /**
     * 更改部分字的颜色
     *
     * @return
     */
    public static String changeSomeWordColor(String content, String word, String color) {
        return content.replace(word, colorFormatToString(word, color));
    }

    /**
     * 设置内容
     *
     * @param textView
     * @param content
     */
    public static void setContent(TextView textView, String content) {
        textView.setText(Html.fromHtml(content));
    }

    /**
     * 设置内容
     *
     * @param textView  控件
     * @param content   原始文本内容
     * @param word  替换的文字
     * @param color  替换的颜色
     */
    public static void changeSomeWordColor(TextView textView, String content, String word, String color) {
        setContent(textView, changeSomeWordColor(content, word, color));
    }
}
