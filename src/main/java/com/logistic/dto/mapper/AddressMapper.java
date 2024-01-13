package com.logistic.dto.mapper;

import com.logistic.domain.Address;
import com.logistic.dto.AddressDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    List<AddressDTO> addressListToAddressDTOList(List<Address> address);

}
