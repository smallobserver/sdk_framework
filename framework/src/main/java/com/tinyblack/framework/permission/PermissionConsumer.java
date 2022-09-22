package com.tinyblack.framework.permission;


import io.reactivex.rxjava3.functions.Consumer;

/**
 * @author yubiao
 */
public class PermissionConsumer implements Consumer<Boolean> {
    private PermissionListener listener;

    public PermissionConsumer(PermissionListener listener) {
        this.listener = listener;
    }

    @Override
    public void accept(Boolean isAllow) throws Exception {
        if (listener != null) {
            if (isAllow == true) {
                listener.onAllowPermission();
            } else {
                listener.onRefusePermission();
            }
        }
    }


}
