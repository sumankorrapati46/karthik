package com.farmer.Form.Service.Impl;

import com.farmer.Form.DTO.PincodeApiResponse;
import com.farmer.Form.DTO.PincodeApiResponse.PostOffice;
import com.farmer.Form.Service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PostOffice fetchAddressByPincode(String pincode) {
        try {
            String url = "https://api.postalpincode.in/pincode/" + pincode;
            String response = restTemplate.getForObject(url, String.class);

            PincodeApiResponse[] parsedArray = objectMapper.readValue(response, PincodeApiResponse[].class);

            if (parsedArray != null && parsedArray.length > 0) {
                PincodeApiResponse firstEntry = parsedArray[0];
                List<PostOffice> postOffices = firstEntry.getPostOffice();

                if (postOffices != null && !postOffices.isEmpty()) {
                    return postOffices.get(0); // ✅ return first post office
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error while fetching address by pincode: " + e.getMessage());
            e.printStackTrace(); // for debugging
        }

        return null;
    }
}
