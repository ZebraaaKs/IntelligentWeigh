package com.example.chen.intelligentweigh.bean;

/**
 * @author chen
 * @date 2018/12/22.   0:34
 * description：
 */
public class CloudManage {


    /**
     * cunl : 12
     * chul : 2
     * taotai : 3
     * cl_total_weigh : 28902.90
     * cl_total_price : 1979848.65
     * cl_org_price : 2315377.80
     * cl_total_cb : -7178.60
     * cl_profit : -328350.55
     * KP : -618.72
     * KPI : -0.02
     * cl_pj_zz : -9.66
     */

    private String cunl;
    private String chul;
    private String taotai;
    private String cl_total_weigh;
    private String cl_total_price;
    private String cl_org_price;
    private String cl_total_cb;
    private String cl_profit;
    private String KP;
    private String KPI;
    private String cl_pj_zz;

    public String getCunl() {
        return cunl;
    }

    public void setCunl(String cunl) {
        this.cunl = cunl;
    }

    public String getChul() {
        return chul;
    }

    public void setChul(String chul) {
        this.chul = chul;
    }

    public String getTaotai() {
        return taotai;
    }

    public void setTaotai(String taotai) {
        this.taotai = taotai;
    }

    public String getCl_total_weigh() {
        return cl_total_weigh;
    }

    public void setCl_total_weigh(String cl_total_weigh) {
        this.cl_total_weigh = cl_total_weigh;
    }

    public String getCl_total_price() {
        return cl_total_price;
    }

    public void setCl_total_price(String cl_total_price) {
        this.cl_total_price = cl_total_price;
    }

    public String getCl_org_price() {
        return cl_org_price;
    }

    public void setCl_org_price(String cl_org_price) {
        this.cl_org_price = cl_org_price;
    }

    public String getCl_total_cb() {
        return cl_total_cb;
    }

    public void setCl_total_cb(String cl_total_cb) {
        this.cl_total_cb = cl_total_cb;
    }

    public String getCl_profit() {
        return cl_profit;
    }

    public void setCl_profit(String cl_profit) {
        this.cl_profit = cl_profit;
    }

    public String getKP() {
        return KP;
    }

    public void setKP(String KP) {
        this.KP = KP;
    }

    public String getKPI() {
        return KPI;
    }

    public void setKPI(String KPI) {
        this.KPI = KPI;
    }

    public String getCl_pj_zz() {
        return cl_pj_zz;
    }

    public void setCl_pj_zz(String cl_pj_zz) {
        this.cl_pj_zz = cl_pj_zz;
    }

    @Override
    public String toString() {
        return "CloudManage{" +
                "cunl='" + cunl + '\'' +
                ", chul='" + chul + '\'' +
                ", taotai='" + taotai + '\'' +
                ", cl_total_weigh='" + cl_total_weigh + '\'' +
                ", cl_total_price='" + cl_total_price + '\'' +
                ", cl_org_price='" + cl_org_price + '\'' +
                ", cl_total_cb='" + cl_total_cb + '\'' +
                ", cl_profit='" + cl_profit + '\'' +
                ", KP='" + KP + '\'' +
                ", KPI='" + KPI + '\'' +
                ", cl_pj_zz='" + cl_pj_zz + '\'' +
                '}';
    }
}
