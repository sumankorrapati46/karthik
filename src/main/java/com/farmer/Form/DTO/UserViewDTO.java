package com.farmer.Form.DTO;

import lombok.Data;
 
@Data
public class UserViewDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String dateOfBirth;
    private String city;
    private String role;
    private String status;
 
    public String getName() {
        return (firstName + " " + (lastName != null ? lastName : "")).trim();
    }
}
 
