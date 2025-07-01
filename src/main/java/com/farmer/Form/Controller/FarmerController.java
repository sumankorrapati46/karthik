package com.farmer.Form.Controller;

import com.farmer.Form.DTO.FarmerDTO;
import com.farmer.Form.DTO.PincodeApiResponse.PostOffice;
import com.farmer.Form.Service.AddressService;
import com.farmer.Form.Service.FarmerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

import java.util.List;

@RestController
@RequestMapping("/api/farmers")
@RequiredArgsConstructor
public class FarmerController {

    private final FarmerService farmerService;
    private final AddressService addressService;

    // ✅ Create farmer
    @PostMapping
    public ResponseEntity<?> createFarmer(@ModelAttribute @Valid FarmerDTO dto) {
        try {
            FarmerDTO saved = farmerService.createFarmer(
                dto,
                dto.getPhoto(),
                dto.getCropPhoto(),
                dto.getCurrentSoilTestCertificate(),
                dto.getProposedSoilTestCertificate(),
                dto.getPassbook(),
                dto.getDocumentFile()
            );
            return ResponseEntity.ok(saved);
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().body("❌ File upload failed: " + e.getMessage());
        }
    }

    // ✅ Get all farmers
    @GetMapping
    public ResponseEntity<List<FarmerDTO>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    // ✅ Get farmer by ID
    @GetMapping("/{id}")
    public ResponseEntity<FarmerDTO> getFarmerById(@PathVariable Long id) {
        FarmerDTO dto = farmerService.getFarmerById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // ✅ Update farmer
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFarmer(@PathVariable Long id, @ModelAttribute FarmerDTO dto) {
        try {
            FarmerDTO updated = farmerService.updateFarmer(
                id,
                dto,
                dto.getPhoto(),
                dto.getCropPhoto(),
                dto.getCurrentSoilTestCertificate(),
                dto.getProposedSoilTestCertificate(),
                dto.getPassbook(),
                dto.getDocumentFile()
            );
            return ResponseEntity.ok(updated);
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().body("❌ File update failed: " + e.getMessage());
        }
    }

    // ✅ Delete farmer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFarmer(@PathVariable Long id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.ok("✅ Farmer deleted successfully");
    }

    // ✅ Get address by pincode
    @GetMapping("/address-by-pincode/{pincode}")
    public ResponseEntity<PostOffice> getAddressByPincode(@PathVariable String pincode) {
        PostOffice postOffice = addressService.fetchAddressByPincode(pincode);
        return postOffice != null ? ResponseEntity.ok(postOffice) : ResponseEntity.notFound().build();
    }
}
