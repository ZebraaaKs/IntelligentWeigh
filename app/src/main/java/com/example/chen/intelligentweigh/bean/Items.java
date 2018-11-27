package com.example.chen.intelligentweigh.bean;

/**
 * author : chen
 * date   : 2018/11/27  12:49
 * desc   : 自定义Gridview的item类
 */
public class Items {

    private int imageId;
    private String text;

    public Items() {
    }

    public Items(int imageId, String text) {
        this.imageId = imageId;
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
