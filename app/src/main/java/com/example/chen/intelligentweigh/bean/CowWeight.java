package com.example.chen.intelligentweigh.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/24.   22:54
 * description：
 */
public class CowWeight extends LitePalSupport {
    private String weid;
    private String weight;
    private String phone;
    private String date;
    private String kind;

    public String getWeid() {
        return weid;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setWeid(String weid) {
        this.weid = weid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CowWeight{" +
                "weid='" + weid + '\'' +
                ", weight='" + weight + '\'' +
                ", phone='" + phone + '\'' +
                ", date='" + date + '\'' +
                ", kind='" + kind + '\'' +
                '}';
    }
}
