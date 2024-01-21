package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    // Find contacts associated with a specific address ID
    List<Contact> findByAddressId(Integer addressId);


}