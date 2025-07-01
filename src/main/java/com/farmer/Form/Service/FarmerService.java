package com.farmer.Form.Service;

import com.farmer.Form.DTO.FarmerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FarmerService {

    /**
     * Create a new farmer with uploaded files.
     *
     * @param dto Farmer data transfer object
     * @param photo Farmer photo
     * @param cropPhoto Crop photo
     * @param currentSoilTestCertificate Soil test certificate for current crop
     * @param proposedSoilTestCertificate Soil test certificate for proposed crop
     * @param passbook Bank passbook file
     * @param documentFile ID/document file
     * @return Created farmer DTO
     */
    FarmerDTO createFarmer(
            FarmerDTO dto,
            MultipartFile photo,
            MultipartFile cropPhoto,
            MultipartFile currentSoilTestCertificate,
            MultipartFile proposedSoilTestCertificate,
            MultipartFile passbook,
            MultipartFile documentFile
    );

    /**
     * Get farmer by ID.
     */
    FarmerDTO getFarmerById(Long id);

    /**
     * Get list of all farmers.
     */
    List<FarmerDTO> getAllFarmers();

    /**
     * Update farmer details with optional file updates.
     *
     * @param id Farmer ID to update
     * @param dto Updated DTO data
     * @param photo Optional new photo
     * @param cropPhoto Optional new crop photo
     * @param currentSoilTestCertificate Optional new soil test certificate (current)
     * @param proposedSoilTestCertificate Optional new soil test certificate (proposed)
     * @param passbook Optional new passbook
     * @param documentFile Optional new document
     * @return Updated farmer DTO
     */
    FarmerDTO updateFarmer(
            Long id,
            FarmerDTO dto,
            MultipartFile photo,
            MultipartFile cropPhoto,
            MultipartFile currentSoilTestCertificate,
            MultipartFile proposedSoilTestCertificate,
            MultipartFile passbook,
            MultipartFile documentFile
    );

    /**
     * Delete farmer by ID.
     */
    void deleteFarmer(Long id);
}
