package com.graduation.repository;

import com.graduation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Spring Data JPA 会自动根据方法名生成查询
    List<Course> findByTeacherId(Long teacherId);
}