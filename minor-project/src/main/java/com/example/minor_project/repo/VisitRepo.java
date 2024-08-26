package com.example.minor_project.repo;

import com.example.minor_project.entity.Flat;
import com.example.minor_project.entity.Visit;
import com.example.minor_project.enums.VisitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitRepo extends JpaRepository<Visit, Long> {

    List<Visit> findByVisitStatusAndFlat(VisitStatus visitStatus, Flat flat);

    Page<Visit> findByVisitStatusAndFlat(VisitStatus visitStatus, Flat flat, Pageable pageable);

    List<Visit> findByVisitStatusAndCreatedDateLessThanEqual(VisitStatus visitStatus, Date date);

}
