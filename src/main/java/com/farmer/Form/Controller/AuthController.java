package com.farmer.Form.Controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.farmer.Form.DTO.LoginRequest;
import com.farmer.Form.DTO.ResetPasswordDTO;
import com.farmer.Form.DTO.UserDTO;
import com.farmer.Form.DTO.UserResponseDTO;
import com.farmer.Form.DTO.UserViewDTO;
import com.farmer.Form.Entity.User;
import com.farmer.Form.Service.CountryStateCityService;
import com.farmer.Form.Service.EmailService;
import com.farmer.Form.Service.OtpService;
import com.farmer.Form.Service.UserService;
import com.farmer.Form.security.JwtUtil;
import com.farmer.Form.exception.UserAlreadyExistsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final CountryStateCityService countryService;

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
            String token = jwtUtil.generateToken(authentication);
            // Get user details
            User user = userService.getUserByEmailOrPhone(request.getUserName());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(error);
        }
    }

    // ✅ REGISTER
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Registration successful - waiting for approval");
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid data: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ✅ SEND OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String otp = otpService.generateAndSendOtp(emailOrPhone.trim());
        emailService.sendOtpEmail(emailOrPhone.trim(),
                "Your OTP is: " + otp + ". It is valid for 10 minutes.");
        return ResponseEntity.ok("OTP sent successfully to your registered email or phone.");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }
        try {
            String otp = otpService.generateAndSendOtp(email);
            return ResponseEntity.ok(Map.of("message", "OTP re-sent successfully to " + email));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error resending OTP"));
        }
    }

    // ✅ VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        String otp = request.get("otp");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty() ||
                otp == null || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email/Phone and OTP are required.");
        }
        boolean verified = otpService.verifyOtp(emailOrPhone.trim(), otp.trim());
        return verified
                ? ResponseEntity.ok("OTP verified successfully.")
                : ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                                .body("Invalid or expired OTP.");
    }

    // ✅ FORGOT USER ID
    @PostMapping("/forgot-user-id")
    public ResponseEntity<String> forgotUserId(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String result = userService.forgotUserId(emailOrPhone.trim());
        return ResponseEntity.ok(result);
    }

    // ✅ FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String otp = otpService.generateAndSendOtp(emailOrPhone.trim());
        emailService.sendOtpEmail(emailOrPhone.trim(),
                "Your password reset OTP is: " + otp + ". It is valid for 10 minutes.");
        return ResponseEntity.ok("Password reset OTP sent successfully.");
    }

    // ✅ RESET PASSWORD WITHOUT OTP
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        try {
            boolean success = userService.resetPasswordWithoutOtp(
                    request.getEmailOrPhone().trim(),
                    request.getNewPassword().trim(),
                    request.getConfirmPassword().trim()
            );
            return success
                    ? ResponseEntity.ok("Password changed successfully.")
                    : ResponseEntity.status(500).body("Password change failed.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
        }
    }

    // 🌐 Location APIs
    @GetMapping("/countries")
    public ResponseEntity<String> getCountries() {
        return ResponseEntity.ok(countryService.getCountries());
    }

    @GetMapping("/states/{countryCode}")
    public ResponseEntity<String> getStates(@PathVariable String countryCode) {
        return ResponseEntity.ok(countryService.getStates(countryCode));
    }

    @GetMapping("/districts/{countryCode}/{stateCode}")
    public ResponseEntity<String> getDistricts(@PathVariable String countryCode,
                                               @PathVariable String stateCode) {
        return ResponseEntity.ok(countryService.getDistricts(countryCode, stateCode));
    }

    // ✅ Get all users (unfiltered)
    @GetMapping("/users/all")
    public ResponseEntity<List<UserViewDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ Get users by role
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserViewDTO>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    // ✅ Get users by status
    @GetMapping("/users/status/{status}")
    public ResponseEntity<List<UserViewDTO>> getUsersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(userService.getUsersByStatus(status));
    }

    // ✅ Get users by role and status
    @GetMapping("/users")
    public ResponseEntity<List<UserViewDTO>> getUsersByRoleAndStatus(
            @RequestParam String role,
            @RequestParam String status) {
        return ResponseEntity.ok(userService.getUsersByRoleAndStatus(role, status));
    }

    // ✅ Get individual user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<UserViewDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ✅ Update user status
    @PutMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        if (newStatus == null || newStatus.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Status is required");
        }
        userService.updateUserStatus(id, newStatus.trim());
        return ResponseEntity.ok("Status updated successfully");
    }

    // ✅ Approve user and assign role (Super Admin)
    @PutMapping("/users/{id}/approve")
    public ResponseEntity<String> approveAndAssignRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String role = request.get("role");
        if (role == null || role.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Role is required");
        }
        try {
            userService.approveAndAssignRole(id, role.trim());
            return ResponseEntity.ok("User approved and role assigned successfully. Credentials sent to user email.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 🔧 Test endpoint
    @GetMapping("/test")
    public String test() {
        return "This is a test endpoint.";
    }

    // 🔧 Test registration endpoint
    @GetMapping("/test-registration")
    public String testRegistration() {
        return "Registration endpoint is accessible.";
    }
}
