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
            log.info("Fetching all addresses");
            List<Address> addresses = addressRepository.findAll();
            log.info("Found {} addresses", addresses.size());
            return addresses;
        }

        public Optional<Address> findById(Integer id) {
            log.info("Fetching address with id: {}", id);
            Optional<Address> address = addressRepository.findById(id);
            address.ifPresentOrElse(a -> log.info("Address found: {}", a),
                    () -> log.warn("Address not found with id: {}", id));
            return address;
        }

        public Address save(Address address) {
            log.info("Saving address: {}", address);
            Address savedAddress = addressRepository.save(address);
            log.info("Saved address with id: {}", savedAddress.getId());
            return savedAddress;
        }

        public void deleteById(Integer id) {
            log.info("Deleting address with id: {}", id);
            boolean exists = addressRepository.existsById(id);
            if (exists) {
                addressRepository.deleteById(id);
                log.info("Deleted address with id: {}", id);
            } else {
                log.warn("Attempted to delete non-existing address with id: {}", id);
            }
        }


    public void deleteAll() {
        addressRepository.deleteAll();
    }
    public Address updateAddress(Integer id, Address addressDetails) {
        log.info("Updating address with id: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Address not found with id {}", id);
                    return new RuntimeException("Address not found with id " + id);
                });

        address.setStreet(addressDetails.getStreet());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setZipCode(addressDetails.getZipCode());
        address.setCountry(addressDetails.getCountry());

        Address updatedAddress = addressRepository.save(address);
        log.info("Updated address with id: {}", id);
        return updatedAddress;
    }
}