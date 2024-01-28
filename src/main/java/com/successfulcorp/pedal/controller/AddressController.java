package com.successfulcorp.pedal.controller;

import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.service.AddressService;
import com.successfulcorp.pedal.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    private PersonService personService;

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        log.info("Received request to get all addresses");
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) {
        log.info("Received request to get address by ID: {}", id);
        return addressService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Address createAddress(@RequestBody Address address) {
        log.info("Received request to save address: {}", address);
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody Address addressDetails) {
        log.info("Received request to update address: {} to new values: {}", id, addressDetails);
        return addressService.findById(id)
                .map(address -> {
                    address.setStreet(addressDetails.getStreet());
                    address.setCity(addressDetails.getCity());
                    address.setState(addressDetails.getState());
                    address.setZipCode(addressDetails.getZipCode());
                    address.setCountry(addressDetails.getCountry());
                    Address updatedAddress = addressService.save(address);
                    return ResponseEntity.ok(updatedAddress);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        log.info("Received request to delete address: {}", id);
        return addressService.findById(id)
                .map(address -> {
                    List<Person> people = personService.findByPermanentAddress(address);

                    for (Person person : people) {
                        person.setPermanentAddress(null);
                        personService.save(person);
                    }

                    addressService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllAddresses() {
        log.info("Received request to delete all addresses");
        addressService.deleteAll();
        return ResponseEntity.ok().build();
    }

}
