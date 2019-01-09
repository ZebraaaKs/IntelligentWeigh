package com.example.chen.intelligentweigh.bean;

import org.litepal.crud.LitePalSupport;

/**
 * author : chen
 * date   : 2018/11/27  12:39
 * desc   : 用户pojo类  同时使用sqllite的数据库框架LitePal进行数据的存储
 */
public class User extends LitePalSupport{
    /**
     * isSuccess : 1
     * phone : 15162249219
     * password : 123456
     * name : xiaochen001
     * age : null
     * sex : null
     * touxiang : null
     * farmids : null
     * jurisdiction : 0
     */

    private String isSuccess;
    private String phone;
    private String password;
    private String name;
    private String age;
    private String sex;
    private String touxiang;
    private String farmids;
    private String jurisdiction;
    private String farmid;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getFarmid() {
        return farmid;
    }

    public void setFarmid(String farmid) {
        this.farmid = farmid;
    }

    @Override
    public String toString() {
        return "User{" +
                "isSuccess='" + isSuccess + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", touxiang='" + touxiang + '\'' +
                ", farmids='" + farmids + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                ", farmid='" + farmid + '\'' +
                '}';
    }
}
