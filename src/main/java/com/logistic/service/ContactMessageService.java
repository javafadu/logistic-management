package com.logistic.service;

import com.logistic.domain.ContactMessage;
import com.logistic.dto.ContactMessageDTO;
import com.logistic.dto.mapper.ContactMessageMapper;
import com.logistic.dto.request.ContactMessageRequest;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.ContactMessageRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactMessageService {

    private final ContactMessageMapper contactMessageMapper;
    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageMapper contactMessageMapper, ContactMessageRepository contactMessageRepository) {
        this.contactMessageMapper = contactMessageMapper;
        this.contactMessageRepository = contactMessageRepository;
    }

    // Save ContactMessageRequest as ContactMessage
    public void saveMessage(ContactMessageRequest contactMessageRequest, String remoteAddress) {
        ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
        LocalDateTime today = LocalDateTime.now();
        contactMessage.setRemoteAddress(remoteAddress);
        contactMessage.setCreateDate(today);
        contactMessageRepository.save(contactMessage);
    }

    // Get All Contact Messages
    public List<ContactMessageDTO> getAllContactMessagesDTO() {

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


    public ContactMessageDTO getContactMessageWithId(Long id) {
       ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(()->
               new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return contactMessageMapper.contactMessageToDTO(contactMessage);
    }

    public void deleteMessage(Long id) {
        ContactMessageDTO contactMessageDTO =  getContactMessageWithId(id);
        contactMessageRepository.deleteById(id);
    }

    public void updateContactMessage(Long id, ContactMessageRequest contactMessageRequest) {
        ContactMessageDTO foundContactMessageDTO = getContactMessageWithId(id);

        foundContactMessageDTO.setName(contactMessageRequest.getName());
        foundContactMessageDTO.setBody(contactMessageRequest.getBody());
        foundContactMessageDTO.setSubject(contactMessageRequest.getSubject());
        foundContactMessageDTO.setEmail(contactMessageRequest.getEmail());

        ContactMessage updatedContactMessage = contactMessageMapper.contactMessageDTOToContactMessage(foundContactMessageDTO);
        contactMessageRepository.save(updatedContactMessage);
    }
}
