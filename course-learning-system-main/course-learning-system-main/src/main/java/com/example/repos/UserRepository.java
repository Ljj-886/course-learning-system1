package com.example.repos;


import com.example.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface UserRepository extends CrudRepository<User,String> {
    User findByUsername(String username);

    /**

     根据账号查找role
     */
    @Query(value="SELECT u.role FROM User u WHERE u.username=:name")String queryRoleByName(String name);
    /**

     根据role查找用户
     */
    List<User> findByRole(String role);
    /**

     保存用户

     */
    User save(User user);
    /**

     删除用户

     */
    void delete(User user);
    /**

     查询用户是否存在

     */
    boolean existsByUsername(String username);

    // ✅ 修正：添加 @Modifying 和 @Query 注解
    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.nickname = :nickname WHERE u.username = :username")
    int updateNicknameByUsername(String username, String nickname);

    // ✅ 新增：根据用户名更新密码
    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.password = :password WHERE u.username = :username")
    int updatePasswordByUsername(String username, String password);

}
