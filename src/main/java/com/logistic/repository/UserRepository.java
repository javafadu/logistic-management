package com.logistic.repository;

import com.logistic.domain.Address;
import com.logistic.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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


    @Modifying // When DML operation with a custom query in JPARepository, we use it
    @Query("UPDATE User u SET u.name=:name, u.email=:email, u.phone=:phone, u.birthDate=:birthDate WHERE u.id=:id")
    void update(@Param("id") Long id,
                @Param("name") String name,
                @Param("email") String email,
                @Param("phone") String phone,
                @Param("birthDate") LocalDate birtDate);

    @EntityGraph(attributePaths = "id") // do not bring roles
    Optional<User> findUserById(Long id);

}
