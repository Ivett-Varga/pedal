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
        log.info("Fetching all contacts");
        List<Contact> contacts = contactRepository.findAll();
        log.info("Found {} contacts", contacts.size());
        return contacts;
    }

    public Optional<Contact> findById(Integer id) {
        log.info("Fetching contact with id: {}", id);
        Optional<Contact> contact = contactRepository.findById(id);
        contact.ifPresentOrElse(a -> log.info("Contact found: {}", a),
                () -> log.warn("Contact not found with id: {}", id));
        return contact;
    }

    public Contact save(Contact contact) {
        log.info("Saving contact: {}", contact);
        Contact savedcontact = contactRepository.save(contact);
        log.info("Saved contact with id: {}", savedcontact.getId());
        return savedcontact;
    }

    public void deleteById(Integer id) {
        log.info("Deleting contact with id: {}", id);
        boolean exists = contactRepository.existsById(id);
        if (exists) {
            contactRepository.deleteById(id);
            log.info("Deleted contact with id: {}", id);
        } else {
            log.warn("Attempted to delete non-existing contact with id: {}", id);
        }
    }


    public void deleteAll() {
        contactRepository.deleteAll();
    }

    public Contact updateContact(Integer id, Contact contactDetails) {
        log.info("Updating contact with id: {}", id);
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Contact not found with id {}", id);
                    return new RuntimeException("Contact not found with id " + id);
                });

        contact.setContactType(contactDetails.getContactType());
        contact.setContactDetail(contactDetails.getContactDetail());

        Contact updatedcontact = contactRepository.save(contact);
        log.info("Updated contact with id: {}", id);
        return updatedcontact;
    }

}
