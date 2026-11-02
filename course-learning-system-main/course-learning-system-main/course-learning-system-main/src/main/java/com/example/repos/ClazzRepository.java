
package com.example.repos;

import com.example.entities.Clazz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClazzRepository extends CrudRepository<Clazz, Integer> {

    // 根据班级名称查询
    Clazz findByName(String name);

    // 根据年级查询班级列表
    List<Clazz> findByGrade(Integer grade);

    // 根据专业查询班级列表
    List<Clazz> findByMajor(String major);

    // 根据年级 + 专业联合查询
    List<Clazz> findByGradeAndMajor(Integer grade, String major);

    // 根据ID查询班级名称（模仿User的queryRoleByName）
    @Query("SELECT c.name FROM Clazz c WHERE c.id = :id")
    String queryClazzNameById(Integer id);

    // 判断班级名称是否存在（可选，业务常用）
    boolean existsByName(String name);

    // ===================== ✅ 【只加这一行】根据用户名查班级（完全匹配题目） =====================
    @Query(value = "SELECT c.* FROM clazz c JOIN user_class uc ON c.id = uc.class_id WHERE uc.username = :username AND c.valid = '1'", nativeQuery = true)
    List<Clazz> findClassesByUsername(String username);
}