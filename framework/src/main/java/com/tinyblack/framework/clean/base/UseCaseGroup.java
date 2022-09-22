package com.tinyblack.framework.clean.base;

/**
 * @author yubiao
 */
public interface UseCaseGroup<R extends BaseReqParameter, C extends BaseCallback> {

    void release();

    void execute(R req, C callback);

    void execute(R req);

    void setRspCallback(C callback);

}
