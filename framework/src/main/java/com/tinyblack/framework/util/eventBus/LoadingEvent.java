package com.tinyblack.framework.util.eventBus;

public class LoadingEvent {
    public boolean isShow;
    public String message;

    public LoadingEvent(boolean isShow, String message) {
        this.isShow = isShow;
        this.message = message;
    }
}
