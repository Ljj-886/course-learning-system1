package com.example.repos;

import com.example.entities.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClassRepository extends JpaRepository<UserClass, Integer> {

    UserClass findByUsername(String username);
    // 删除用户的所有班级记录（给 moveUserToClass 使用）
    void deleteByUsername(String username);

}