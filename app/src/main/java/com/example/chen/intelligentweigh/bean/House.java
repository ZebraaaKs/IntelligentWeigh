package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/6.   15:55
 * description： 牧场pojo类   areas 为分区名字
 */
public class House implements Serializable{

    /**
     * ID : 7
     * name : Muc
     * areas : null
     */

    private String ID;
    private String name;
    private String areas;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreas() {
        return areas;
    }

    public void setAreas(String areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "House{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", areas='" + areas + '\'' +
                '}';
    }
}
