package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.domain.Person;
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
public class PersonRepositoryIT {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestHelper testHelper;

    private Person testPerson;
    private Address testAddress;

    private Contact testContact;

    private List<Person> people;
    private List<Address> addresses;
    private List<Contact> contacts;

    @BeforeEach
    public void setUp() {
        // Clear the repository and set up a test person and address
        personRepository.deleteAll();
        testAddress = testHelper.createAndPersistAddress("Test Street 1", "Test City", "Test State", "Test Zip", "Test Country");
        testPerson = testHelper.createAndPersistPerson("John", "Doe", testAddress, null);
        testContact = testHelper.createAndPersistContact(testAddress.getId(), "Email", "test@example.com");
        contacts = testHelper.createAndPersistContacts(2);
        addresses = testHelper.createAndPersistAddresses(2, contacts);
        people = testHelper.createAndPersistPeopleWithAddresses(2, addresses);
    }

    @Test
    public void whenFindById_thenReturnPerson() {
        // when
        Optional<Person> foundPerson = personRepository.findById(testPerson.getId());

        // then
        assertTrue(foundPerson.isPresent(), "Person should be present");
        assertThat(foundPerson.get().getId()).isEqualTo(testPerson.getId());
    }

    @Test
    public void whenFindAll_thenReturnAllPeople() {
        // given
        int numberOfAdditionalPeople = 2; // Add 2 more people
        testHelper.createAndPersistPeopleWithAddresses(numberOfAdditionalPeople, List.of(testAddress));

        // when
        List<Person> foundPeople = personRepository.findAll();

        // then
        assertThat(foundPeople.size()).isEqualTo(numberOfAdditionalPeople + 3); // +1 for the testPerson
    }

    @Test
    public void whenDelete_thenPersonShouldBeDeleted() {
        // given
        Integer personId = testPerson.getId();

        // when
        personRepository.deleteById(personId);
        Optional<Person> deletedPerson = personRepository.findById(personId);

        // then
        assertTrue(deletedPerson.isEmpty(), "Person should be deleted");
    }

    @Test
    public void whenFindByFirst_thenReturnPeopleWithMatchingLastName() {
        String firstName = "Jane";

        List<Person> foundPeople = personRepository.findByFirstName(firstName);

        assertThat(foundPeople.size()).isGreaterThanOrEqualTo(1);
        assertThat(foundPeople.get(0).getFirstName()).isEqualTo(firstName);
    }

    @Test
    public void whenFindByLastName_thenReturnPeopleWithMatchingLastName() {
        String lastName = "Doe";

        List<Person> foundPeople = personRepository.findByLastName(lastName);

        assertThat(foundPeople.size()).isGreaterThanOrEqualTo(1);
        assertThat(foundPeople.get(0).getLastName()).isEqualTo(lastName);
    }

    @Test
    public void whenFindByFirstNameAndLastName_thenReturnPeopleWithMatchingFirstNameAndLastName() {
        String firstName = "John";
        String lastName = "Doe";

        List<Person> foundPeople = personRepository.findByFirstNameAndLastName(firstName, lastName);

        assertThat(foundPeople.size()).isGreaterThanOrEqualTo(1);
        assertThat(foundPeople.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(foundPeople.get(0).getLastName()).isEqualTo(lastName);
    }
}
