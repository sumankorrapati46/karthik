package com.farmer.Form.Service.Impl;

import com.farmer.Form.DTO.FarmerDTO;
import com.farmer.Form.Entity.Farmer;
import com.farmer.Form.Mapper.FarmerMapper;
import com.farmer.Form.Repository.FarmerRepository;
import com.farmer.Form.Service.FarmerService;
import com.farmer.Form.Service.FileStorageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final FileStorageService fileStorageService;

    @Override
    public FarmerDTO createFarmer(FarmerDTO dto, MultipartFile photo, MultipartFile cropPhoto,
                                  MultipartFile currentSoilTestCertificate, MultipartFile proposedSoilTestCertificate,
                                  MultipartFile passbook, MultipartFile documentFile) {
        try {
            String photoPath = fileStorageService.storeFile(photo, "photos");
            String cropPhotoPath = fileStorageService.storeFile(cropPhoto, "crops");
            String currentSoilPath = fileStorageService.storeFile(currentSoilTestCertificate, "soil-certificates");
            String proposedSoilPath = fileStorageService.storeFile(proposedSoilTestCertificate, "soil-certificates");
            String passbookPath = fileStorageService.storeFile(passbook, "passbooks");
            String documentPath = fileStorageService.storeFile(documentFile, "documents");

            Farmer farmer = FarmerMapper.toEntity(dto, photoPath, cropPhotoPath, passbookPath, documentPath, currentSoilPath, proposedSoilPath);
            Farmer saved = farmerRepository.save(farmer);
            return FarmerMapper.toDTO(saved);
        } catch (IOException e) {
            throw new RuntimeException("File upload error: " + e.getMessage());
        }
    }

    @Override
    public FarmerDTO getFarmerById(Long id) {
        return farmerRepository.findById(id)
                .map(FarmerMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
    }

    @Override
    public List<FarmerDTO> getAllFarmers() {
        return farmerRepository.findAll().stream()
                .map(FarmerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FarmerDTO updateFarmer(Long id, FarmerDTO dto, MultipartFile photo, MultipartFile cropPhoto,
                                  MultipartFile currentSoilTestCertificate, MultipartFile proposedSoilTestCertificate,
                                  MultipartFile passbook, MultipartFile documentFile) {
        Farmer existing = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        try {
            String photoPath = (photo != null) ? fileStorageService.storeFile(photo, "photos") : existing.getPhotoFileName();
            String cropPhotoPath = (cropPhoto != null) ? fileStorageService.storeFile(cropPhoto, "crops") : existing.getCropPhotoFileName();
            String currentSoilPath = (currentSoilTestCertificate != null) ? fileStorageService.storeFile(currentSoilTestCertificate, "soil-certificates") : existing.getCurrentSoilTestCertificateFileName();
            String proposedSoilPath = (proposedSoilTestCertificate != null) ? fileStorageService.storeFile(proposedSoilTestCertificate, "soil-certificates") : existing.getProposedSoilTestCertificateFileName();
            String passbookPath = (passbook != null) ? fileStorageService.storeFile(passbook, "passbooks") : existing.getPassbookFileName();
            String documentPath = (documentFile != null) ? fileStorageService.storeFile(documentFile, "documents") : existing.getDocumentFileName();

            Farmer updated = FarmerMapper.toEntity(dto, photoPath, cropPhotoPath, passbookPath, documentPath, currentSoilPath, proposedSoilPath);
            updated.setId(id);

            Farmer saved = farmerRepository.save(updated);
            return FarmerMapper.toDTO(saved);
        } catch (IOException e) {
            throw new RuntimeException("File update error: " + e.getMessage());
        }
    }

    @Override
    public void deleteFarmer(Long id) {
        farmerRepository.deleteById(id);
    }
}
