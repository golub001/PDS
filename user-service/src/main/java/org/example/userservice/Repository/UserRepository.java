package org.example.userservice.Repository;

import org.example.userservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select firstName from user")
    List<String> getAllUserNames();

    Boolean existsByEmail(String email);

    Optional<?> getUsersByEmail(String email);

    @Query("SELECT u.id FROM user u WHERE u.email = :email")
    Optional<?> getUserIdByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM user u WHERE u.email = :email")
    int deleteByEmail(@Param("email") String email);
}
