package com.dzt.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Z
 */
public class StringUtil {

    /**
     下划线命名转驼峰
     */
    public static String toCamelCase(String name){
        String[] s = name.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s[0]);
        for (int i = 1; i < s.length; i++) {
            stringBuilder.append(s[i].substring(0,1).toUpperCase() + s[i].substring(1));
        }

        return stringBuilder.toString();
    }



    /**
     驼峰转下划线
     */
    public static String toUnderScoreCase(String name){
        StringBuilder stringBuilder = new StringBuilder();
        String s = null;
        s = name.substring(0,1);
        if(s.equals(s.toUpperCase())){
            stringBuilder.append(s.toLowerCase());
        } else {
            stringBuilder.append(s);
        }

        for (int i = 1; i < name.length(); i++) {
            s = name.substring(i,i+1);
            if(s.equals(s.toUpperCase())){
                stringBuilder.append("_");
            }
            stringBuilder.append(s.toLowerCase());
        }

        return stringBuilder.toString();
    }


    /**
     * 对字符串根据字数进行分组处理
     * @param str
     * @return
     */
    public static List<String> pagination(String str){
        int i = 600;
        List<String> list = new ArrayList<>();
        int count = 0 , j;
        for ( j = 0; j < str.length() - i ; j = j + i) {
            list.add(str.substring(j,j + i));
        }
        list.add(str.substring(j));
        return list;
    }

    /**
     * 将分组处理的list转换成字符串
     * @param list
     * @return
     */
    public static String gather(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }


    public static boolean notEmpty(String s){
        if (s != null && s != ""){
            return true;
        }
        return false;
    }

}
