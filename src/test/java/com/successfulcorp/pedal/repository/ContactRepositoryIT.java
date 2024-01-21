package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestHelper.class) // Import TestHelper to use in this test
public class ContactRepositoryIT {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TestHelper testHelper;

    private Contact testContact;
    private Address testAddress;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
        addressRepository.deleteAll();
        List<Contact> contacts = testHelper.createAndPersistContacts(3);
        List<Address> addresses = testHelper.createAndPersistAddresses(1, contacts);
        testAddress = addresses.get(0); // Select the first address for testing
        testContact = contacts.get(0); // Select the first contact for testing

        // Associate the contact with the first address
        contacts.forEach(contact -> {
            contact.setAddressId(testAddress.getId());
            contactRepository.save(contact);
        });
    }




    @Test
    public void whenFindById_thenReturnContact() {
        // when
        Optional<Contact> foundContact = contactRepository.findById(testContact.getId());

        // then
        assertTrue(foundContact.isPresent(), "Contact should be present");
        assertThat(foundContact.get().getId()).isEqualTo(testContact.getId());
    }

    @Test
    public void whenFindByAddressId_thenReturnContacts() {
        // given
        testHelper.createAndPersistContact(testAddress.getId(), "email", "john.doe@example.com");

        // when
        List<Contact> foundContacts = contactRepository.findByAddressId(testAddress.getId());

        // then
        assertThat(foundContacts).hasSize(2); // 2 contacts associated with the testAddress
    }

    @Test
    public void whenDelete_thenContactShouldBeDeleted() {
        // given
        Integer contactId = testContact.getId();

        // when
        contactRepository.deleteById(contactId);
        Optional<Contact> deletedContact = contactRepository.findById(contactId);

        // then
        assertTrue(deletedContact.isEmpty(), "Contact should be deleted");
    }
}
