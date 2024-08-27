package com.example.repository.user;

import com.example.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    @Query("FROM User u where u.email=:email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
