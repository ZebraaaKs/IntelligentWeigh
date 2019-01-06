package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2019/1/7.   0:35
 * description：
 */
public class CardData implements Serializable{

    /**
     * rizengzhong : 0.00
     * cunl : 0
     * chul : 0
     * taotai : 0
     */

    private String rizengzhong;
    private String cunl;
    private String chul;
    private String taotai;

    public String getRizengzhong() {
        return rizengzhong;
    }

    public void setRizengzhong(String rizengzhong) {
        this.rizengzhong = rizengzhong;
    }

    public String getCunl() {
        return cunl;
    }

    public void setCunl(String cunl) {
        this.cunl = cunl;
    }

    public String getChul() {
        return chul;
    }

    public void setChul(String chul) {
        this.chul = chul;
    }

    public String getTaotai() {
        return taotai;
    }

    public void setTaotai(String taotai) {
        this.taotai = taotai;
    }
}
