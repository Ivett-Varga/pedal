package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Person;
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
public class PersonRepositoryIT {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestHelper testHelper;

    private Person testPerson;
    private Address testAddress;

    @BeforeEach
    public void setUp() {
        // Clear the repository and set up a test person and address
        personRepository.deleteAll();
        testAddress = testHelper.createAndPersistAddress("Test Street 1", "Test City", "Test State", "Test Zip", "Test Country");
        testPerson = testHelper.createAndPersistPerson("John", "Doe", testAddress, null);
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
        assertThat(foundPeople.size()).isEqualTo(numberOfAdditionalPeople + 1); // +1 for the testPerson
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
    public void whenFindByLastName_thenReturnPeopleWithMatchingLastName() {
        String lastName = "Doe";

        List<Person> foundPeople = personRepository.findByLastName(lastName);

        assertThat(foundPeople.size()).isEqualTo(1);
        assertThat(foundPeople.get(0).getLastName()).isEqualTo(lastName);
    }

    @Test
    public void whenFindByFirstNameAndLastName_thenReturnPeopleWithMatchingFirstNameAndLastName() {
        String firstName = "John";
        String lastName = "Doe";

        List<Person> foundPeople = personRepository.findByFirstNameAndLastName(firstName, lastName);

        assertThat(foundPeople.size()).isEqualTo(1);
        assertThat(foundPeople.get(0).getFirstName()).isEqualTo(firstName);
        assertThat(foundPeople.get(0).getLastName()).isEqualTo(lastName);
    }
}
