package com.graduation.repository;

import com.graduation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Spring Data JPA 会自动根据方法名生成查询
    // 使用 JOIN FETCH 一次性加载 Course 和关联的 Teacher
    // 这会生成一条 SQL JOIN 语句，非常高效
    @Query("SELECT c FROM Course c JOIN FETCH c.teacher WHERE c.teacher.id = :teacherId")
    List<Course> findByTeacherId(Long teacherId);


    // --- NEW METHOD FOR THE DETAIL VIEW ---
    /**
     * Finds a single Course by its ID and eagerly fetches the associated Teacher.
     * This prevents LazyInitializationException when the Course is serialized.
     * The return type is Optional<Course> to gracefully handle cases where the course is not found.
     *
     * @param id The ID of the course to find.
     * @return An Optional containing the Course with its Teacher, or an empty Optional if not found.
     */
    @Query("SELECT c FROM Course c JOIN FETCH c.teacher WHERE c.id = :id")
    Optional<Course> findByIdWithTeacher(Long id);
}