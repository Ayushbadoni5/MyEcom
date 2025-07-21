package dev.ayushbadoni.MyEcom.services;

import dev.ayushbadoni.MyEcom.dto.AddressRequest;
import dev.ayushbadoni.MyEcom.entities.Address;
import dev.ayushbadoni.MyEcom.entities.User;
import dev.ayushbadoni.MyEcom.exceptions.ResourceNotFoundEx;
import dev.ayushbadoni.MyEcom.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(AddressRequest addressRequest, Principal loggedInUser) {
        User user = (User) userDetailsService.loadUserByUsername(loggedInUser.getName());
        Address address = Address.builder()
                .name(addressRequest.getName())
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .phoneNumber(addressRequest.getPhoneNumber())
                .user(user)
                .build();
        return addressRepository.save(address);
    }

    public void deleteAddress(UUID id, Principal loggedInUser) throws AccessDeniedException {
        // Step 1: Get address
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundEx("Address not found"));

        // Step 2: Check ownership (you need to implement this logic)
        if (!address.getUser().getEmail().equals(loggedInUser.getName())) {
            throw new AccessDeniedException("You are not allowed to delete this address");
        }

        // Step 3: Delete it
        addressRepository.deleteById(id);
    }

}

