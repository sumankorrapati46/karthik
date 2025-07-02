package com.farmer.Form.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
 
@Data
public class ResetPasswordDTO {
 
    @NotBlank(message = "Email or phone number is required")
    private String emailOrPhone;
 
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
 
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
