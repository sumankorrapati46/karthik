package com.farmer.Form.DTO;

import lombok.Data;
 
@Data
public class CountryResponseDTO {
    private String name;
    private String iso2;      // Maps to shortName
    private String phonecode; // Maps to dialCode
}
 