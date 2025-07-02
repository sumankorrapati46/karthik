package com.farmer.Form.DTO;

import lombok.Data;
import java.time.LocalDate;
 
@Data
public class FarmerDto {
    private Long id;
    // Include all fields from Farmer...
    private String photo;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String fatherName;
    private String contactNumber;
    private String alternativeRelationType;
    private String alternativeContactNumber;
    private String nationality;
    private String country;
    private String state;
    private String district;
    private String block;
    private String village;
    private String pincode;
    private String education;
    private String experience;
    private String cropPhoto;
    private String currentSurveyNumber;
    private Double currentLandHolding;
    private String currentGeoTag;
    private String currentCrop;
    private Double currentNetIncome;
    private Boolean currentSoilTest;
    private String currentSoilTestCertificate;
    private String proposedSurveyNumber;
    private Double proposedLandHolding;
    private String proposedGeoTag;
    private String proposedCrop;
    private Double proposedNetIncome;
    private Boolean proposedSoilTest;
    private String proposedSoilTestCertificate;
    private String currentWaterSource;
    private String currentDischargeLPH;
    private String currentSummerDischarge;
    private String currentBorewellLocation;
    private String proposedWaterSource;
    private String proposedDischargeLPH;
    private String proposedSummerDischarge;
    private String proposedBorewellLocation;
    private String bankName;
    private String accountNumber;
    private String branchName;
    private String ifscCode;
    private String passbook;
    private String documentType;
    private String documentNumber;
    private String documentFile;
    private String portalRole;
    private String portalAccess;
}