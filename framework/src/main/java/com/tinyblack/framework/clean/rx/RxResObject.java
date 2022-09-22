package com.tinyblack.framework.clean.rx;

/**
 * @author yubiao
 */
public class RxResObject extends ResObject {
    public static String DELETE="delete";
    public static String REMOVE="remove";

    public RxResObject(String cmd){
        this.cmd=cmd;
    }

    private String cmd;

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }
}
