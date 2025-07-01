package com.farmer.Form.Mapper;

import com.farmer.Form.DTO.FarmerDTO;
import com.farmer.Form.Entity.Farmer;

public class FarmerMapper {

    // ✅ Entity creation from DTO
    public static Farmer toEntity(FarmerDTO dto, String photoFile, String cropPhoto, String passbook,
                                  String docFile, String currentSoil, String proposedSoil) {

        Farmer farmer = new Farmer();

        farmer.setSalutation(dto.getSalutation());
        farmer.setFirstName(dto.getFirstName());
        farmer.setMiddleName(dto.getMiddleName());
        farmer.setLastName(dto.getLastName());
        farmer.setDateOfBirth(dto.getDateOfBirth());
        farmer.setGender(dto.getGender());
        farmer.setFatherName(dto.getFatherName());
        farmer.setContactNumber(dto.getContactNumber());
        farmer.setAlternativeRelationType(dto.getAlternativeRelationType());
        farmer.setAlternativeContactNumber(dto.getAlternativeContactNumber());
        farmer.setNationality(dto.getNationality());

        farmer.setCountry(dto.getCountry());
        farmer.setState(dto.getState());
        farmer.setDistrict(dto.getDistrict());
        farmer.setBlock(dto.getBlock());
        farmer.setVillage(dto.getVillage());
        farmer.setPincode(dto.getPincode());

        farmer.setEducation(dto.getEducation());
        farmer.setExperience(dto.getExperience());

        farmer.setPhotoFileName(photoFile);
        farmer.setCropPhotoFileName(cropPhoto);
        farmer.setPassbookFileName(passbook);
        farmer.setDocumentFileName(docFile);
        farmer.setCurrentSoilTestCertificateFileName(currentSoil);
        farmer.setProposedSoilTestCertificateFileName(proposedSoil);

        farmer.setCurrentCrop(dto.getCurrentCrop());
        farmer.setCurrentSurveyNumber(dto.getCurrentSurveyNumber());
        farmer.setCurrentLandHolding(dto.getCurrentLandHolding());
        farmer.setCurrentGeoTag(dto.getCurrentGeoTag());
        farmer.setCurrentNetIncome(dto.getCurrentNetIncome());
        farmer.setCurrentSoilTest(dto.getCurrentSoilTest());

        farmer.setProposedCrop(dto.getProposedCrop());
        farmer.setProposedSurveyNumber(dto.getProposedSurveyNumber());
        farmer.setProposedLandHolding(dto.getProposedLandHolding());
        farmer.setProposedGeoTag(dto.getProposedGeoTag());
        farmer.setProposedNetIncome(dto.getProposedNetIncome());
        farmer.setProposedSoilTest(dto.getProposedSoilTest());

        farmer.setCurrentWaterSource(dto.getCurrentWaterSource());
        farmer.setCurrentDischargeLPH(dto.getCurrentDischargeLPH());
        farmer.setCurrentSummerDischarge(dto.getCurrentSummerDischarge());
        farmer.setCurrentBorewellLocation(dto.getCurrentBorewellLocation());

        farmer.setProposedWaterSource(dto.getProposedWaterSource());
        farmer.setProposedDischargeLPH(dto.getProposedDischargeLPH());
        farmer.setProposedSummerDischarge(dto.getProposedSummerDischarge());
        farmer.setProposedBorewellLocation(dto.getProposedBorewellLocation());

        farmer.setBankName(dto.getBankName());
        farmer.setAccountNumber(dto.getAccountNumber());
        farmer.setBranchName(dto.getBranchName());
        farmer.setIfscCode(dto.getIfscCode());

        farmer.setDocumentType(dto.getDocumentType());
        farmer.setDocumentNumber(dto.getDocumentNumber());

        farmer.setPortalRole(dto.getPortalRole());
        farmer.setPortalAccess(dto.getPortalAccess());

        return farmer;
    }

    // ✅ Entity to DTO conversion
    public static FarmerDTO toDTO(Farmer farmer) {
        FarmerDTO dto = new FarmerDTO();

        dto.setId(farmer.getId());
        dto.setSalutation(farmer.getSalutation());
        dto.setFirstName(farmer.getFirstName());
        dto.setMiddleName(farmer.getMiddleName());
        dto.setLastName(farmer.getLastName());
        dto.setDateOfBirth(farmer.getDateOfBirth());
        dto.setGender(farmer.getGender());
        dto.setFatherName(farmer.getFatherName());
        dto.setContactNumber(farmer.getContactNumber());
        dto.setAlternativeRelationType(farmer.getAlternativeRelationType());
        dto.setAlternativeContactNumber(farmer.getAlternativeContactNumber());
        dto.setNationality(farmer.getNationality());

        dto.setCountry(farmer.getCountry());
        dto.setState(farmer.getState());
        dto.setDistrict(farmer.getDistrict());
        dto.setBlock(farmer.getBlock());
        dto.setVillage(farmer.getVillage());
        dto.setPincode(farmer.getPincode());

        dto.setEducation(farmer.getEducation());
        dto.setExperience(farmer.getExperience());

        dto.setPhotoFileName(farmer.getPhotoFileName());
        dto.setCropPhotoFileName(farmer.getCropPhotoFileName());
        dto.setPassbookFileName(farmer.getPassbookFileName());
        dto.setDocumentFileName(farmer.getDocumentFileName());
        dto.setCurrentSoilTestCertificateFileName(farmer.getCurrentSoilTestCertificateFileName());
        dto.setProposedSoilTestCertificateFileName(farmer.getProposedSoilTestCertificateFileName());

        dto.setCurrentCrop(farmer.getCurrentCrop());
        dto.setCurrentSurveyNumber(farmer.getCurrentSurveyNumber());
        dto.setCurrentLandHolding(farmer.getCurrentLandHolding());
        dto.setCurrentGeoTag(farmer.getCurrentGeoTag());
        dto.setCurrentNetIncome(farmer.getCurrentNetIncome());
        dto.setCurrentSoilTest(farmer.getCurrentSoilTest());

        dto.setProposedCrop(farmer.getProposedCrop());
        dto.setProposedSurveyNumber(farmer.getProposedSurveyNumber());
        dto.setProposedLandHolding(farmer.getProposedLandHolding());
        dto.setProposedGeoTag(farmer.getProposedGeoTag());
        dto.setProposedNetIncome(farmer.getProposedNetIncome());
        dto.setProposedSoilTest(farmer.getProposedSoilTest());

        dto.setCurrentWaterSource(farmer.getCurrentWaterSource());
        dto.setCurrentDischargeLPH(farmer.getCurrentDischargeLPH());
        dto.setCurrentSummerDischarge(farmer.getCurrentSummerDischarge());
        dto.setCurrentBorewellLocation(farmer.getCurrentBorewellLocation());

        dto.setProposedWaterSource(farmer.getProposedWaterSource());
        dto.setProposedDischargeLPH(farmer.getProposedDischargeLPH());
        dto.setProposedSummerDischarge(farmer.getProposedSummerDischarge());
        dto.setProposedBorewellLocation(farmer.getProposedBorewellLocation());

        dto.setBankName(farmer.getBankName());
        dto.setAccountNumber(farmer.getAccountNumber());
        dto.setBranchName(farmer.getBranchName());
        dto.setIfscCode(farmer.getIfscCode());

        dto.setDocumentType(farmer.getDocumentType());
        dto.setDocumentNumber(farmer.getDocumentNumber());

        dto.setPortalRole(farmer.getPortalRole());
        dto.setPortalAccess(farmer.getPortalAccess());

        return dto;
    }

    // ✅ Merge fields for update
    public static Farmer merge(Farmer existing, FarmerDTO dto,
                               String photoFile, String cropPhoto, String passbook,
                               String docFile, String currentSoil, String proposedSoil) {
        // (same content you already have — no change needed)
        return existing;
    }
}
