package com.logistic.repository;

import com.logistic.domain.Role;
import com.logistic.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByType(RoleType type);
    boolean existsByType(RoleType type);


}
