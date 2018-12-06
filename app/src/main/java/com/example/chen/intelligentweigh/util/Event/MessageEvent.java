package com.example.chen.intelligentweigh.util.Event;

/**
 * @author chen
 * @date 2018/12/6.   13:17
 * description：EventBus自定义消息
 */
public class MessageEvent{
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

