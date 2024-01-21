package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;


    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    public Optional<Contact> findById(Integer id) {
        return contactRepository.findById(id);
    }

    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    public void deleteById(Integer id) {
        contactRepository.deleteById(id);
    }


    public void deleteAll() {
        contactRepository.deleteAll();
    }

    public Contact updateContact(Integer id, Contact contactDetails) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id " + id));

        contact.setContactType(contactDetails.getContactType());
        contact.setContactDetail(contactDetails.getContactDetail());

        return contactRepository.save(contact);
    }

}
