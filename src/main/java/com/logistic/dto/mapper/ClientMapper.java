package com.logistic.dto.mapper;

import com.logistic.domain.Address;
import com.logistic.domain.Client;
import com.logistic.domain.Company;
import com.logistic.domain.ImageFile;
import com.logistic.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    // convert DTO to Pojo
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "company", ignore = true)
    Client clientDTOToClient(ClientDTO clientDTO);


    // convert List-DTO to list Pojo
    List<ClientDTO> clientsToClientDTOS(List<Client> clients);


    // convert CustomerDTO to Customer
    @Mapping(source = "images", target = "images", qualifiedByName = "getImagesAsString")
    @Mapping(source = "addresses", target = "addresses", qualifiedByName = "getAddressesAsLong")
    @Mapping(source = "company", target = "company", qualifiedByName = "getCompanyAsLong")
    ClientDTO clientToClientDTO(Client client);


    @Named("getImagesAsString")
    public static Set<String> getImageIds(Set<ImageFile> imageFiles) {
        Set<String> imageIds = new HashSet<>();

        imageIds = imageFiles.stream().map(imFile->imFile.getId().toString()).collect(Collectors.toSet());

        return imageIds;
    }



    @Named("getAddressesAsLong")
    public static List<Long> getAddressIds(List<Address> addresses) {
        List<Long> addIds = new ArrayList<>();

        addIds = addresses.stream().map(address->address.getId()).collect(Collectors.toList());

        return addIds;
    }

    @Named("getCompanyAsLong")
    public static Long getCompanyId(Company company) {
        return company.getId();
    }


}
