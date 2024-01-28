package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.repository.AddressRepository;
import com.successfulcorp.pedal.repository.ContactRepository;
import com.successfulcorp.pedal.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ContactRepository contactRepository;

    private PersonService personService;

    private Person testPerson;

    @BeforeEach
    public void setUp() {
        // Mock initialization
        addressRepository = Mockito.mock(AddressRepository.class);
        personRepository = Mockito.mock(PersonRepository.class);
        contactRepository = Mockito.mock(ContactRepository.class);

        // Manual injection
        personService = new PersonService(personRepository, addressRepository);

        TestHelper testHelper = new TestHelper(addressRepository, personRepository, contactRepository);
        testPerson = testHelper.createAndPersistPerson("John", "Doe", null, null);

        if(testPerson == null) {
            testPerson = new Person();
            testPerson.setFirstName("John");
            testPerson.setLastName("Doe");
        }

        when(personRepository.save(any(Person.class))).thenReturn(testPerson);
    }
    @Test
    public void whenFindAll_thenReturnAllPersons() {
        // Given
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", "Doe"));
        persons.add(new Person("Jane", "Doe"));

        when(personRepository.findAll()).thenReturn(persons);

        // When
        List<Person> foundPersons = personService.findAll();

        // Then
        assertThat(foundPersons).isNotNull();
        assertThat(foundPersons).hasSize(2);
        assertThat(foundPersons.get(0).getFirstName()).isEqualTo("John");
        assertThat(foundPersons.get(1).getFirstName()).isEqualTo("Jane");

        verify(personRepository, times(1)).findAll();
    }
    @Test
    public void whenFindById_thenReturnPerson() {
        // when
        when(personRepository.findById(testPerson.getId())).thenReturn(Optional.of(testPerson));
        Optional<Person> foundPerson = personService.findById(testPerson.getId());

        // then
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getFirstName()).isEqualTo("John");
        assertThat(foundPerson.get().getLastName()).isEqualTo("Doe");
        verify(personRepository, times(1)).findById(testPerson.getId());
    }

    @Test
    public void whenSavePerson_thenPersonShouldBeSaved() {
        // when
        when(personRepository.save(any(Person.class))).thenReturn(testPerson);
        Person savedPerson = personService.save(testPerson);

        // then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getFirstName()).isEqualTo("John");
        assertThat(savedPerson.getLastName()).isEqualTo("Doe");
        verify(personRepository, times(1)).save(testPerson);
    }

    @Test
    public void whenDeleteAll_thenAllPersonsShouldBeDeleted() {
        // Act
        personService.deleteAll();

        // Assert
        verify(personRepository, times(1)).deleteAll();
    }

    @Test
    public void whenDeletePerson_thenPersonShouldBeDeleted() {
        when(personRepository.existsById(1)).thenReturn(true);

        // When
        personService.delete(1);

        // Then
        verify(personRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenUpdatePerson_thenPersonShouldBeUpdated() {
        // Given
        Integer personId = 1;
        Person existingPerson = new Person("John", "Doe");
        existingPerson.setId(personId);

        Person updatedDetails = new Person("Jane", "Doe");
        updatedDetails.setPermanentAddress(new Address());
        updatedDetails.setTemporaryAddress(new Address());

        when(personRepository.findById(personId)).thenReturn(Optional.of(existingPerson));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Person updatedPerson = personService.updatePerson(personId, updatedDetails);

        // Then
        assertThat(updatedPerson.getFirstName()).isEqualTo(updatedDetails.getFirstName());
        assertThat(updatedPerson.getLastName()).isEqualTo(updatedDetails.getLastName());
        assertThat(updatedPerson.getPermanentAddress()).isEqualTo(updatedDetails.getPermanentAddress());
        assertThat(updatedPerson.getTemporaryAddress()).isEqualTo(updatedDetails.getTemporaryAddress());

        verify(personRepository, times(1)).findById(personId);
        verify(personRepository, times(2)).save(any(Person.class));
    }

    @Test
    public void whenCreatePersonWithAddresses_thenPersonShouldBeCreatedWithAddresses() {
        // Given
        String firstName = "John", lastName = "Doe";
        String permStreet = "123 Main St", permCity = "Anytown", permState = "Anystate", permZip = "12345", permCountry = "CountryA";
        String tempStreet = "456 Side St", tempCity = "Othertown", tempState = "Otherstate", tempZip = "67890", tempCountry = "CountryB";

        Address permAddress = new Address(permStreet, permCity, permState, permZip, permCountry);
        Address tempAddress = new Address(tempStreet, tempCity, tempState, tempZip, tempCountry);

        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Person createdPerson = personService.createPersonWithAddresses(
                firstName, lastName,
                permStreet, permCity, permState, permZip, permCountry,
                tempStreet, tempCity, tempState, tempZip, tempCountry);

        // Then
        assertThat(createdPerson.getFirstName()).isEqualTo(firstName);
        assertThat(createdPerson.getLastName()).isEqualTo(lastName);
        assertThat(createdPerson.getPermanentAddress()).isEqualToComparingFieldByField(permAddress);
        assertThat(createdPerson.getTemporaryAddress()).isEqualToComparingFieldByField(tempAddress);

        verify(addressRepository, times(2)).save(any(Address.class));
        verify(personRepository, times(2)).save(any(Person.class));
    }


}
