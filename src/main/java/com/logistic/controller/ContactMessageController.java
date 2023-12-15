package com.logistic.controller;


import com.logistic.service.ContactMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {

    // @Autowired not use, use below construction injection

    private final ContactMessageService contactMessageService;
    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }



}
