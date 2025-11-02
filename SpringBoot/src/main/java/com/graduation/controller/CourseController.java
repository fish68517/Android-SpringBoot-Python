package com.graduation.controller;

import com.graduation.entity.Course;
import com.graduation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    // 查: 获取某个老师的所有课程
    @GetMapping("/teacher/{teacherId}")
    public List<Course> getCoursesByTeacher(@PathVariable Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    // 增: 为某个老师创建一个新课程
    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        // 在真实应用中，应该从安全上下文中获取 teacherId，而不是信任请求体
        return courseRepository.save(course);
    }

    // 查: 获取单个课程详情 (用于详情页)
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 改: 更新一个课程
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setCourseName(courseDetails.getCourseName());
                    course.setCourseCode(courseDetails.getCourseCode());
                    return ResponseEntity.ok(courseRepository.save(course));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 删: 删除一个课程
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}