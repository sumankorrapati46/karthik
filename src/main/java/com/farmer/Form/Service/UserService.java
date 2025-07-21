package com.farmer.Form.Service;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmer.Form.DTO.EmailServiceDTO;
import com.farmer.Form.DTO.UserDTO;
import com.farmer.Form.DTO.UserViewDTO;
import com.farmer.Form.Entity.User;
import com.farmer.Form.Mapper.UserMapper;
import com.farmer.Form.Repository.UserRepository;
import com.farmer.Form.exception.UserAlreadyExistsException;
import com.farmer.Form.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OtpService otpService;
 
    // ✅ Register a new user with OTP verification
    public User registerUser(UserDTO userDTO) {
        log.info("Registering user with email: {}", userDTO.getEmail());
 
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            log.warn("Email already registered: {}", userDTO.getEmail());
            throw new UserAlreadyExistsException("Email already registered: " + userDTO.getEmail());
        });
 
        // Check if phone number is already registered
        userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).ifPresent(user -> {
            log.warn("Phone number already registered: {}", userDTO.getPhoneNumber());
            throw new UserAlreadyExistsException("Phone number already registered: " + userDTO.getPhoneNumber());
        });
 
        // For now, allow registration without OTP verification for testing
        // In production, you might want to enforce OTP verification
        /*
        if (!otpService.isEmailOtpVerified(userDTO.getEmail())) {
            throw new IllegalStateException("OTP not verified. Please verify your email before registering.");
        }
        */
 
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set role from DTO (convert to enum)
        user.setRole(com.farmer.Form.Entity.Role.valueOf(userDTO.getRole().toUpperCase()));
        user.setStatus(com.farmer.Form.Entity.UserStatus.PENDING);
        user.setForcePasswordChange(false);
 
        User savedUser = userRepository.save(user);
        
        // Clear OTP verification if it exists
        try {
            otpService.clearEmailVerification(userDTO.getEmail());
        } catch (Exception e) {
            log.warn("Could not clear email verification: {}", e.getMessage());
        }
 
        try {
            emailService.sendRegistrationEmail(savedUser.getEmail(), savedUser.getFirstName());
        } catch (Exception e) {
            log.error("Failed to send welcome email: {}", e.getMessage());
        }
 
        return savedUser;
    }
 
    // ✅ Forgot User ID
    public String forgotUserId(String emailOrPhone) {
        User user = userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhoneNumber(emailOrPhone))
                .orElseThrow(() -> new UserNotFoundException("User not found with email or phone: " + emailOrPhone));
 
        String otp = otpService.generateAndSendOtp(emailOrPhone);
        try {
            emailService.sendOtpEmail(emailOrPhone,
                    "Your OTP to recover your User ID is: " + otp +
                    ". Use this OTP to verify your identity. Valid for 10 minutes.");
        } catch (Exception e) {
            log.error("Failed to send OTP email: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP.");
        }
 
        return "OTP sent to " + (emailOrPhone.contains("@") ? "email" : "phone") + ".";
    }
 
    // ✅ Reset password without OTP
    public boolean resetPasswordWithoutOtp(String emailOrPhone, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password and Confirm Password do not match.");
        }
 
        User user = userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhoneNumber(emailOrPhone))
                .orElseThrow(() -> new UserNotFoundException("User not found with email or phone: " + emailOrPhone));
 
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
 
        try {
            String subject = "Password Reset Confirmation";
            String body = "Dear " + user.getFirstName() + ",\n\n"
                    + "Your password has been successfully reset.\n"
                    + "If this was not you, please contact support immediately.\n\n"
                    + "Regards,\nFarmer Management Team";
 
            emailService.sendEmail(EmailServiceDTO.builder()
                    .to(user.getEmail())
                    .subject(subject)
                    .body(body)
                    .build());
 
        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage());
        }
 
        return true;
    }
 
    // ✅ Get all users
    public List<UserViewDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toViewDto)
                .collect(Collectors.toList());
    }
 
    // ✅ Get all users by role
    public List<UserViewDTO> getUsersByRole(String role) {
        return userRepository.findByRole(com.farmer.Form.Entity.Role.valueOf(role.toUpperCase())).stream()
                .map(userMapper::toViewDto)
                .collect(Collectors.toList());
    }
 
    // ✅ Get all users by status
    public List<UserViewDTO> getUsersByStatus(String status) {
        return userRepository.findByStatus(com.farmer.Form.Entity.UserStatus.valueOf(status.toUpperCase())).stream()
                .map(userMapper::toViewDto)
                .collect(Collectors.toList());
    }
 
    // ✅ Get users by role and status
    public List<UserViewDTO> getUsersByRoleAndStatus(String role, String status) {
        return userRepository.findByRoleAndStatus(
            com.farmer.Form.Entity.Role.valueOf(role.toUpperCase()),
            com.farmer.Form.Entity.UserStatus.valueOf(status.toUpperCase())
        ).stream()
                .map(userMapper::toViewDto)
                .collect(Collectors.toList());
    }
 
    // ✅ Get individual user by ID
    public UserViewDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return userMapper.toViewDto(user);
    }
 
    // ✅ Update status (approve/reject user)
    public void updateUserStatus(Long id, String newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        user.setStatus(com.farmer.Form.Entity.UserStatus.valueOf(newStatus.toUpperCase()));
        userRepository.save(user);
    }

    // ✅ Approve user and assign role (Super Admin)
    public void approveAndAssignRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        if (user.getStatus() != com.farmer.Form.Entity.UserStatus.PENDING) {
            throw new IllegalStateException("User is not pending approval.");
        }
        String tempPassword = generateTempPassword();
        user.setRole(com.farmer.Form.Entity.Role.valueOf(role.toUpperCase()));
        user.setStatus(com.farmer.Form.Entity.UserStatus.APPROVED);
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setForcePasswordChange(true);
        userRepository.save(user);
        try {
            String subject = "Account Approved - Please Change Your Password";
            String body = "Dear " + user.getFirstName() + ",\n\n" +
                    "Your account has been approved. Please login with the following credentials and change your password on first login.\n" +
                    "Username: " + user.getEmail() + "\n" +
                    "Temporary Password: " + tempPassword + "\n\n" +
                    "Regards,\nAdmin Team";
            emailService.sendEmail(EmailServiceDTO.builder()
                    .to(user.getEmail())
                    .subject(subject)
                    .body(body)
                    .build());
        } catch (Exception e) {
            log.error("Failed to send approval email: {}", e.getMessage());
        }
    }

    private String generateTempPassword() {
        // Simple temp password generator (customize as needed)
        return "Temp@" + (int)(Math.random() * 100000);
    }

    public User getUserByEmailOrPhone(String emailOrPhone) {
        return userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhoneNumber(emailOrPhone))
                .orElseThrow(() -> new UserNotFoundException("User not found with email or phone: " + emailOrPhone));
    }

    public boolean resetPasswordAndClearForceFlag(String emailOrPhone, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password and Confirm Password do not match.");
        }
        User user = getUserByEmailOrPhone(emailOrPhone);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setForcePasswordChange(false);
        userRepository.save(user);
        try {
            String subject = "Password Reset Confirmation";
            String body = "Dear " + user.getFirstName() + ",\n\n"
                    + "Your password has been successfully reset.\n"
                    + "If this was not you, please contact support immediately.\n\n"
                    + "Regards,\nFarmer Management Team";
            emailService.sendEmail(EmailServiceDTO.builder()
                    .to(user.getEmail())
                    .subject(subject)
                    .body(body)
                    .build());
        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage());
        }
        return true;
    }

    public List<User> getAllUsersRaw() {
        return userRepository.findAll();
    }

    public User getUserRawById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public User createUserBySuperAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUserBySuperAdmin(Long id, User updatedUser) {
        User user = getUserRawById(id);
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        // ... update other fields as needed
        return userRepository.save(user);
    }

    public void deleteUserBySuperAdmin(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getPendingUsersRaw() {
        return userRepository.findByStatus(com.farmer.Form.Entity.UserStatus.PENDING);
    }

    public List<User> getApprovedUsersRaw() {
        return userRepository.findByStatus(com.farmer.Form.Entity.UserStatus.APPROVED);
    }

    public void resendOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        otpService.generateAndSendOtp(email);
        emailService.sendEmail(EmailServiceDTO.builder()
            .to(email)
            .subject("Your OTP Code")
            .body("Your new OTP has been sent to this email.")
            .build());
    }
}