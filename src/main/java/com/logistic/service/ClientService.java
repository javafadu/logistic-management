package com.logistic.service;

import com.logistic.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final CustomerRepository customerRepository;


    public ClientService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


}
