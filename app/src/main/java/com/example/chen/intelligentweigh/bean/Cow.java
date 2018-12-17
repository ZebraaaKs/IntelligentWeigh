package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/16.   17:15
 * description：服务器返回牛的信息
 */
public class Cow implements Serializable{


    /**
     * past_id : 17
     * name : NJcow
     * farmname : 江苏农牧
     * ID : 1122
     * father_id : 1234
     * mother_id : 5467
     * kind : 哥斯拉
     * sex : 公
     * birthday : 2017-12-15
     * entranceDay : 2017-12-13
     * registerDay : 2017-12-14
     * enterancePrice : 32.8
     * photo : null
     * area : 南京农牧
     * valid : 1
     */

    private int past_id;
    private String name;
    private String farmname;
    private String ID;
    private String father_id;
    private String mother_id;
    private String kind;
    private String sex;
    private String birthday;
    private String entranceDay;
    private String registerDay;
    private double enterancePrice;
    private String photo;
    private String area;
    private String valid;

    public int getPast_id() {
        return past_id;
    }

    public void setPast_id(int past_id) {
        this.past_id = past_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFarmname() {
        return farmname;
    }

    public void setFarmname(String farmname) {
        this.farmname = farmname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public String getMother_id() {
        return mother_id;
    }

    public void setMother_id(String mother_id) {
        this.mother_id = mother_id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEntranceDay() {
        return entranceDay;
    }

    public void setEntranceDay(String entranceDay) {
        this.entranceDay = entranceDay;
    }

    public String getRegisterDay() {
        return registerDay;
    }

    public void setRegisterDay(String registerDay) {
        this.registerDay = registerDay;
    }

    public double getEnterancePrice() {
        return enterancePrice;
    }

    public void setEnterancePrice(double enterancePrice) {
        this.enterancePrice = enterancePrice;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Cow{" +
                "past_id=" + past_id +
                ", name='" + name + '\'' +
                ", farmname='" + farmname + '\'' +
                ", ID='" + ID + '\'' +
                ", father_id='" + father_id + '\'' +
                ", mother_id='" + mother_id + '\'' +
                ", kind='" + kind + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", entranceDay='" + entranceDay + '\'' +
                ", registerDay='" + registerDay + '\'' +
                ", enterancePrice=" + enterancePrice +
                ", photo='" + photo + '\'' +
                ", area='" + area + '\'' +
                ", valid='" + valid + '\'' +
                '}';
    }
}
