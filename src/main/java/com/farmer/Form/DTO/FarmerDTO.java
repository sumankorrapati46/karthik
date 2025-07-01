package com.farmer.Form.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class FarmerDTO {

    private Long id;

    // Personal Info
    @JsonIgnore
    private MultipartFile photo;
    private String photoFileName;
    private String photoUrl;

    @NotBlank(message = "Salutation is required")
    private String salutation;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Father's name is required")
    private String fatherName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid contact number")
    private String contactNumber;

    private String alternativeRelationType;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid alternative contact number")
    private String alternativeContactNumber;

    @NotBlank(message = "Nationality is required")
    private String nationality;

    // Address
    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "District is required")
    private String district;

    private String block;
    private String village;

    @Pattern(regexp = "^\\d{6}$", message = "Invalid pincode")
    private String pincode;

    // Education and Experience
    @NotBlank(message = "Education is required")
    private String education;

    @NotBlank(message = "Experience is required")
    private String experience;

    // Current Crop
    @JsonIgnore
    private MultipartFile cropPhoto;
    private String cropPhotoFileName;
    private String cropPhotoUrl;

    @NotBlank(message = "Current survey number is required")
    private String currentSurveyNumber;

    @NotNull(message = "Current land holding is required")
    @PositiveOrZero(message = "Land holding must be 0 or positive")
    private Double currentLandHolding;

    private String currentGeoTag;

    @NotBlank(message = "Current crop is required")
    private String currentCrop;

    @NotNull(message = "Current net income is required")
    @PositiveOrZero(message = "Income must be 0 or positive")
    private Double currentNetIncome;

    private Boolean currentSoilTest;

    @JsonIgnore
    private MultipartFile currentSoilTestCertificate;
    private String currentSoilTestCertificateFileName;
    private String currentSoilTestCertificateUrl;

    // Proposed Crop
    private String proposedSurveyNumber;

    @PositiveOrZero(message = "Proposed land holding must be 0 or positive")
    private Double proposedLandHolding;

    private String proposedGeoTag;
    private String proposedCrop;

    @PositiveOrZero(message = "Proposed net income must be 0 or positive")
    private Double proposedNetIncome;

    private Boolean proposedSoilTest;

    @JsonIgnore
    private MultipartFile proposedSoilTestCertificate;
    private String proposedSoilTestCertificateFileName;
    private String proposedSoilTestCertificateUrl;

    // Irrigation
    private String currentWaterSource;
    private String currentDischargeLPH;
    private String currentSummerDischarge;
    private String currentBorewellLocation;
    private String proposedWaterSource;
    private String proposedDischargeLPH;
    private String proposedSummerDischarge;
    private String proposedBorewellLocation;

    // Bank Details
    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Branch name is required")
    private String branchName;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code")
    private String ifscCode;

    @JsonIgnore
    private MultipartFile passbook;
    private String passbookFileName;
    private String passbookUrl;

    // Document Details
    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @JsonIgnore
    private MultipartFile documentFile;
    private String documentFileName;
    private String documentFileUrl;

    // Portal Access
    @NotBlank(message = "Portal role is required")
    private String portalRole;

    @NotBlank(message = "Portal access is required")
    private String portalAccess;
}
