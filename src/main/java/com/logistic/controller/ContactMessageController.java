package com.logistic.controller;


import com.logistic.dto.ContactMessageDTO;
import com.logistic.dto.request.ContactMessageRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.ContactMessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<LogiResponse> createMessage(HttpServletRequest request,
                                                      @Valid @RequestBody ContactMessageRequest contactMessageRequest
    ) {

        String remoteAddress = request.getRemoteAddr();
        contactMessageService.saveMessage(contactMessageRequest, remoteAddress);

        LogiResponse response = new LogiResponse(ResponseMessage.CONTACT_MESSAGE_CREATE_RESPONSE, true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // 2- Get All Contact Messages
    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages() {
        List<ContactMessageDTO> contactMessageDTOList = contactMessageService.getAllContactMessagesDTO();
        // return new ResponseEntity<>(contactMessageDTOList, HttpStatus.OK);
        return ResponseEntity.ok(contactMessageDTOList);
    }


    // 3- Get All Contact Messages with Pageable
    @GetMapping("/pages")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessagesWithPages(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop, // order by
            @RequestParam(value = "direction", required = false, defaultValue = "DESC") Sort.Direction direction) {


        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<ContactMessageDTO> contactMessagePages = contactMessageService.getAllContactMessagesWithPages(pageable);

        return ResponseEntity.ok(contactMessagePages);

    }

    // 4- Get a Contact Message with Id (PathVariable)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id) {

        ContactMessageDTO contactMessageDTO = contactMessageService.getContactMessageWithId(id);
        return ResponseEntity.ok(contactMessageDTO);
    }

    // 4- Get a Contact Message with Id (RequestParam)
    @GetMapping("/request")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ContactMessageDTO> getMessageWithRequestParam(@RequestParam("id") Long id) {

        ContactMessageDTO contactMessageDTO = contactMessageService.getContactMessageWithId(id);
        return ResponseEntity.ok(contactMessageDTO);
    }

    // 5- Delete Message
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> deleteMessage(@PathVariable Long id) { // only 1 data
        contactMessageService.deleteMessage(id);
        LogiResponse logiResponse = new LogiResponse(ResponseMessage.CONTACT_MESSAGE_DELETE_RESPONSE, true);
        return ResponseEntity.ok(logiResponse);
    }

    // 6- Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> updateContactMessage(@PathVariable Long id,
                                                             @Valid @RequestBody ContactMessageRequest contactMessageRequest) {
        contactMessageService.updateContactMessage(id, contactMessageRequest);

        LogiResponse logiResponse = new LogiResponse(ResponseMessage.CONTACT_MESSAGE_UPDATE_RESPONSE, true);
        return ResponseEntity.ok(logiResponse);


    }


}
