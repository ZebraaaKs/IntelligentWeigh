package com.example.chen.intelligentweigh.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chen
 * @date 2019/8/6.   14:13
 * description：
 */
public class DataCheckUtils {

    /***

     * 判断 String 是否是 int<br>通过正则表达式判断

     * 

     * @param input

     * @return

     */

    public static boolean isInteger(String input){
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.find();

    }



    /**

     * 判断 String 是否是 double<br>通过正则表达式判断

     * @param input

     * @return

     */

    public static boolean isDouble(String input){
        Matcher mer = Pattern.compile("^[+-]?[0-9.]+$").matcher(input);
        return mer.find();
    }



    /**

     * 检测字符串是否为 number 类型的数字

     * @param str

     * @return

     */

    public static boolean isNumeric(String str){
        if(str == null ){
            return false;
         }
        String strF = str.replaceAll("-", "");
        String strFormat = strF.replaceAll("\\.", "");
        if("".equals(strFormat)){
            return false;
        }
        for (int i = strFormat.length();--i>=0;){
            if (!Character.isDigit(strFormat.charAt(i))){
                return false;
            }
        }
        return true;
    }


    public static Double string2Double(String data){
        return Double.parseDouble(data);
    }

    public static String double2String(double data){
        return Double.toString(data);
    }

}
