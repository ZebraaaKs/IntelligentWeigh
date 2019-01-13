package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2019/1/13.   12:46
 * description：
 */
public class CattleFramKind implements Serializable{

    /**
     * cunl : 10
     * chul : 2
     * taotai : 2
     */

    private String cunl;
    private String chul;
    private String taotai;

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

    @Override
    public String toString() {
        return "CattleFramKind{" +
                "cunl='" + cunl + '\'' +
                ", chul='" + chul + '\'' +
                ", taotai='" + taotai + '\'' +
                '}';
    }
}
