package com.ecommerce.service;

import com.ecommerce.entity.Address;
import com.ecommerce.entity.User;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Address> getAddresses(String email) {
        return addressRepository.findByUserId(userRepository.findByEmail(email).get().getId());
    }

    public Address addAddress(String email, Address address) {
        User user = userRepository.findByEmail(email).orElseThrow();
        address.setUser(user);
        return addressRepository.save(address);
    }
}
