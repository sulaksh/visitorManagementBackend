package com.example.minor_project.service;

import com.example.minor_project.dto.AddressDto;
import com.example.minor_project.dto.UserDto;
import com.example.minor_project.entity.Address;
import com.example.minor_project.entity.Flat;
import com.example.minor_project.entity.User;
import com.example.minor_project.enums.UserStatus;
import com.example.minor_project.repo.FlatRepo;
import com.example.minor_project.repo.UserRepo;
import com.example.minor_project.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FlatRepo flatRepo;
    @Autowired
    private CommonUtil commonUtil;

    public Long createUser(UserDto userDto){

        AddressDto addressDto = userDto.getAddress();

        Address address = commonUtil.convertAddressDto(addressDto);

        Flat flat = null;

        if(userDto.getFlatNo() != null){
            flat = flatRepo.findByNumber(userDto.getFlatNo());
        }

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .idNumber(userDto.getIdNumber())
                .phone(userDto.getPhone())
                .flat(flat)
                .address(address)
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepo.save(user);
        return user.getId();
    }
}
