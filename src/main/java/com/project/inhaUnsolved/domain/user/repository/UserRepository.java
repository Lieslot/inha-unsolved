package com.project.inhaUnsolved.domain.user.repository;

import com.project.inhaUnsolved.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
