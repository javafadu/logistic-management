package com.logistic.repository;

import com.logistic.domain.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Optional
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {



}
