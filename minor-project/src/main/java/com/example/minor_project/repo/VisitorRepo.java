package com.example.minor_project.repo;

import com.example.minor_project.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepo extends JpaRepository<Visitor, Long> {
    Visitor findByIdNumber(String idNumber);
}
