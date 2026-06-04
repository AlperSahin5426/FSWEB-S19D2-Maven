package com.workintech.s18d4.service;

import com.workintech.s18d4.entity.Address;
import java.util.List;

public interface AddressService {
    List<Address> findAll();
    Address find(Long id); // Long yerine int
    Address save(Address address);
    Address delete(Long id); // Long yerine int
}