package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
        public List<Address> findAll() {
            return addressRepository.findAll();
        }

        public Optional<Address> findById(Integer id) {
            return addressRepository.findById(id);
        }

        public Address save(Address address) {
            return addressRepository.save(address);
        }

        public void deleteById(Integer id) {
            addressRepository.deleteById(id);
        }


    public void deleteAll() {
        addressRepository.deleteAll();
    }
    public Address updateAddress(Integer id, Address addressDetails) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id " + id));

        address.setStreet(addressDetails.getStreet());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setZipCode(addressDetails.getZipCode());
        address.setCountry(addressDetails.getCountry());

        return addressRepository.save(address);
    }
}