package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 *
 * 人员管理
 * */
public class People implements Serializable{

    /**
     * phone : 15162242863
     * name : 123
     * age : null
     * sex : null
     * touxiang : null
     * farmids : null
     * jurisdiction : 1
     */

    private String phone;
    private String name;
    private String age;
    private String sex;
    private String touxiang;
    private String farmids;
    private String jurisdiction;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getFarmids() {
        return farmids;
    }

    public void setFarmids(String farmids) {
        this.farmids = farmids;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    @Override
    public String toString() {
        return "People{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", touxiang='" + touxiang + '\'' +
                ", farmids='" + farmids + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                '}';
    }
}
