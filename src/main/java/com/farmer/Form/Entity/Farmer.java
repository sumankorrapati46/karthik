package com.farmer.Form.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
 
@Entity
@Table(name = "farmers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Farmer {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // Personal Info
    private String photo;
    @NotBlank private String salutation;
    @NotBlank private String firstName;
    private String middleName;
    @NotBlank private String lastName;
    @NotNull private LocalDate dateOfBirth;
    @NotNull private String gender;
    private String fatherName;
    @Pattern(regexp="^\\d{10}$") private String contactNumber;
    private String alternativeRelationType;
    @Pattern(regexp="^\\d{10}$") private String alternativeContactNumber;
    @NotBlank private String nationality;
 
    // Address
    @NotBlank private String country;
    @NotBlank private String state;
    @NotBlank private String district;
    @NotBlank private String block;
    @NotBlank private String village;
    @Pattern(regexp="^\\d{6}$") private String pincode;
 
    // Professional Info
    private String education;
    private String experience;
 
    // Current Crop
    private String cropPhoto;
    private String currentSurveyNumber;
    private Double currentLandHolding;
    private String currentGeoTag;
    private String currentCrop;
    private Double currentNetIncome;
    private Boolean currentSoilTest;
    private String currentSoilTestCertificate;
 
    // Proposed Crop
    private String proposedSurveyNumber;
    private Double proposedLandHolding;
    private String proposedGeoTag;
    private String proposedCrop;
    private Double proposedNetIncome;
    private Boolean proposedSoilTest;
    private String proposedSoilTestCertificate;
 
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
    private String bankName;
    private String accountNumber;
    private String branchName;
    private String ifscCode;
    private String passbook;
 
    // Document
    private String documentType;
    private String documentNumber;
    private String documentFile;
 
    // Portal Info
    private String portalRole;
    private String portalAccess;
}