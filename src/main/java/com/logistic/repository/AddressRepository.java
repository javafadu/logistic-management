package com.logistic.repository;

import com.logistic.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    @Modifying // When DML operation with a custom query in JPARepository, we use it
    @Query("UPDATE Address a SET a.type=:type, a.country=:country, a.state=:state, a.city=:city, a.district=:district, a.zipCode=:zipCode, a.address=:address, a.location.latitude=:latitude, a.location.longitude=:longitude WHERE a.id=:id")
    void updateUserAddress(@Param("id") Long id,
                @Param("type") String type, @Param("country") String country,
                @Param("state") String state,
                @Param("city") String city,
                @Param("district") String district,
                @Param("zipCode") String zipCode,
                @Param("address") String address,
                @Param("latitude") Double latitude,
                @Param("longitude") Double longitude);

    @Query("SELECT a.id FROM Address a WHERE a.user.id=:id")
    List<Long> userAddressIds(@Param("id") Long id);

    @Query("SELECT a from Address a INNER JOIN User u ON a.user.id = u.id WHERE u.id=:userId")
    List<Address> userAddresses(@Param("userId") Long userId);

}
