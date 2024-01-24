package com.logistic.service;

import com.logistic.domain.Address;
import com.logistic.domain.Location;
import com.logistic.domain.User;
import com.logistic.dto.AddressDTO;
import com.logistic.dto.mapper.AddressMapper;
import com.logistic.dto.request.AddressRequest;
import com.logistic.exception.ConflictException;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, UserService userService, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userService = userService;
        this.addressMapper = addressMapper;
    }

    // Add Address
    public void addOwnAddress(AddressRequest addressRequest) {
        // check1: get current logged in user
        User user = userService.getCurrentLoggedInUser();
        addAddressForUser(user.getId(), addressRequest);

    }

    public void addAddressForUser(Long userId, AddressRequest addressRequest) {
        User user = userService.getById(userId);

        Location location = new Location();
        location.setLatitude(addressRequest.getLatitude());
        location.setLongitude(addressRequest.getLongitude());

        Address address = new Address();
        address.setType(addressRequest.getType());
        address.setCountry(addressRequest.getCountry());
        address.setState(addressRequest.getState());
        address.setCity(addressRequest.getCity());
        address.setDistrict(addressRequest.getDistrict());
        address.setZipCode(addressRequest.getZipCode());
        address.setAddress(addressRequest.getAddress());
        address.setLocation(location);

        address.setUser(user);

        addressRepository.save(address);

    }

    @Transactional
    public void updateOwnAddress(Long addressId, AddressRequest addressRequest) {
        User user = userService.getCurrentLoggedInUser();
        updateUserAddress(user.getId(), addressId,addressRequest);
    }

    @Transactional
    public void updateUserAddress(Long userId, Long addressId, AddressRequest addressRequest) {
        //check1 : user exist or not
        User user = userService.getById(userId);

        // check2 : address id belongs to the user
        List<Long> userAddressIds = addressRepository.userAddressIds(user.getId());
        if(!userAddressIds.contains(addressId)) {
            throw new ConflictException(String.format(ErrorMessages.ADDRESS_ID_CONFLICT_MESSAGE,addressId));
        }
        addressRepository.updateUserAddress(addressId,addressRequest.getType(),addressRequest.getCountry(),addressRequest.getState(),addressRequest.getCity(),addressRequest.getDistrict(),addressRequest.getZipCode(),addressRequest.getAddress(),addressRequest.getLatitude(),addressRequest.getLongitude());


    }

    public List<AddressDTO> getUserOwnAddresses() {
        User user = userService.getCurrentLoggedInUser();
        return getAddressesOfAUser(user.getId());
    }


    public List<AddressDTO> getAddressesOfAUser(Long userId) {
        User user = userService.getById(userId);
        List<Address> userAddresses = addressRepository.userAddresses(user.getId());

        return addressMapper.addressListToAddressDTOList(userAddresses);
    }


    public AddressDTO getUserOwnAddress(Long addressId) {
        // check1: addressId belongs to the logged-in user or not
        User user = userService.getCurrentLoggedInUser();
        return getUserAddress(user.getId(), addressId);
    }

    public AddressDTO getUserAddress(Long userId, Long addressId) {

        User user = userService.getById(userId);
        List<Long> userAddressIds = addressRepository.userAddressIds(user.getId());
        if(!userAddressIds.contains(addressId)) {
            throw new ConflictException(String.format(ErrorMessages.ADDRESS_ID_CONFLICT_MESSAGE,addressId));
        }
        Address address = addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,addressId)));

        return addressMapper.addressToAddressDTO(address);
    }

    // DELETE ADDRESS by User
    public void deleteAddressById(Long addressId) {
        User user = userService.getCurrentLoggedInUser();
        if(checkUserAddress(user.getId(),addressId)) {
            addressRepository.deleteById(addressId);
        } else {
            throw new ConflictException(String.format(ErrorMessages.ADDRESS_ID_CONFLICT_MESSAGE,addressId));
        }
    }

    // DELETE ADDRESS by Admin
    public void deleteAddressByAdmin(Long userId, Long addressId) {
        if(checkUserAddress(userId,addressId)) {
            addressRepository.deleteById(addressId);
        } else {
            throw new ConflictException(String.format(ErrorMessages.ADDRESS_ID_CONFLICT_MESSAGE,addressId));
        }
    }


    // CHECK ADDRESSID belongs to USERID
    public Boolean checkUserAddress(Long userId, Long addressId) {

        User user = userService.getById(userId);
        List<Long> userAddressIds = addressRepository.userAddressIds(user.getId());
        if(!userAddressIds.contains(addressId)) {
            throw new ConflictException(String.format(ErrorMessages.ADDRESS_ID_CONFLICT_MESSAGE,addressId));
        }
        Address address = addressRepository.findById(addressId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,addressId)));

        return true;
    }

}
