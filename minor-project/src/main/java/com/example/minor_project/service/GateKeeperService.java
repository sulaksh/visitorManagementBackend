package com.example.minor_project.service;

import com.example.minor_project.dto.AddressDto;
import com.example.minor_project.dto.VisitDto;
import com.example.minor_project.dto.VisitorDto;
import com.example.minor_project.entity.Address;
import com.example.minor_project.entity.Flat;
import com.example.minor_project.entity.Visit;
import com.example.minor_project.entity.Visitor;
import com.example.minor_project.enums.VisitStatus;
import com.example.minor_project.exceptions.BadRequestException;
import com.example.minor_project.exceptions.NotFoundException;
import com.example.minor_project.repo.FlatRepo;
import com.example.minor_project.repo.VisitRepo;
import com.example.minor_project.repo.VisitorRepo;
import com.example.minor_project.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class GateKeeperService {
    @Autowired
    private VisitorRepo visitorRepo;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private FlatRepo flatRepo;

    @Autowired
    private VisitRepo visitRepo;

    public VisitorDto getVisitor(String idNumber){
        Visitor visitor = visitorRepo.findByIdNumber(idNumber);

        VisitorDto visitorDto = null;
        if(visitor != null){
            visitorDto = VisitorDto.builder()
                    .name(visitor.getName())
                    .email(visitor.getEmail())
                    .phone(visitor.getPhone())
                    .idNumber((visitor.getIdNumber()))
                    .build();
        }
        return visitorDto;
    }

    public Long createVisitor(VisitorDto visitorDto){
        AddressDto addressDto = visitorDto.getAddress();

        Address address = commonUtil.convertAddressDto(addressDto);

        Visitor visitor = Visitor.builder()
                .name(visitorDto.getName())
                .phone(visitorDto.getPhone())
                .email(visitorDto.getEmail())
                .idNumber(visitorDto.getIdNumber())
                .address(address)
                .build();

        visitor = visitorRepo.save(visitor);
        return visitor.getId();
    }

    public Long createVisit(VisitDto visitDto) {

        Flat flat = flatRepo.findByNumber(visitDto.getFlatNumber());
        Visitor visitor = visitorRepo.findByIdNumber(visitDto.getIdNumber());
        Visit visit = Visit.builder()
                .flat(flat)
                .imageUrl(visitDto.getUrlOfImage())
                .noOfPeople(visitDto.getNoOfPeople())
                .purpose(visitDto.getPurpose())
                .visitStatus(VisitStatus.WAITING)
                .visitor(visitor)
                .build();

        visit = visitRepo.save(visit);
        return visit.getId();
    }

    @Transactional
    public String markEntry(Long id){

        Optional<Visit> visitOptional = visitRepo.findById(id);
        if(visitOptional.isEmpty()){
            throw new NotFoundException("Visit not found!");
        }

        Visit visit = visitOptional.get();

        if(visit.getVisitStatus().equals(VisitStatus.APPROVED)){
            visit.setInTime(new Date());
        }
        else{
            throw new BadRequestException("Invalid state transition!");
        }
        return "Done";
    }

    @Transactional
    public String markExit(Long id) {
        Visit visit = visitRepo.findById(id).get();

        if(visit == null){
            throw new NotFoundException("Visit not found!");
        }

        if(visit.getVisitStatus().equals(VisitStatus.APPROVED) && visit.getInTime() != null){
            visit.setOutTime(new Date());
            visit.setVisitStatus(VisitStatus.COMPLETED);
        }
        else{
            throw new BadRequestException("Invalid state transition!");
        }
        return "Done";
    }
}
