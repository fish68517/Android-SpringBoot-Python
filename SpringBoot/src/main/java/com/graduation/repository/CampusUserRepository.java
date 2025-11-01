package com.graduation.repository;


import com.graduation.entity.CampusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CampusUserRepository extends JpaRepository<CampusUser, Long> {
    Optional<CampusUser> findByUsername(String username);
    // (可以添加 findByRole(int role) 来获取所有学生/教师)

    // 添加 username 和 password 的查找方法来登录
    Optional<CampusUser> findByUsernameAndPassword(String username, String password);
}