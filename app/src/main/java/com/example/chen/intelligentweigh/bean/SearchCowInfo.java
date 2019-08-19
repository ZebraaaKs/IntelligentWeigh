package com.example.chen.intelligentweigh.bean;

/**
 * @author chen
 * @date 2019/8/19.   0:16
 * description：
 */
public class SearchCowInfo {

    /**
     * cattle : {"ID":"900002600152495","name":"默认","past_id":10,"farmname":"牧场一","kind":"克斯那","sex":"公","birthday":"2019-01-09","entranceDay":"2019-01-09","registerDay":"2019-01-09","enteranceprice":55.0,"father_id":"0000","mother_id":"0000"}
     * cz : [{"ID":46,"cattle_id":"900002600152495","user_phone":"15862187050","weight":25.5,"wtime":"2018-03-10"},{"ID":20,"cattle_id":"900002600152495","user_phone":"18014845521","weight":8531.5,"wtime":"2018-11-29"},{"ID":22,"cattle_id":"900002600152495","user_phone":"18014845521","weight":8531.5,"wtime":"2018-11-29"},{"ID":27,"cattle_id":"900002600152495","user_phone":"18014845521","weight":8531.5,"wtime":"2018-11-29"},{"ID":28,"cattle_id":"900002600152495","user_phone":"18014845521","weight":8531.5,"wtime":"2018-11-29"},{"ID":19,"cattle_id":"900002600152495","user_phone":"18014845521","weight":8531.5,"wtime":"2018-12-24"},{"ID":48,"cattle_id":"900002600152495","user_phone":"15862187050","weight":86.0,"wtime":"2019-01-04"},{"ID":77,"cattle_id":"900002600152495","user_phone":"15162249219","weight":2153.5,"wtime":"2019-01-09"},{"ID":92,"cattle_id":"900002600152495","user_phone":"18014845521","weight":2153.5,"wtime":"2019-01-11"},{"ID":93,"cattle_id":"900002600152495","user_phone":"18014845521","weight":2153.5,"wtime":"2019-01-11"},{"ID":94,"cattle_id":"900002600152495","user_phone":"18014845521","weight":2153.5,"wtime":"2019-01-11"},{"ID":101,"cattle_id":"900002600152495","user_phone":"15162249219","weight":2153.5,"wtime":"2019-02-11"},{"ID":171,"cattle_id":"900002600152495","user_phone":"15895205838","weight":117.0,"wtime":"2019-05-08"},{"ID":148,"cattle_id":"900002600152495","user_phone":"15162249219","weight":2153.5,"wtime":"2019-05-17"}]
     * zw : [{"dzengzhong":0.0,"wtime":"2018-03-10"},{"dzengzhong":"32.22","wtime":"2018-11-29"},{"dzengzhong":"0.00","wtime":"2018-11-29"},{"dzengzhong":"0.00","wtime":"2018-11-29"},{"dzengzhong":"0.00","wtime":"2018-11-29"},{"dzengzhong":"0.00","wtime":"2018-12-24"},{"dzengzhong":"-767.77","wtime":"2019-01-04"},{"dzengzhong":"413.50","wtime":"2019-01-09"},{"dzengzhong":"0.00","wtime":"2019-01-11"},{"dzengzhong":"0.00","wtime":"2019-01-11"},{"dzengzhong":"0.00","wtime":"2019-01-11"},{"dzengzhong":"0.00","wtime":"2019-02-11"},{"dzengzhong":"-23.68","wtime":"2019-05-08"},{"dzengzhong":"226.28","wtime":"2019-05-17"}]
     */

    private String cattle;
    private String cz;
    private String zw;

    public String getCattle() {
        return cattle;
    }

    public void setCattle(String cattle) {
        this.cattle = cattle;
    }

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
