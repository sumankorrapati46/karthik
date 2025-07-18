package com.farmer.Form.Controller;
 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.security.core.Authentication;
 
import com.farmer.Form.DTO.EmployeeDTO;
import com.farmer.Form.DTO.PincodeApiResponse.PostOffice;
import com.farmer.Form.Entity.Employee;
import com.farmer.Form.Mapper.EmployeeMapper;
import com.farmer.Form.Service.AddressService;
import com.farmer.Form.Service.EmployeeService;
import com.farmer.Form.Service.FileStorageService;
import com.farmer.Form.Entity.Farmer;
import com.farmer.Form.Service.FarmerService;
 
import java.io.IOException;
import java.util.List;
 
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
 
    private final EmployeeService employeeService;
    private final AddressService addressService;
    private final FileStorageService fileStorageService;
    private final FarmerService farmerService;
 
    // ✅ Create employee with file upload
    @PostMapping
    public ResponseEntity<?> createEmployee(@ModelAttribute @Valid EmployeeDTO dto) {
        try {
            String photoFile = fileStorageService.storeFile(dto.getPhoto(), "photos");
            String passbookFile = fileStorageService.storeFile(dto.getPassbook(), "passbooks");
            String docFile = fileStorageService.storeFile(dto.getDocumentFile(), "documents");
 
            Employee employee = EmployeeMapper.toEntity(dto, photoFile, passbookFile, docFile);
            Employee saved = employeeService.saveEmployee(employee);
            return ResponseEntity.ok(saved);
 
        } catch (IOException | MultipartException e) {
            return ResponseEntity.badRequest().body("❌ File upload failed: " + e.getMessage());
        }
    }
 
    // ✅ Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
 
    // ✅ Get one employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }
 
    // ✅ Update employee with file support
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @ModelAttribute EmployeeDTO dto) {
        try {
            Employee existing = employeeService.getEmployeeById(id);
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }
 
            String photoFile = dto.getPhoto() != null ? fileStorageService.storeFile(dto.getPhoto(), "photos") : existing.getPhotoFileName();
            String passbookFile = dto.getPassbook() != null ? fileStorageService.storeFile(dto.getPassbook(), "passbooks") : existing.getPassbookFileName();
            String docFile = dto.getDocumentFile() != null ? fileStorageService.storeFile(dto.getDocumentFile(), "documents") : existing.getDocumentFileName();
 
            Employee updated = EmployeeMapper.toEntity(dto, photoFile, passbookFile, docFile);
            updated.setId(id);
            Employee saved = employeeService.saveEmployee(updated);
 
            return ResponseEntity.ok(saved);
 
        } catch (IOException | MultipartException e) {
            return ResponseEntity.badRequest().body("❌ File update failed: " + e.getMessage());
        }
    }
 
    // ✅ Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("✅ Employee deleted successfully");
    }
 
    // ✅ Get address by pincode (tested endpoint)
    @GetMapping("/address-by-pincode/{pincode}")
    public ResponseEntity<PostOffice> getAddressByPincode(@PathVariable String pincode) {
        System.out.println("📍 Pincode requested: " + pincode); // Debug log
        PostOffice postOffice = addressService.fetchAddressByPincode(pincode);
        return postOffice != null ? ResponseEntity.ok(postOffice) : ResponseEntity.notFound().build();
    }

    // View all farmers assigned to the logged-in employee
    @GetMapping("/assigned-farmers")
    public ResponseEntity<List<Farmer>> getAssignedFarmers(Authentication authentication) {
        String employeeEmail = authentication.getName();
        List<Farmer> assignedFarmers = farmerService.getFarmersByEmployeeEmail(employeeEmail);
        return ResponseEntity.ok(assignedFarmers);
    }

    // Approve KYC for a farmer
    @PutMapping("/approve-kyc/{farmerId}")
    public ResponseEntity<String> approveKyc(@PathVariable Long farmerId) {
        farmerService.approveKyc(farmerId);
        return ResponseEntity.ok("KYC approved for farmer");
    }
}
 
 