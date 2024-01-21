package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.domain.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ContactRepositoryIT {

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void whenFindById_thenReturnContact() {
        // given
        Contact contact = new Contact(/* set properties */);
        contact = contactRepository.save(contact);

        // when
        Optional<Contact> foundContact = contactRepository.findById(contact.getId());

        // then
        assertTrue(foundContact.isPresent(), "Contact should be present");
        assertThat(foundContact.get().getId()).isEqualTo(contact.getId());
    }

    @Test
    public void whenFindByAddressId_thenReturnContact() {

        Integer addressId = 1;
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        contact1 = contactRepository.save(contact1);
        contact2 = contactRepository.save(contact2);

        List<Contact> foundContacts = contactRepository.findByAddressId(addressId);

        assertThat(foundContacts).contains(contact1, contact2);
    }

}