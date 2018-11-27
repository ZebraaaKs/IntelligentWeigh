package com.example.chen.intelligentweigh.util;

/**
 * author : chen
 * date   : 2018/11/27  12:25
 * desc   :  URL地址工具类
 */
public class HttpUrlUtils {

    //基础URL
    public final static String BASE_URL = "http://106.14.145.208/IntelligentWeigh/";

    //登录url
    public final static String LOGIN_URL = BASE_URL+"LoginCheckSer";

    //注册url
    public final static String REGISTER_URL = BASE_URL+"RegisterMemberSer";

    //修改用户信息（除头像）url
    public final static String CHANGEINFO_NOTOUXINANG_URL = BASE_URL+"ModifyUserInfo";

    //修改用户头像信息url
    public final static String CHANGEINFO_TOUXIANG_URL = BASE_URL+"ModtifyUserPhoto";

    //删除用户url
    public final static String DELETEUSER_URL = BASE_URL+"DeleteUser";
}
