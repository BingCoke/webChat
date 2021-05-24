package com.dzt.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyResult extends HashMap<String,Object> {
    private int code;
    private String msg;

    private MyResult() {}

    public static MyResult build(){
        return new MyResult();
    }

    public MyResult setCode(int code) {
        this.put("code",code);
        return this;
    }

    public MyResult setMsg(String msg) {
        this.put("msg",msg);
        return this;
    }

    public MyResult setData(Object o){
        this.put("data",o);
        return this;
    }

    public MyResult setData(List list){
        this.put("data",list);
        return this;
    }

    public MyResult add(String key, Object value){
        super.put(key,value);
        return this;
    }

    public String toJson(){
        return JSONObject.toJSONString(this);
    }


}
