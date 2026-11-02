package com.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "clazz")
public class Clazz {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "major", nullable = false, length = 50)
    private String major;

    @Column(name = "student_count")
    private Integer studentCount;

    @Column(name = "oriented", length = 10)
    private String oriented;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "student_number", nullable = false)
    private Integer studentNumber;

    @Column(name = "create_date", nullable = false, length = 19)
    private String createDate;

    @Column(name = "valid", nullable = false, length = 1)
    private String valid;

    @Transient
    private String classId;

    @Transient
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getOriented() {
        return oriented;
    }

    public void setOriented(String oriented) {
        this.oriented = oriented;
    }

    public Clazz() {}

    public Clazz(Integer id, String name, Integer grade, String major, Integer studentCount) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.major = major;
        this.studentCount = studentCount;
    }

    // GETTER & SETTER
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
}