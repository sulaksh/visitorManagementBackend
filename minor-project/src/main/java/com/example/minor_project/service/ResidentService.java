package com.example.minor_project.service;

import com.example.minor_project.dto.AllPendingVisitsDto;
import com.example.minor_project.dto.VisitDto;
import com.example.minor_project.entity.Flat;
import com.example.minor_project.entity.User;
import com.example.minor_project.entity.Visit;
import com.example.minor_project.entity.Visitor;
import com.example.minor_project.enums.VisitStatus;
import com.example.minor_project.exceptions.BadRequestException;
import com.example.minor_project.exceptions.NotFoundException;
import com.example.minor_project.repo.UserRepo;
import com.example.minor_project.repo.VisitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResidentService {

    @Autowired
    private VisitRepo visitRepo;
    @Autowired
    private UserRepo userRepo;

    public String updateVisit(Long id, VisitStatus visitStatus){

        if(visitStatus != VisitStatus.APPROVED && visitStatus != VisitStatus.REJECTED){
            throw new BadRequestException("Invalid state transition!");
        }

        Visit visit = visitRepo.findById(id).get();

        if(visit == null){
            throw new NotFoundException("Visit Not Found!");
        }

        if(VisitStatus.WAITING.equals(visit.getVisitStatus())){
            visit.setVisitStatus(visitStatus);
            visitRepo.save(visit);
        }
        else{
            throw new BadRequestException("Invalid state transition!");
        }

        return "Done";

    }

    public List<VisitDto> getPendingVisits(Long userId){
        User user = userRepo.findById(userId).get();
        Flat flat = user.getFlat();
        List<Visit> visitList = visitRepo.findByVisitStatusAndFlat(VisitStatus.WAITING, flat);

        List<VisitDto> visitDtoList = new ArrayList<>();

        for(Visit visit : visitList){

            Visitor visitor = visit.getVisitor();
            if (visitor == null) {
                // Skip or handle the visit if the visitor is null
                continue; // or you can log a warning, or handle it in another way
            }
            VisitDto visitDto = VisitDto.builder()
                    .flatNumber(flat.getNumber())
                    .purpose(visit.getPurpose())
                    .noOfPeople(visit.getNoOfPeople())
                    .urlOfImage(visit.getImageUrl())
                    .visitorName(visitor.getName())
                    .visitorPhone(visitor.getPhone())
                    .status(visit.getVisitStatus())
                    .idNumber(visitor.getIdNumber())
                    .build();

            visitDtoList.add(visitDto);
        }

        return  visitDtoList;

    }

    public AllPendingVisitsDto getPendingVisitsByPage(Long userId, Integer pageNo, Integer pageSize){
        AllPendingVisitsDto allPendingVisitsDto = new AllPendingVisitsDto();
        List<VisitDto> visitDtoList = new ArrayList<>();
        User user = userRepo.findById(userId).get();
        Flat flat = user.getFlat();
//        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNo);
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Visit> visitPage = visitRepo.findByVisitStatusAndFlat(VisitStatus.WAITING, flat, pageable);
        List<Visit> visitList = visitPage.stream().toList();
        for(Visit visit : visitList){

            Visitor visitor = visit.getVisitor();
            if (visitor == null) {
                // Skip or handle the visit if the visitor is null
                continue; // or you can log a warning, or handle it in another way
            }
            VisitDto visitDto = VisitDto.builder()
                    .flatNumber(flat.getNumber())
                    .purpose(visit.getPurpose())
                    .noOfPeople(visit.getNoOfPeople())
                    .urlOfImage(visit.getImageUrl())
                    .visitorName(visitor.getName())
                    .visitorPhone(visitor.getPhone())
                    .status(visit.getVisitStatus())
                    .idNumber(visitor.getIdNumber())
                    .build();

            visitDtoList.add(visitDto);
        }
        allPendingVisitsDto.setVisits(visitDtoList);
        allPendingVisitsDto.setTotalPages(visitPage.getTotalPages());
        allPendingVisitsDto.setTotalRows(visitPage.getTotalElements());
        return allPendingVisitsDto;
    }
}
