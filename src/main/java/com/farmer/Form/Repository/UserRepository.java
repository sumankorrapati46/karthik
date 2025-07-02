package com.farmer.Form.Repository;

import com.farmer.Form.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
 
public interface UserRepository extends JpaRepository<User, Long> {
 
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailOrPhoneNumber(String email, String phone);
 
    // ✅ ADD THESE METHODS:
    List<User> findByRole(String role);
    List<User> findByStatus(String status);
    List<User> findByRoleAndStatus(String role, String status);
}
