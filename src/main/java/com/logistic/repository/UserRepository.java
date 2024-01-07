package com.logistic.repository;

import com.logistic.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles") // make eager for default lazy (...ToMany) situation
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id=5")
    List<User> findAdminUsers(int roleId);

    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    @EntityGraph(attributePaths = "roles")
    @Query("SELECT u FROM User u where  lower(u.name) like %:q% OR lower(u.email) like %:q% OR lower(u.phone) like %:q% ")
    Page<User> getSearchedUsersWithPaging(@Param("q") String q, Pageable pageable);


    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);



}
