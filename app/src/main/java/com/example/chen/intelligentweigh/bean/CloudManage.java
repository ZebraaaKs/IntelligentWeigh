package com.example.chen.intelligentweigh.bean;

/**
 * @author chen
 * @date 2018/12/22.   0:34
 * description：
 */
public class CloudManage {

    /**
     * cattlenum : 8
     * rizengzhong : 0.00
     * zhesuanchengben : 0.00
     * profit : 0.00
     */

    private String cattlenum;
    private String rizengzhong;
    private String zhesuanchengben;
    private String profit;

    public String getCattlenum() {
        return cattlenum;
    }

    public void setCattlenum(String cattlenum) {
        this.cattlenum = cattlenum;
    }

    public String getRizengzhong() {
        return rizengzhong;
    }

    public void setRizengzhong(String rizengzhong) {
        this.rizengzhong = rizengzhong;
    }

    public String getZhesuanchengben() {
        return zhesuanchengben;
    }

    public void setZhesuanchengben(String zhesuanchengben) {
        this.zhesuanchengben = zhesuanchengben;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "CloudManage{" +
                "cattlenum='" + cattlenum + '\'' +
                ", rizengzhong='" + rizengzhong + '\'' +
                ", zhesuanchengben='" + zhesuanchengben + '\'' +
                ", profit='" + profit + '\'' +
                '}';
    }
}
