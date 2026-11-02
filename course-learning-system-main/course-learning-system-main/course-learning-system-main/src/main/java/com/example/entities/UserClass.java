package com.example.entities;
import javax.persistence.*;
@Entity@Table(name="user_class")
public class UserClass{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private Integer id;
    @Column(name="username",nullable=false)
    private String username;
    @Column(name="class_id",nullable=false)
    private Integer classId;
    @Column(name="datetime",length=19)
    private String datetime;
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id=id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public Integer getClassId(){
        return this.classId;
    }
    public void setClassId(Integer classField){
        this.classId=classField;
    }
    public String getDatetime(){
        return datetime;
    }
    public void setDatetime(String datetime){
        this.datetime=datetime;
    }
}