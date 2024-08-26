package com.example.minor_project.controller;


import com.example.minor_project.dto.VisitDto;
import com.example.minor_project.dto.VisitorDto;
import com.example.minor_project.service.GateKeeperService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/gatekeeper")
public class GateKeeperController {

    @Autowired
    private GateKeeperService gateKeeperService;

    @Value("${static.domain.name}")
    private String staticDomainName;

    @Value("${image.upload.home}")
    private String imageUploadHome;


    private Logger LOGGER = LoggerFactory.getLogger(GateKeeperController.class);

    @GetMapping("/getVisitor")
    ResponseEntity<VisitorDto> getVisitorByIdNumber(@RequestParam String idNumber){
        VisitorDto visitorDto = gateKeeperService.getVisitor(idNumber);
        if(visitorDto == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visitorDto);
    }

    @PostMapping("/createVisitor")
    ResponseEntity<Long> createVisitor(@RequestBody @Valid VisitorDto visitorDto){
        Long id = gateKeeperService.createVisitor(visitorDto);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/createVisit")
    ResponseEntity<Long> createVisit(@RequestBody @Valid VisitDto visitDto){
        Long id = gateKeeperService.createVisit(visitDto);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/markEntry/{id}")
    ResponseEntity<String> markEntry(@PathVariable Long id){
        return ResponseEntity.ok(gateKeeperService.markEntry(id));
    }

    @PutMapping("/markExit/{id}")
    ResponseEntity<String> markExit(@PathVariable Long id){
        return ResponseEntity.ok(gateKeeperService.markExit(id));
    }

    @PostMapping("/image-upload")
    public ResponseEntity<String> imageUpload(@RequestParam MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String uploadPath = imageUploadHome + fileName;
        String publicUrl = staticDomainName + "content/" + fileName;
        File directory = new File(imageUploadHome);
        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory if it doesn't exist
        }
        try {
            file.transferTo(new File(uploadPath));
        } catch (IOException e) {
            LOGGER.error("Exception while uploading image: ", e);
            return ResponseEntity.status(500).body("Exception while uploading image!");
        }
        LOGGER.info("File uploaded to: " + uploadPath);
        LOGGER.info("Public URL: " + publicUrl);

        return ResponseEntity.ok(publicUrl);
    }



}