package com.farmer.Form.Repository;

import com.farmer.Form.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.farmer.Form.Entity.Role;
import com.farmer.Form.Entity.UserStatus;
 
public interface UserRepository extends JpaRepository<User, Long> {
 
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmailOrPhoneNumber(String email, String phone);
 
    // ✅ ADD THESE METHODS:
    List<User> findByRole(Role role);
    List<User> findByStatus(UserStatus status);
    List<User> findByRoleAndStatus(Role role, UserStatus status);
}
