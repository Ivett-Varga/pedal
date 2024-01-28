package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.repository.AddressRepository;
import com.successfulcorp.pedal.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    public List<Person> findAll() {
        log.info("Fetching all persons");
        List<Person> persons = personRepository.findAll();
        log.info("Found {} persons", persons.size());
        return persons;
    }

    public Optional<Person> findById(Integer id) {
        log.info("Fetching person with id: {}", id);
        Optional<Person> person = personRepository.findById(id);
        person.ifPresentOrElse(a -> log.info("person found: {}", a),
                () -> log.warn("person not found with id: {}", id));
        return person;
    }

    public Person save(Person person) {
        log.info("Saving person: {}", person);
        Person savedperson = personRepository.save(person);
        log.info("Saved person with id: {}", savedperson.getId());
        return savedperson;
    }

    public void delete(Integer id) {
        log.info("Deleting person with id: {}", id);
        boolean exists = personRepository.existsById(id);
        if (exists) {
            personRepository.deleteById(id);
            log.info("Deleted person with id: {}", id);
        } else {
            log.warn("Attempted to delete non-existing person with id: {}", id);
        }
    }

    public void deleteAll() {
        personRepository.deleteAll();
    }

    public Person updatePerson(Integer id, Person personDetails) {
        log.info("Updating person with id: {}", id);
        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Person not found with id {}", id);
                    return new RuntimeException("Person not found with id " + id);
                });

        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        if (personDetails.getPermanentAddress() != null) {
            person.setPermanentAddress(personDetails.getPermanentAddress());
        }
        if (personDetails.getTemporaryAddress() != null) {
            person.setTemporaryAddress(personDetails.getTemporaryAddress());
        }
        Person updatedPerson = personRepository.save(person);
        log.info("Updated person with id: {}", id);
        return updatedPerson;
    }

    public Person createPersonWithAddresses(
            String firstName, String lastName,
            String permStreet, String permCity, String permState, String permZipCode, String permCountry,
            String tempStreet, String tempCity, String tempState, String tempZipCode, String tempCountry
    ) {
        Address permAddress = new Address(permStreet, permCity, permState, permZipCode, permCountry);
        Address tempAddress = new Address(tempStreet, tempCity, tempState, tempZipCode, tempCountry);

        permAddress = addressRepository.save(permAddress);
        tempAddress = addressRepository.save(tempAddress);

        Person person = new Person(firstName, lastName);
        person.setPermanentAddress(permAddress);
        person.setTemporaryAddress(tempAddress);

        return personRepository.save(person);
    }

    public List<Person> findByPermanentAddress(Address permanentAddress) {
        return personRepository.findByPermanentAddress(permanentAddress);
    }
}
