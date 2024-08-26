package com.example.minor_project.util;

import com.example.minor_project.controller.AdminController;
import com.example.minor_project.entity.Visit;
import com.example.minor_project.enums.VisitStatus;
import com.example.minor_project.repo.VisitRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Configuration
public class VisitExpireScheduleTask {

    private Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private VisitRepo visitRepo;

    @Scheduled(fixedDelay = 5000)
    public void markVisitAsExpired(){
        LOGGER.info("Marking visit as expired!");

        Date date = new Date();
        date.setMinutes(date.getMinutes() - 30);

        List<Visit> visitList = visitRepo.findByVisitStatusAndCreatedDateLessThanEqual(VisitStatus.WAITING, date);

        for(Visit visit : visitList){
            visit.setVisitStatus(VisitStatus.EXPIRED);
        }

        visitRepo.saveAll(visitList);

    }
}
