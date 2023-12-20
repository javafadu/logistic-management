package com.logistic.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ContactMessageDTO {

    private Long id;

    private String name;

    private String email;

    private String subject;

    private String body;

    private LocalDateTime createDate;

    private String remoteAddress;



}
