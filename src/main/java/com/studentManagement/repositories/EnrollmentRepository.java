package com.studentManagement.repositories;

import com.studentManagement.models.Enrollment;
import com.studentManagement.models.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
}
