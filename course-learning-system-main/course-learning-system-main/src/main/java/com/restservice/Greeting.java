package com.restservice;
public class Greeting{
    private final long id;
    private final String info;
    public Greeting(long id,String info){
        this.id=id;
        this.info=info;
    }
    public long getId(){
        return id;
    }
    public String getInfo(){
        return info;
    }
}