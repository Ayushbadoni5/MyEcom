package dev.ayushbadoni.MyEcom.controllers;

import dev.ayushbadoni.MyEcom.dto.AddressRequest;
import dev.ayushbadoni.MyEcom.entities.Address;
import dev.ayushbadoni.MyEcom.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@CrossOrigin
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressRequest addressRequest,Principal loggedInUser){
        Address address = addressService.createAddress(addressRequest,loggedInUser);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable UUID id,Principal loggedInUser) throws AccessDeniedException {
        addressService.deleteAddress(id,loggedInUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
 }
