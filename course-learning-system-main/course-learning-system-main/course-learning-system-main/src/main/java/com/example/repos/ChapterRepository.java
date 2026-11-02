package com.example.repos;

import com.example.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

    // 根据课程ID查询所有章节
    List<Chapter> findByCourseIdOrderByCode(Integer courseId);

    // 查询课程下 以 0000 结尾的父章节（第一章、第二章...）
    List<Chapter> findByCourseIdAndCodeEndingWithOrderByCode(Integer courseId, String suffix);

    // 根据 parentCode 查询子章节（按code排序）
    List<Chapter> findByCourseIdAndParentCodeOrderByCode(Integer courseId, String parentCode);

    List<Chapter> findByCourseIdAndCodeEndingWith(Integer courseId, String number);
    List<Chapter> findByCourseIdAndCodeLike(Integer courseId, String codeLike);

    List<Chapter> findByCourseIdAndParentCode(Integer courseId, String code);
    // 🔥 专门给下拉菜单用：强制按 code 数字正序（绝对不乱）
    @Query(value = "SELECT * FROM chapter WHERE course_id = :courseId ORDER BY code ASC", nativeQuery = true)
    List<Chapter> listChaptersSorted(@Param("courseId") Integer courseId);

    List<Chapter> findByCourseId(Integer courseId);

    List<Chapter> findAllByCourseId(Integer courseId);

    Chapter findByCourseIdAndCode(Integer courseId, String chapterCode);
}