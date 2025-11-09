package com.graduation.controller;

import com.graduation.dto.CourseCreateDto;
import com.graduation.dto.CourseDto;
import com.graduation.dto.TeacherDto;
import com.graduation.entity.CampusUser;
import com.graduation.entity.Course;
import com.graduation.repository.CampusUserRepository;
import com.graduation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CampusUserRepository campusUserRepository;

    // 查: 获取某个老师的所有课程
    @GetMapping("/teacher/{teacherId}")
    public List<Course> getCoursesByTeacher(@PathVariable Long teacherId) {
        // 此时 TeacherId 是一个 Long 类型的 ID，而不是 CampusUser 对象 Teacher teacher 是代理对象
        List<Course> courses =courseRepository.findByTeacherId(teacherId);

        /*for (Course course : courses) {
            // Course: 计算机网络, Teacher ID: 1
            int teacherIdqq  = course.getTeacher() != null ? course.getTeacher().getId().intValue() : -1;
            System.out.println("Course: " + course.getCourseName() + ", Teacher ID: " + (course.getTeacher() != null ? course.getTeacher().getId() : "null"));
            CampusUser teacher = campusUserRepository.findById(Long.valueOf(teacherIdqq)).orElse(null);
            course.setTeacher(teacher);
        }*/

        return courses;


    }

    // 增: 为某个老师创建一个新课程
    @PostMapping

    public ResponseEntity<Course> createCourse(@RequestBody CourseCreateDto courseDto) {
        Course course = new Course();
        course.setCourseCode(courseDto.getCourseCode());
        course.setCourseName(courseDto.getCourseName());

        // !!! 问题很可能出在这里 !!!
        // 您不能直接将 courseDto.getTeacherId() 赋值给 course.setTeacher()
        // 因为 course.setTeacher() 期望的是一个 CampusUser 对象，而不是一个 Long

        // 如果您没有正确处理 teacherId，那么 course.teacher 字段在保存时就会是 null
        // 或者您可能尝试了类似 course.setTeacher(new CampusUser(courseDto.getTeacherId()));
        // 这种情况下，这个 CampusUser 对象是 transient 的，因为它没有被 EntityManager 管理。

        // 正确的做法是：
        // 1. 根据 teacherId 从数据库中查找对应的 CampusUser 实体
        CampusUser teacher = campusUserRepository.findById(courseDto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + courseDto.getTeacherId()));

        // 2. 将查找到的 CampusUser 实体设置到 Course 对象中
        course.setTeacher(teacher);

        // 3. 保存 Course 实体
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.ok(savedCourse);
    }

    // 查: 获取单个课程详情 (用于详情页)
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseRepository.findByIdWithTeacher(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 改: 更新一个课程
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseRepository.findByIdWithTeacher(id)
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


    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        // 1. 查询 Course 实体列表
        // 如果需要 teacher 信息，可以在这里使用 JOIN FETCH 强制加载，避免 N+1 问题
        // 例如：List<Course> courses = courseRepository.findAllWithTeachers();
        List<Course> courses = courseRepository.findAll();

        // 2. 将实体映射到 DTO
        List<CourseDto> courseDtos = courses.stream().map(course -> {
            CourseDto dto = new CourseDto();
            dto.setId(course.getId());
            dto.setCourseCode(course.getCourseCode());
            dto.setCourseName(course.getCourseName());

            // 只有当 teacher 存在且被初始化时才设置 DTO 中的 teacher 信息
            // Hibernate.isInitialized(course.getTeacher()) 可以检查代理是否已初始化
            if (course.getTeacher() != null && course.getTeacher().getId() != null) { // 简单的检查，确保代理不是null且有ID
                TeacherDto teacherDto = new TeacherDto();
                teacherDto.setId(course.getTeacher().getId());
                teacherDto.setUsername(course.getTeacher().getUsername()); // 假设 CampusUser 有 getUsername() 方法
                dto.setTeacher(teacherDto);
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(courseDtos);
    }
}