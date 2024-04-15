package com.logistic.service;

import com.logistic.domain.Company;
import com.logistic.domain.User;
import com.logistic.dto.CompanyDTO;
import com.logistic.dto.mapper.CompanyMapper;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;


    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    // save company
    public void companyAdd(CompanyDTO companyDTO) {
        Company company = companyMapper.companyDTOToCompany(companyDTO);
        LocalDateTime today = LocalDateTime.now();
        company.setRegisterDate(today);

        companyRepository.save(company);
    }

    // update company
    public void updateCompany(Long companyID, CompanyDTO companyDTO) {
        //check1 : company exist or not
        Company company = getById(companyID);

        company.setName(companyDTO.getName());
       //  check these if empty or not taxOffice; taxNo; phone; email; webSite;
        company.setTaxOffice(ObjectUtils.isEmpty(companyDTO.getTaxOffice()) ? company.getTaxOffice() : companyDTO.getTaxOffice());
        company.setTaxNo(ObjectUtils.isEmpty(companyDTO.getTaxNo()) ? company.getTaxNo() : companyDTO.getTaxNo());
        company.setPhone(ObjectUtils.isEmpty(companyDTO.getPhone()) ? company.getPhone() : companyDTO.getPhone());
        company.setEmail(ObjectUtils.isEmpty(companyDTO.getEmail()) ? company.getEmail() : companyDTO.getEmail());
        company.setEmail(ObjectUtils.isEmpty(companyDTO.getEmail()) ? company.getEmail() : companyDTO.getEmail());
        company.setWebSite(ObjectUtils.isEmpty(companyDTO.getWebSite()) ? company.getWebSite() : companyDTO.getWebSite());

        // save
        company.setId(companyID);
        companyRepository.save(company);

    }

    public Company getById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.COMPANY_NOT_FOUND_MESSAGE,id)));

        return company;

    }
}
