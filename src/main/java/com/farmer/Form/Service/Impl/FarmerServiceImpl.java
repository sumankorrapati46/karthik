package com.farmer.Form.Service.Impl;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.farmer.Form.DTO.FarmerDto;
import com.farmer.Form.Entity.Farmer;
import com.farmer.Form.Repository.FarmerRepository;
import com.farmer.Form.Service.FarmerService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class FarmerServiceImpl implements FarmerService {
 
    @Autowired
    private FarmerRepository farmerRepository;
 
    @Override
    public FarmerDto createFarmer(FarmerDto dto, MultipartFile photo, MultipartFile passbookPhoto,
                                  MultipartFile aadhaar, MultipartFile soilTestCertificate) {
 
        try {
            if (photo != null && !photo.isEmpty()) {
                dto.setPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
            }
 
            if (passbookPhoto != null && !passbookPhoto.isEmpty()) {
                dto.setPassbook(Base64.getEncoder().encodeToString(passbookPhoto.getBytes()));
            }
 
            if (aadhaar != null && !aadhaar.isEmpty()) {
                dto.setDocumentFile(Base64.getEncoder().encodeToString(aadhaar.getBytes()));
            }
 
            if (soilTestCertificate != null && !soilTestCertificate.isEmpty()) {
                dto.setCurrentSoilTestCertificate(Base64.getEncoder().encodeToString(soilTestCertificate.getBytes()));
            }
 
            Farmer farmer = convertToEntity(dto);
            Farmer saved = farmerRepository.save(farmer);
            return convertToDTO(saved);
 
        } catch (IOException e) {
            throw new RuntimeException("Failed to process uploaded files", e);
        }
    }
 
    @Override
    public FarmerDto getFarmerById(Long id) {
        return farmerRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
    }
 
    @Override
    public List<FarmerDto> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
 
    @Override
    public FarmerDto updateFarmer(Long id, FarmerDto dto) {
        Farmer existing = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        Farmer updated = convertToEntity(dto);
        updated.setId(existing.getId());
        return convertToDTO(farmerRepository.save(updated));
    }
 
    @Override
    public FarmerDto updateFarmer(Long id, FarmerDto dto,
                                  MultipartFile photo, MultipartFile passbookPhoto,
                                  MultipartFile aadhaar, MultipartFile soilTestCertificate) {
 
        Farmer existing = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
 
        try {
            if (photo != null && !photo.isEmpty()) {
                dto.setPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
            } else {
                dto.setPhoto(existing.getPhoto());
            }
 
            if (passbookPhoto != null && !passbookPhoto.isEmpty()) {
                dto.setPassbook(Base64.getEncoder().encodeToString(passbookPhoto.getBytes()));
            } else {
                dto.setPassbook(existing.getPassbook());
            }
 
            if (aadhaar != null && !aadhaar.isEmpty()) {
                dto.setDocumentFile(Base64.getEncoder().encodeToString(aadhaar.getBytes()));
            } else {
                dto.setDocumentFile(existing.getDocumentFile());
            }
 
            if (soilTestCertificate != null && !soilTestCertificate.isEmpty()) {
                dto.setCurrentSoilTestCertificate(Base64.getEncoder().encodeToString(soilTestCertificate.getBytes()));
            } else {
                dto.setCurrentSoilTestCertificate(existing.getCurrentSoilTestCertificate());
            }
 
            Farmer updated = convertToEntity(dto);
            updated.setId(id);
            return convertToDTO(farmerRepository.save(updated));
 
        } catch (IOException e) {
            throw new RuntimeException("Failed to process uploaded files during update", e);
        }
    }
 
    @Override
    public void deleteFarmer(Long id) {
        farmerRepository.deleteById(id);
    }
 
    @Override
    public long getFarmerCount() {
        return farmerRepository.count();
    }
 
    private Farmer convertToEntity(FarmerDto dto) {
        return Farmer.builder()
                .id(dto.getId())
                .photo(dto.getPhoto())
                .salutation(dto.getSalutation())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .fatherName(dto.getFatherName())
                .contactNumber(dto.getContactNumber())
                .alternativeRelationType(dto.getAlternativeRelationType())
                .alternativeContactNumber(dto.getAlternativeContactNumber())
                .nationality(dto.getNationality())
                .country(dto.getCountry())
                .state(dto.getState())
                .district(dto.getDistrict())
                .block(dto.getBlock())
                .village(dto.getVillage())
                .pincode(dto.getPincode())
                .education(dto.getEducation())
                .experience(dto.getExperience())
                .cropPhoto(dto.getCropPhoto())
                .currentSurveyNumber(dto.getCurrentSurveyNumber())
                .currentLandHolding(dto.getCurrentLandHolding())
                .currentGeoTag(dto.getCurrentGeoTag())
                .currentCrop(dto.getCurrentCrop())
                .currentNetIncome(dto.getCurrentNetIncome())
                .currentSoilTest(dto.getCurrentSoilTest())
                .currentSoilTestCertificate(dto.getCurrentSoilTestCertificate())
                .proposedSurveyNumber(dto.getProposedSurveyNumber())
                .proposedLandHolding(dto.getProposedLandHolding())
                .proposedGeoTag(dto.getProposedGeoTag())
                .proposedCrop(dto.getProposedCrop())
                .proposedNetIncome(dto.getProposedNetIncome())
                .proposedSoilTest(dto.getProposedSoilTest())
                .proposedSoilTestCertificate(dto.getProposedSoilTestCertificate())
                .currentWaterSource(dto.getCurrentWaterSource())
                .currentDischargeLPH(dto.getCurrentDischargeLPH())
                .currentSummerDischarge(dto.getCurrentSummerDischarge())
                .currentBorewellLocation(dto.getCurrentBorewellLocation())
                .proposedWaterSource(dto.getProposedWaterSource())
                .proposedDischargeLPH(dto.getProposedDischargeLPH())
                .proposedSummerDischarge(dto.getProposedSummerDischarge())
                .proposedBorewellLocation(dto.getProposedBorewellLocation())
                .bankName(dto.getBankName())
                .accountNumber(dto.getAccountNumber())
                .branchName(dto.getBranchName())
                .ifscCode(dto.getIfscCode())
                .passbook(dto.getPassbook())
                .documentType(dto.getDocumentType())
                .documentNumber(dto.getDocumentNumber())
                .documentFile(dto.getDocumentFile())
                .portalRole(dto.getPortalRole())
                .portalAccess(dto.getPortalAccess())
                .build();
    }
 
    private FarmerDto convertToDTO(Farmer farmer) {
        FarmerDto dto = new FarmerDto();
        dto.setId(farmer.getId());
        dto.setPhoto(farmer.getPhoto());
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
        dto.setCropPhoto(farmer.getCropPhoto());
        dto.setCurrentSurveyNumber(farmer.getCurrentSurveyNumber());
        dto.setCurrentLandHolding(farmer.getCurrentLandHolding());
        dto.setCurrentGeoTag(farmer.getCurrentGeoTag());
        dto.setCurrentCrop(farmer.getCurrentCrop());
        dto.setCurrentNetIncome(farmer.getCurrentNetIncome());
        dto.setCurrentSoilTest(farmer.getCurrentSoilTest());
        dto.setCurrentSoilTestCertificate(farmer.getCurrentSoilTestCertificate());
        dto.setProposedSurveyNumber(farmer.getProposedSurveyNumber());
        dto.setProposedLandHolding(farmer.getProposedLandHolding());
        dto.setProposedGeoTag(farmer.getProposedGeoTag());
        dto.setProposedCrop(farmer.getProposedCrop());
        dto.setProposedNetIncome(farmer.getProposedNetIncome());
        dto.setProposedSoilTest(farmer.getProposedSoilTest());
        dto.setProposedSoilTestCertificate(farmer.getProposedSoilTestCertificate());
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
        dto.setPassbook(farmer.getPassbook());
        dto.setDocumentType(farmer.getDocumentType());
        dto.setDocumentNumber(farmer.getDocumentNumber());
        dto.setDocumentFile(farmer.getDocumentFile());
        dto.setPortalRole(farmer.getPortalRole());
        dto.setPortalAccess(farmer.getPortalAccess());
        return dto;
    }
}
 