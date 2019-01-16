package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2019/1/13.   18:29
 * description：
 */
public class RiZeng implements Serializable {

    /**
     * dzengzhong : 0.0
     * wtime : 2018-11-08
     */

    private double dzengzhong;
    private String wtime;

    public double getDzengzhong() {
        return dzengzhong;
    }

    public void setDzengzhong(double dzengzhong) {
        this.dzengzhong = dzengzhong;
    }

    public String getWtime() {
        return wtime;
    }

    public void setWtime(String wtime) {
        this.wtime = wtime;
    }
}
