package com.project.myboard.user.repository;

import com.project.myboard.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User getByUid(String uid);

    Optional<User> findByUid(String uid);

    String getNameByUid(String uid);
}
