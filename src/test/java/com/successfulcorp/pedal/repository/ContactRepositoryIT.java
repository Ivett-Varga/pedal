package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.domain.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestExecutionListeners(listeners={
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@Import(TestHelper.class)
public class ContactRepositoryIT {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TestHelper testHelper;

    private Contact testContact;
    private Address testAddress;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
        testAddress = testHelper.createAndPersistAddress("Test Street 1", "Test City", "Test State", "Test Zip", "Test Country");
        testContact = testHelper.createAndPersistContact(testAddress.getId(), "Email", "test@example.com");
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
    public void whenFindAll_thenReturnAllContacts() {
        // given
        int numberOfAdditionalContacts = 2; // Add 2 more contacts
        testHelper.createAndPersistContacts(numberOfAdditionalContacts);

        // when
        List<Contact> foundContacts = contactRepository.findAll();

        // then
        assertThat(foundContacts.size()).isEqualTo(numberOfAdditionalContacts + 1); // +1 for the testContact
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

    @Test
    public void whenFindByAddressId_thenReturnContacts() {
        // given
        Integer addressId = testAddress.getId();

        // when
        List<Contact> foundContacts = contactRepository.findByAddressId(addressId);

        // then
        assertThat(foundContacts).isNotEmpty();
        assertThat(foundContacts.get(0).getAddressId()).isEqualTo(addressId);
    }
}
