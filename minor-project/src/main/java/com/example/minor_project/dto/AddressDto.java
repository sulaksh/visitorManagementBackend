package com.example.minor_project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private String line1;

    private String line2;

    private String city;

    private String pincode;
}
