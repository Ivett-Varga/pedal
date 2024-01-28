package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Contact;
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
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestHelper.class)
public class AddressRepositoryIT {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ContactRepository contactRepository;


    @Autowired
    private TestHelper testHelper;

    private Address testAddress;
    private List<Contact> testContacts;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll(); // Delete contacts first to avoid foreign key constraint violation        addressRepository.deleteAll();

        testAddress = testHelper.createAndPersistAddress("Test Street 1", "Test City", "Test State", "Test Zip", "Test Country");
        testContacts = testHelper.createAndPersistContacts(2);
        testHelper.createAndPersistAddresses(2, testContacts);
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
        // when
        List<Address> foundAddresses = addressRepository.findAll();

        // then
        assertThat(foundAddresses.size()).isGreaterThanOrEqualTo(3); // 3 including the testAddress and two created in setUp
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
