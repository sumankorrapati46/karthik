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
 
        if (!otpService.isVerified(userDTO.getEmail())) {
            throw new IllegalStateException("OTP not verified. Please verify before registering.");
        }
 
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("Farmer");
        user.setStatus("Pending");
 
        User savedUser = userRepository.save(user);
        otpService.clearVerification(userDTO.getEmail());
 
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
        return userRepository.findByRole(role).stream()
                .map(userMapper::toViewDto)
                .collect(Collectors.toList());
    }
 
    // ✅ Get all users by status
    public List<UserViewDTO> getUsersByStatus(String status) {
        return userRepository.findByStatus(status).stream()
                .map(userMapper::toViewDto)
                .collect(Collectors.toList());
    }
 
    // ✅ Get users by role and status
    public List<UserViewDTO> getUsersByRoleAndStatus(String role, String status) {
        return userRepository.findByRoleAndStatus(role, status).stream()
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
        user.setStatus(newStatus);
        userRepository.save(user);
    }
}