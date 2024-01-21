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
public class AddressRepositoryIT {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TestHelper testHelper;

    private Address testAddress;

    @BeforeEach
    public void setUp() {
        addressRepository.deleteAll();
        List<Contact> contacts = testHelper.createAndPersistContacts(1);
        testAddress = testHelper.createAndPersistAddresses(2, contacts).get(0);
        // Associating contacts with the address
        contacts.forEach(contact -> {
            contact.setAddressId(testAddress.getId());
            contactRepository.save(contact);
        });
    }

    @Test
    public void whenFindById_thenReturnAddress() {
        // when
        Optional<Address> foundAddress = addressRepository.findById(testAddress.getId());

        // then
        assertTrue(foundAddress.isPresent(), "Address should be present");
        assertThat(foundAddress.get().getId()).isEqualTo(testAddress.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllAddresses() {
        // given
        int numberOfAdditionalAddresses = 2; // Add 2 more addresses
        testHelper.createAndPersistAddresses(numberOfAdditionalAddresses, List.of());

        // when
        List<Address> foundAddresses = addressRepository.findAll();

        // then
        assertThat(foundAddresses.size()).isEqualTo(numberOfAdditionalAddresses + 1); // +1 for the testAddress
    }

    @Test
    public void whenDelete_thenAddressShouldBeDeleted() {
        // given
        Integer addressId = testAddress.getId();

        // when
        addressRepository.deleteById(addressId);
        Optional<Address> deletedAddress = addressRepository.findById(addressId);

        // then
        assertTrue(deletedAddress.isEmpty(), "Address should be deleted");
    }
}
