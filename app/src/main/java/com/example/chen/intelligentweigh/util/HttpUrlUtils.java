package com.example.chen.intelligentweigh.util;

/**
 * author : chen
 * date   : 2018/11/27  12:25
 * desc   :  URL地址工具类
 */
public class HttpUrlUtils {

    //基础URL
    public final static String BASE_URL = "http://106.14.145.208/IntelligentWeigh/";

    //头像URL
    public final static String TOUXIANG_URL = "http://106.14.145.208/";

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

    //所有用户信息URL
    public final static String ALL_USER_URL = BASE_URL+"BackAllUsers";

    //所有牧场信息URL
    public final static String ALL_HOUSE_URL = BASE_URL+"BackUserAllFarms";

    //添加牧场URL
    public final static String ADD_NEW_HOUSE = BASE_URL+"AddNewFarm";

    //指定牧场分区URL
    public final static String ALL_HOUSE_AREA_URL = BASE_URL+"BackFarmAreasByID";

    //添加牧场分区URL
    public final static String ADD_HOUSE_AREA_URL = BASE_URL+"AddFarmArea";

    //删除牧场分区URL
    public final static String DELETE_HOUSE_AREA_URL = BASE_URL+"DeleteFarmArea";

    //绑定用户牧场
    public final static String BING_USER_HOUSE_URL = BASE_URL+"BindUserFarms";

    //牧场信息
    public final static String HOUSE_CHOOSE_URL = BASE_URL+"BackBindFarmByBindStr";

    //肉牛品种
    public final static String CATTLEBACKALLKIND = BASE_URL+"CattleBackAllKind";

    //添加肉牛品种
    public final static String CATTLEKINDADD = BASE_URL+"CattleKindAdd";

    //录入新牛
    public final static String CATTLEADDSERVER = BASE_URL+"CattleAddServer";

    //根据条件返回肉牛（健在与删除）
    public final static String CATTLEBACKBYFRAMAREA = BASE_URL+"CattleBackByFarmArea";

    //返回肉牛称重的记录
    public final static String CATTLEBACKWEIGHRECORD = BASE_URL+"CattleBackWeighRecord";

    //删除牛
    public final static String DELETECOW = BASE_URL+"CattleDeleteSer";

    //云统计
    public final static String CLOUDMANAGE = BASE_URL+"CatYunCount";

    //添加一次称重记录
    public final static String ADDONEWEIGHT = BASE_URL+"CattleWeighCattle";

    //修改肉牛信息
    public final static String UPDATECOWINFO = BASE_URL+"CattleModifySer";
}
