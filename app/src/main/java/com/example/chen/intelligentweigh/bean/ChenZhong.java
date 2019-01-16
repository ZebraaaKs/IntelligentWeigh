package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2019/1/13.   18:27
 * description：
 */
public class ChenZhong implements Serializable {

    /**
     * ID : 2
     * cattle_id : 1232
     * user_phone : 18014845521
     * weight : 120.45
     * wtime : 2018-11-08
     */

    private int ID;
    private String cattle_id;
    private String user_phone;
    private double weight;
    private String wtime;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCattle_id() {
        return cattle_id;
    }

    public void setCattle_id(String cattle_id) {
        this.cattle_id = cattle_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWtime() {
        return wtime;
    }

    public void setWtime(String wtime) {
        this.wtime = wtime;
    }
}
