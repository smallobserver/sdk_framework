package com.tinyblack.framework.widget.edittext;

import java.util.regex.PatternSyntaxException;

/**
 * @author yubiao
 */
public interface InputFilter {
    /**
     * 过滤输入内存
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    String textFilter(String str) throws PatternSyntaxException;
}
