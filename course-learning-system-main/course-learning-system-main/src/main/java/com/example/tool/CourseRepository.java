package com.example.tool;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    // 查询 valid=1 的课程
    List<Course> findByValid(String valid);
}