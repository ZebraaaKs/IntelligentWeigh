package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/15.   16:45
 * description：录入新牛
 */
public class NewCow implements Serializable{
    private String houseId;
    private String houseName;
    private String cowName;
    private String cowType;
    private String cowSex;
    private String cowBrith;
    private String cowHouseDate;
    private String cowRegisterDate;
    private String cowPrice;
    private String cowFatherId;
    private String cowMontherId;
    private String cowId;
    private String cowTypeId;

    public NewCow() {
    }

    public NewCow(String cowTypeId,String houseId,String houseName, String cowName, String cowType, String cowSex, String cowBrith, String cowHouseDate, String cowRegisterDate, String cowPrice, String cowFatherId, String cowMontherId, String cowId) {
        this.cowTypeId = cowTypeId;
        this.houseId = houseId;
        this.houseName = houseName;
        this.cowName = cowName;
        this.cowType = cowType;
        this.cowSex = cowSex;
        this.cowBrith = cowBrith;
        this.cowHouseDate = cowHouseDate;
        this.cowRegisterDate = cowRegisterDate;
        this.cowPrice = cowPrice;
        this.cowFatherId = cowFatherId;
        this.cowMontherId = cowMontherId;
        this.cowId = cowId;

    }

    public String getCowTypeId() {
        return cowTypeId;
    }

    public void setCowTypeId(String cowTypeId) {
        this.cowTypeId = cowTypeId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getCowName() {
        return cowName;
    }

    public void setCowName(String cowName) {
        this.cowName = cowName;
    }

    public String getCowType() {
        return cowType;
    }

    public void setCowType(String cowType) {
        this.cowType = cowType;
    }

    public String getCowSex() {
        return cowSex;
    }

    public void setCowSex(String cowSex) {
        this.cowSex = cowSex;
    }

    public String getCowBrith() {
        return cowBrith;
    }

    public void setCowBrith(String cowBrith) {
        this.cowBrith = cowBrith;
    }

    public String getCowHouseDate() {
        return cowHouseDate;
    }

    public void setCowHouseDate(String cowHouseDate) {
        this.cowHouseDate = cowHouseDate;
    }

    public String getCowRegisterDate() {
        return cowRegisterDate;
    }

    public void setCowRegisterDate(String cowRegisterDate) {
        this.cowRegisterDate = cowRegisterDate;
    }

    public String getCowPrice() {
        return cowPrice;
    }

    public void setCowPrice(String cowPrice) {
        this.cowPrice = cowPrice;
    }

    public String getCowFatherId() {
        return cowFatherId;
    }

    public void setCowFatherId(String cowFatherId) {
        this.cowFatherId = cowFatherId;
    }

    public String getCowMontherId() {
        return cowMontherId;
    }

    public void setCowMontherId(String cowMontherId) {
        this.cowMontherId = cowMontherId;
    }

    public String getCowId() {
        return cowId;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    @Override
    public String toString() {
        return "NewCow{" +
                "houseId='" + houseId + '\'' +
                ", houseName='" + houseName + '\'' +
                ", cowName='" + cowName + '\'' +
                ", cowType='" + cowType + '\'' +
                ", cowSex='" + cowSex + '\'' +
                ", cowBrith='" + cowBrith + '\'' +
                ", cowHouseDate='" + cowHouseDate + '\'' +
                ", cowRegisterDate='" + cowRegisterDate + '\'' +
                ", cowPrice='" + cowPrice + '\'' +
                ", cowFatherId='" + cowFatherId + '\'' +
                ", cowMontherId='" + cowMontherId + '\'' +
                ", cowId='" + cowId + '\'' +
                ", cowTypeId='" + cowTypeId + '\'' +
                '}';
    }
}
