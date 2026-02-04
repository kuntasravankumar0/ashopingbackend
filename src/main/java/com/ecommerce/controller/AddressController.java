package com.ecommerce.controller;

import com.ecommerce.entity.Address;
import com.ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/address")


public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public List<Address> getAddresses(Authentication auth) {
        return addressService.getAddresses(auth.getName());
    }

    @PostMapping
    public Address addAddress(@RequestBody Address address, Authentication auth) {
        return addressService.addAddress(auth.getName(), address);
    }
}
