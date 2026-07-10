package com.onboarding.system.repositories;

import com.onboarding.system.models.Department;
import com.onboarding.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {


}