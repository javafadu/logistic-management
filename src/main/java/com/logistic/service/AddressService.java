package com.logistic.service;

import com.logistic.domain.Address;
import com.logistic.domain.Location;
import com.logistic.domain.User;
import com.logistic.dto.request.AddressRequest;
import com.logistic.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    public AddressService(AddressRepository addressRepository, UserService userService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
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
}
