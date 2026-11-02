package com.example.service;

import com.example.entities.User;
import com.example.entities.UserClass;
import com.example.repos.UserClassRepository;
import com.example.repos.UserRepository;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final UserClassRepository userClassRepository;

    public UserService(UserRepository userRepository, UserClassRepository userClassRepository){
        this.userRepository = userRepository;
        this.userClassRepository = userClassRepository;
    }

    @Override
    public void register(String classId, String username, String password, String nickname, String role){
        if( ! userRepository.existsByUsername(username)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setNickname(nickname);
            user.setIsValid("1");
            user.setRole("0");
            user.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            this.userRepository.save(user);
        }
        UserClass userClass = new UserClass();
        userClass.setUsername(username);
        userClass.setClassId(Integer.parseInt(classId));
        userClass.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        this.userClassRepository.save(userClass);
    }

    // ===================== 题目要求：加入班级方法 =====================
    @Override
    public boolean addUserToClass(String username, String classId) {
        UserClass userClass = new UserClass();
        userClass.setUsername(username);
        userClass.setClassId(Integer.parseInt(classId));
        userClass.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userClassRepository.save(userClass);
        return true;
    }

}