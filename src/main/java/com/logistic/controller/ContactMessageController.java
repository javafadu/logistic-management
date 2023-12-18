package com.logistic.controller;


import com.logistic.dto.ContactMessageDTO;
import com.logistic.dto.request.ContactMessageRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {

    // @Autowired not use (field injection)
    // use below construction injection

    private final ContactMessageService contactMessageService;
    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }


    // 1- Create Contact Message
    @PostMapping("/visitors")
    public ResponseEntity<LogiResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {

        contactMessageService.saveMessage(contactMessageRequest);

        LogiResponse response = new LogiResponse("Contact Message successfully created", true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // 2- Get All Contact Messages
    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages() {
        List<ContactMessageDTO> contactMessageDTOList = contactMessageService.getAllContactMessages();
        // return new ResponseEntity<>(contactMessageDTOList, HttpStatus.OK);
        return ResponseEntity.ok(contactMessageDTOList);
    }



}
