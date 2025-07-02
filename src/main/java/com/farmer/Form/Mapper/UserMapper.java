package com.farmer.Form.Mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.farmer.Form.DTO.UserDTO;
import com.farmer.Form.DTO.UserViewDTO;
import com.farmer.Form.Entity.User;
 
@Mapper(componentModel = "spring")
public interface UserMapper {
 
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "role", ignore = true),
        @Mapping(target = "status", ignore = true),
        @Mapping(target = "dateOfBirth", source = "dateOfBirth") // String to LocalDate
    })
    User toEntity(UserDTO dto);
 
    @Mappings({
        @Mapping(target = "firstName", source = "firstName"),
        @Mapping(target = "lastName", source = "lastName"),
        @Mapping(target = "email", source = "email"),
        @Mapping(target = "mobileNumber", source = "phoneNumber"),
        @Mapping(target = "dateOfBirth", source = "dateOfBirth"), // LocalDate to String
        @Mapping(target = "city", source = "state"),
        @Mapping(target = "role", source = "role"),
        @Mapping(target = "status", source = "status")
    })
    UserViewDTO toViewDto(User user);
}
