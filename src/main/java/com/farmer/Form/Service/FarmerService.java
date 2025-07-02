package com.farmer.Form.Service;


import org.springframework.web.multipart.MultipartFile;

import com.farmer.Form.DTO.FarmerDto;

import java.util.List;
 
public interface FarmerService {
 
    FarmerDto createFarmer(FarmerDto dto,
                           MultipartFile photo,
                           MultipartFile passbookPhoto,
                           MultipartFile aadhaar,
                           MultipartFile soilTestCertificate);
 
    FarmerDto updateFarmer(Long id,
                           FarmerDto dto,
                           MultipartFile photo,
                           MultipartFile passbookPhoto,
                           MultipartFile aadhaar,
                           MultipartFile soilTestCertificate);
 
    FarmerDto updateFarmer(Long id, FarmerDto dto);
 
    FarmerDto getFarmerById(Long id);
 
    List<FarmerDto> getAllFarmers();
 
    void deleteFarmer(Long id);
 
    long getFarmerCount(); // ✅ Add this
}
 