package com.farmer.Form.Controller;

import com.farmer.Form.DTO.FarmerDto;
import com.farmer.Form.DTO.PincodeApiResponse.PostOffice;
import com.farmer.Form.Service.AddressService;
import com.farmer.Form.Service.FarmerService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/farmers")
public class FarmerController {
 
    private final FarmerService service;
    private final ObjectMapper objectMapper;
 
    public FarmerController(FarmerService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }
 
    // ✅ Create farmer with multipart/form-data
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FarmerDto> createFarmer(
            @RequestPart("farmerDto") String farmerDtoJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "passbookPhoto", required = false) MultipartFile passbookPhoto,
            @RequestPart(value = "aadhaar", required = false) MultipartFile aadhaar,
            @RequestPart(value = "soilTestCertificate", required = false) MultipartFile soilTestCertificate
    ) throws JsonProcessingException {
 
        FarmerDto farmerDTO = objectMapper.readValue(farmerDtoJson, FarmerDto.class);
 
        // ✅ Call the correct method
        FarmerDto createdFarmer = service.createFarmer(
                farmerDTO,
                photo,
                passbookPhoto,
                aadhaar,
                soilTestCertificate
        );
 
        return ResponseEntity.ok(createdFarmer);
    }
 
    // ✅ Get farmer by ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmerDto> getFarmerById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFarmerById(id));
    }
 
    // ✅ Get all farmers
    @GetMapping
    public ResponseEntity<List<FarmerDto>> getAllFarmers() {
        return ResponseEntity.ok(service.getAllFarmers());
    }
 
    // ✅ Update farmer
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FarmerDto> updateFarmer(
            @PathVariable Long id,
            @RequestPart("farmerDto") String farmerDtoJson,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "passbookPhoto", required = false) MultipartFile passbookPhoto,
            @RequestPart(value = "aadhaar", required = false) MultipartFile aadhaar,
            @RequestPart(value = "soilTestCertificate", required = false) MultipartFile soilTestCertificate
    ) throws JsonProcessingException {
 
        FarmerDto farmerDTO = objectMapper.readValue(farmerDtoJson, FarmerDto.class);
 
        FarmerDto updatedFarmer = service.updateFarmer(
                id,
                farmerDTO,
                photo,
                passbookPhoto,
                aadhaar,
                soilTestCertificate
        );
 
        return ResponseEntity.ok(updatedFarmer);
    }
 
 
    
}
