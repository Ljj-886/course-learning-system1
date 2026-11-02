package com.example.service;

public interface IUserService{
    /**
     用户注册
     @param classId 班级id
     @param username 用户名
     @param password 用户密码
     @param nickname 昵称
     @param role 角色
     */
    void register(String classId,String username,String password,String nickname,String role);

    // ===================== 只加这一行 =====================
    boolean addUserToClass(String username, String classId);



}