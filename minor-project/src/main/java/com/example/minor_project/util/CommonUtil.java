package com.example.minor_project.util;

import com.example.minor_project.dto.AddressDto;
import com.example.minor_project.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public Address convertAddressDto(AddressDto addressDto){
        return Address.builder()
                .line1(addressDto.getLine1())
                .line2(addressDto.getLine2())
                .city(addressDto.getCity())
                .pincode(addressDto.getPincode())
                .build();
    }
}
