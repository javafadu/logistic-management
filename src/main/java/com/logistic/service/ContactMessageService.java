package com.logistic.service;

import com.logistic.domain.ContactMessage;
import com.logistic.dto.ContactMessageDTO;
import com.logistic.dto.mapper.ContactMessageMapper;
import com.logistic.dto.request.ContactMessageRequest;
import com.logistic.repository.ContactMessageRepository;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Get All Contact Messages
    public List<ContactMessageDTO> getAllContactMessages() {
        List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
        return contactMessageMapper.contactMessageListToDTOList(contactMessageList);
    }


    // Get All Contact Messages with Page
    public Page<ContactMessageDTO> getAllContactMessagesWithPages(Pageable pageable) {

            Page<ContactMessage> contactMessagePage = contactMessageRepository.findAll(pageable);
            Page<ContactMessageDTO> pageDTO = getPageDTO(contactMessagePage);

            return pageDTO;
    }


    // convert Page<ContactMessage> to Page<ContactMessageDTO>
    private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage) {
        return contactMessagePage.map(
                contactMessage -> contactMessageMapper.contactMessageToDTO(contactMessage)
        );
    }


}
