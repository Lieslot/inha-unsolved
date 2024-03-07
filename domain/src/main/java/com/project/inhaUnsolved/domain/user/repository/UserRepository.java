package com.project.inhaUnsolved.domain.user.repository;

import com.project.inhaUnsolved.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByHandle(String handle);

    @Query(nativeQuery = true, value= "SELECT COUNT(*) FROM users")
    Long getUserCount();

    List<User> findAllByHandleIn(List<String> handle);

    boolean existsByHandle(String handle);

}
