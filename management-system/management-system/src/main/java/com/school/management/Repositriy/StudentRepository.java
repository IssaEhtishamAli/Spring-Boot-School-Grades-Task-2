package com.school.management.Repositriy;

import com.school.management.Models.Student.mStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<mStudent, Long> {
    Optional<mStudent> findByEmail(String email);
}


