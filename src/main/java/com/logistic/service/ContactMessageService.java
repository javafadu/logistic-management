package com.logistic.service;

import com.logistic.domain.ContactMessage;
import com.logistic.dto.mapper.ContactMessageMapper;
import com.logistic.dto.request.ContactMessageRequest;
import com.logistic.repository.ContactMessageRepository;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageService {

    private final ContactMessageMapper contactMessageMapper;
    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageMapper contactMessageMapper, ContactMessageRepository contactMessageRepository) {
        this.contactMessageMapper = contactMessageMapper;
        this.contactMessageRepository = contactMessageRepository;
    }

    // Save ContactMessageRequest as ContactMessage
    public void saveMessage(ContactMessageRequest contactMessageRequest) {
        ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
        contactMessageRepository.save(contactMessage);
    }

}
