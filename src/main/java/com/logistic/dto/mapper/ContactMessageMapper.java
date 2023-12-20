package com.logistic.dto.mapper;

import com.logistic.domain.ContactMessage;
import com.logistic.dto.ContactMessageDTO;
import com.logistic.dto.request.ContactMessageRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring") // any class can be injected and be used
public interface ContactMessageMapper {

    // get ContactMessage Pojo class and map to ContactMessageDTO
    ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);

    // get ContactMessageRequest and map to ContactMessage pojo
    @Mapping(target = "id", ignore = true)
    ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);

    @Mapping(target="createDate", source = "createDate")
    List<ContactMessageDTO> contactMessageListToDTOList(List<ContactMessage> contactMessageList);


}
