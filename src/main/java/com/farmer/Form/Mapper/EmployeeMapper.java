package com.farmer.Form.Mapper;
 
import com.farmer.Form.DTO.EmployeeDTO;
import com.farmer.Form.Entity.Employee;
 
public class EmployeeMapper {
    public static Employee toEntity(EmployeeDTO dto, String photoFile, String passbookFile, String docFile) {
        return Employee.builder()
                .salutation(dto.getSalutation())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .gender(dto.getGender())
                .nationality(dto.getNationality())
                .dob(dto.getDob())
                .contactNumber(dto.getContactNumber())
                .email(dto.getEmail())
                .relationType(dto.getRelationType())
                .relationName(dto.getRelationName())
                .altNumber(dto.getAltNumber())
                .altNumberType(dto.getAltNumberType())
                .country(dto.getCountry())
                .state(dto.getState())
                .district(dto.getDistrict())
                .block(dto.getBlock())
                .village(dto.getVillage())
                .zipcode(dto.getZipcode())
                .sector(dto.getSector())
                .education(dto.getEducation())
                .experience(dto.getExperience())
                .bankName(dto.getBankName())
                .accountNumber(dto.getAccountNumber())
                .branchName(dto.getBranchName())
                .ifscCode(dto.getIfscCode())
                .documentType(dto.getDocumentType())
                .documentNumber(dto.getDocumentNumber())
                .role(dto.getRole())
                .accessStatus(dto.getAccessStatus())
                .photoFileName(photoFile)
                .passbookFileName(passbookFile)
                .documentFileName(docFile)
                .build();
    }
}
 
 