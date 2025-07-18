package com.farmer.Form.DTO;
 
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
 
import java.time.LocalDate;
 
@Data
public class EmployeeDTO {
 
    // Personal Information
    @NotBlank(message = "Salutation is required")
    private String salutation;
 
    @NotBlank(message = "First name is required")
    private String firstName;
 
    private String middleName;
 
    @NotBlank(message = "Last name is required")
    private String lastName;
 
    @NotBlank(message = "Gender is required")
    private String gender;
 
    @NotBlank(message = "Nationality is required")
    private String nationality;
 
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;
 
    // Contact Details
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
    private String contactNumber;
 
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;
 
    private String relationType;
 
    private String relationName;
 
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit alternate number")
    private String altNumber;
 
    private String altNumberType;
 
    // Address
    @NotBlank(message = "Country is required")
    private String country;
 
    @NotBlank(message = "State is required")
    private String state;
 
    @NotBlank(message = "District is required")
    private String district;
 
    private String block;
    private String village;
 
    @Pattern(regexp = "^\\d{6}$", message = "Zipcode must be a 6-digit number")
    private String zipcode;
 
    private String sector;
 
    // Professional Details
    @NotBlank(message = "Education is required")
    private String education;
 
    @NotBlank(message = "Experience is required")
    private String experience;
 
    // Bank Details
    @NotBlank(message = "Bank name is required")
    private String bankName;
 
    @NotBlank(message = "Account number is required")
    private String accountNumber;
 
    @NotBlank(message = "Branch name is required")
    private String branchName;
 
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Enter a valid IFSC code")
    private String ifscCode;
 
    // Document Info
    @NotBlank(message = "Document type is required")
    private String documentType;
 
    @NotBlank(message = "Document number is required")
    private String documentNumber;
 
    // Portal Access
    @NotBlank(message = "Role is required")
    private String role;
 
    @NotBlank(message = "Access status is required")
    private String accessStatus;
 
    // File Uploads (Validate in service or controller)
    private MultipartFile photo;
    private MultipartFile passbook;
    private MultipartFile documentFile;
}
 
 