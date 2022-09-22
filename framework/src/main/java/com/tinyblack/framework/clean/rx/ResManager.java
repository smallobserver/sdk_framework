package com.tinyblack.framework.clean.rx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * 资源管理器
 *
 * @author yubiao
 */

public class ResManager {
    private final static ResManager INSTANCE = new ResManager();
    private ResFactory resFactory;

    public static ResManager instance() {
        return INSTANCE;
    }

    /**
     * 设置通用的资源控制器
     */
    public void setCommonResFactory(@NonNull ResFactory resFactory) {
        this.resFactory = resFactory;
    }

    @Nullable
    public ResFactory getResFactory() {
        return resFactory;
    }

    /**
     * 添加默认的资源控制器
     *
     * @param resControls
     */
    public void registerTo(@NonNull List<ResControl> resControls) {
        if (getResFactory() != null) {
            resControls.addAll(resFactory.createResControl());
        }
    }


    public boolean add(@NonNull List<ResControl> resControls, @NonNull Object value) {
        ResObject resObject;
        if (!(value instanceof ResObject)) {
            resObject = new ResObject();
            resObject.setResource(value);
        } else {
            resObject = (ResObject) value;
        }
        for (ResControl control : resControls) {
            if (control != null) {
                if (control.canDeal(resObject)) {
                    control.addResource(resObject);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 释放所有的ResControl
     *
     * @param resControls
     */
    public void release(@NonNull List<ResControl> resControls) {
        for (ResControl resControl : resControls) {
            if (resControl != null) {
                resControl.releaseResource();
            }
        }
    }

    /**
     * 移除指定的对象
     *
     * @param resControls
     * @param value
     */
    public void release(@NonNull List<ResControl> resControls, @NonNull ResObject value) {
        for (ResControl resControl : resControls) {
            if (resControl != null && resControl.canDeal(value)) {
                resControl.releaseResource(value);
            }
        }
    }

    public interface ResFactory {
        /**
         * 创建资源控制器
         *
         * @return
         */
        @NonNull
        List<ResControl> createResControl();
    }
}
