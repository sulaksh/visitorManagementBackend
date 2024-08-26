package com.example.minor_project.repo;

import com.example.minor_project.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatRepo extends JpaRepository<Flat, Long> {
    Flat findByNumber(String number);
}
