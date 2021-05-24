package com.dzt.util;

import javax.websocket.server.PathParam;
import java.util.regex.Pattern;

public class PatternUtils {
    /**
     * 用户名的正则表达式
     * 字母开头，只能字母，数字，下划线，5到16个字符
     */
    private static String USER_CHECK = "[a-zA-Z0-9_]{5,15}$";
    /**
     * 密码的正则表达式
     * 字母和数字的组合,可以有特殊字符
     * 6-18位
     */
    private static String PASSWORD_CHECK = "^(?![a-zA-Z]+$)(?![0-9]+$)[0-9A-Za-z\\W]{6,18}$";
    private static String PHONE_CHECK = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9])|(19[0-9]))\\d{8}$";
    private static String MAIL_CHECK = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private static String NUMBER_CHECK = "^[1-9]\\d*$";
    public static Boolean usernameCheck(String username){
        if (username != null){
            return Pattern.matches(USER_CHECK,username);
        } else {
            return false;
        }

    }

    public static Boolean passwordCheck(String password){
        if (password != null){
            return Pattern.matches(PASSWORD_CHECK,password);
        } else {
            return false;
        }

    }

    public static Boolean phoneCheck(String phone){
        if (phone != null){
            return Pattern.matches(PHONE_CHECK,phone);
        } else {
            return false;
        }
    }

    public static Boolean mailCheck(String mail){
        if (mail != null){
            return Pattern.matches(MAIL_CHECK,mail);
        } else  {
            return false;
        }
    }

    public static Boolean numberCheck(String number) {
        if (number != null){
            return Pattern.matches(NUMBER_CHECK,number);
        } else {
            return false;
        }
    }
    public static void main(String[] args) {
        System.out.println(numberCheck("123"));
    }
}
