package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/6.   20:07
 * description：牧场分区
 */
public class Area implements Serializable{


    /**
     * area : 艾欧尼亚
     * num : 3
     */

    private String area;
    private String num;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Area{" +
                "area='" + area + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
