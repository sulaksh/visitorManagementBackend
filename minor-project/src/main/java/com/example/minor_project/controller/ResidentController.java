package com.example.minor_project.controller;

import com.example.minor_project.dto.AllPendingVisitsDto;
import com.example.minor_project.dto.VisitDto;
import com.example.minor_project.enums.VisitStatus;
import com.example.minor_project.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @PutMapping("actOnVisit/{id}")
    public ResponseEntity<String> actOnVisit(@PathVariable Long id, @RequestParam VisitStatus visitStatus){
        return ResponseEntity.ok(residentService.updateVisit(id,visitStatus));
    }


    @GetMapping("/pendingVisits")
    public ResponseEntity<List<VisitDto>> getPendingVisits(@RequestHeader Long userId){
        return ResponseEntity.ok(residentService.getPendingVisits(userId));
    }

    @GetMapping("/page-pendingVisits")
    public ResponseEntity<AllPendingVisitsDto> getPagePendingVisits(@RequestHeader Long userId,
                                                                    @RequestParam Integer pageNo,
                                                                    @RequestParam Integer pageSize){
        return ResponseEntity.ok(residentService.getPendingVisitsByPage(userId, pageNo, pageSize));
    }
}

