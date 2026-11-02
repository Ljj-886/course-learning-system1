package com.restservice;

import com.alibaba.fastjson.JSONObject;

public class CommonMessage{
    private String code;
    private String info;
    private Object data;
    public CommonMessage(){
    }
    public CommonMessage(String code,String info){
        this.code=code;
        this.info=info;
    }
    public CommonMessage(String code){
        this.code=code;
    }

    public CommonMessage(String number, String s, Object data) {
        this.code = number;
        this.info = s;
        this.data = data;
    }
    // ===================== 只加这一个方法 =====================
    public static CommonMessage success(Object data) {
        return new CommonMessage("200", null, data);
    }

    public String getCode(){
        return code;
    }
    public void setCode(String code){
        this.code=code;
    }
    public String getInfo(){
        return info;
    }
    public void setInfo(String info){
        this.info=info;
    }
    public Object getData(){
        return data;
    }
    public void setData(Object data){
        this.data=data;
    }
}