package com.farmer.Form.Service;

import com.farmer.Form.DTO.PincodeApiResponse.PostOffice;

public interface AddressService {
    PostOffice fetchAddressByPincode(String pincode);
}
