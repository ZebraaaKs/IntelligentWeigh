package com.example.chen.intelligentweigh.bean;

import java.io.Serializable;

/**
 * @author chen
 * @date 2018/12/17.   22:47
 * description：称重
 */
public class WeighData implements Serializable {


    /**
     * cz : [{"ID":2,"cattle_id":"1232","user_phone":"18014845521","weight":120.45,"wtime":"2018-11-08"},{"ID":3,"cattle_id":"1232","user_phone":"18014845521","weight":140.15,"wtime":"2018-11-09"},{"ID":4,"cattle_id":"1232","user_phone":"18014845521","weight":150.25,"wtime":"2018-11-11"},{"ID":5,"cattle_id":"1232","user_phone":"18014845521","weight":170.35,"wtime":"2018-11-14"},{"ID":6,"cattle_id":"1232","user_phone":"18014845521","weight":180.65,"wtime":"2018-11-16"},{"ID":7,"cattle_id":"1232","user_phone":"18014845521","weight":220.95,"wtime":"2018-11-18"},{"ID":8,"cattle_id":"1232","user_phone":"18014845521","weight":250.85,"wtime":"2018-11-21"},{"ID":9,"cattle_id":"1232","user_phone":"18014845521","weight":320.75,"wtime":"2018-11-26"},{"ID":10,"cattle_id":"1232","user_phone":"18014845521","weight":350.65,"wtime":"2018-11-27"},{"ID":11,"cattle_id":"1232","user_phone":"18014845521","weight":400.55,"wtime":"2018-11-28"},{"ID":12,"cattle_id":"1232","user_phone":"18014845521","weight":430.45,"wtime":"2018-12-02"},{"ID":13,"cattle_id":"1232","user_phone":"18014845521","weight":450.35,"wtime":"2018-12-04"},{"ID":14,"cattle_id":"1232","user_phone":"18014845521","weight":500.35,"wtime":"2018-12-08"},{"ID":16,"cattle_id":"1232","user_phone":"18014845521","weight":480.35,"wtime":"2018-12-12"}]
     * zw : [{"dzengzhong":0.0,"wtime":"2018-11-08"},{"dzengzhong":"19.70","wtime":"2018-11-09"},{"dzengzhong":"5.05","wtime":"2018-11-11"},{"dzengzhong":"6.70","wtime":"2018-11-14"},{"dzengzhong":"5.15","wtime":"2018-11-16"},{"dzengzhong":"20.15","wtime":"2018-11-18"},{"dzengzhong":"9.97","wtime":"2018-11-21"},{"dzengzhong":"13.98","wtime":"2018-11-26"},{"dzengzhong":"29.90","wtime":"2018-11-27"},{"dzengzhong":"49.90","wtime":"2018-11-28"},{"dzengzhong":"7.47","wtime":"2018-12-02"},{"dzengzhong":"9.95","wtime":"2018-12-04"},{"dzengzhong":"12.50","wtime":"2018-12-08"},{"dzengzhong":"-5.00","wtime":"2018-12-12"}]
     */

    private String cz;
    private String zw;

    public String getCz() {
        return cz;
    }

    public void setCz(String cz) {
        this.cz = cz;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }
}
