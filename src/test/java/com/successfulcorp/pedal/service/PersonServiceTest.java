package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.repository.AddressRepository;
import com.successfulcorp.pedal.repository.ContactRepository;
import com.successfulcorp.pedal.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private PersonService personService;


    private Person testPerson;

    @BeforeEach
    public void setUp() {
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
    public void whenSavePerson_thenPersonShouldBeSaved() {
        // when
        when(personRepository.save(any(Person.class))).thenReturn(testPerson);
        Person savedPerson = personService.savePerson(testPerson);

        // then
        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getFirstName()).isEqualTo("John");
        assertThat(savedPerson.getLastName()).isEqualTo("Doe");
        verify(personRepository, times(1)).save(testPerson);
    }

    @Test
    public void whenFindById_thenReturnPerson() {
        // when
        when(personRepository.findById(testPerson.getId())).thenReturn(Optional.of(testPerson));
        Optional<Person> foundPerson = personService.getPersonById(testPerson.getId());

        // then
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getFirstName()).isEqualTo("John");
        assertThat(foundPerson.get().getLastName()).isEqualTo("Doe");
        verify(personRepository, times(1)).findById(testPerson.getId());
    }

    @Test
    public void whenDelete_thenPersonShouldBeDeleted() {
        // when
        personService.deletePerson(testPerson.getId());

        // then
        verify(personRepository, times(1)).deleteById(testPerson.getId());
    }
}
