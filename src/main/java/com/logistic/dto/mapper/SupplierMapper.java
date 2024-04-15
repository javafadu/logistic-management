package com.logistic.dto.mapper;

import com.logistic.domain.*;
import com.logistic.dto.SupplierDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    // convert DTO to Pojo
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "company", ignore = true)
    Supplier supplierDTOToSupplier(SupplierDTO supplierDTO);


    // convert List-DTO to list Pojo
    List<SupplierDTO> suppliersToSupplierDTOS(List<Supplier> suppliers);


    // convert SupplierDTO to Supplier
    @Mapping(source = "images", target = "images", qualifiedByName = "getImagesAsString")
    @Mapping(source = "addresses", target = "addresses", qualifiedByName = "getAddressesAsLong")
    @Mapping(source = "company", target = "company", qualifiedByName = "getCompanyAsLong")
    SupplierDTO supplieroSupplierDTO(Supplier supplier);


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
