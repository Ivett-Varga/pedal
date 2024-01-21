package com.successfulcorp.pedal.controller;

import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<Contact> getAllContacts() {
        log.info("Received request to get all contacts");
        return contactService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Integer id) {
        log.info("Received request to get contact by ID: {}", id);
        return contactService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        log.info("Received request to save contact: {}", contact);
        return contactService.save(contact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Integer id, @RequestBody Contact contactDetails) {
        log.info("Received request to update contact: {} to new values: {}", id, contactDetails);
        return contactService.findById(id)
                .map(contact -> {
                    contact.setContactType(contactDetails.getContactType());
                    contact.setContactDetail(contactDetails.getContactDetail());
                    Contact updatedContact = contactService.save(contact);
                    return ResponseEntity.ok(updatedContact);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Integer id) {
        log.info("Received request to delete contact: {}", id);
        return contactService.findById(id)
                .map(contact -> {
                    contactService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
