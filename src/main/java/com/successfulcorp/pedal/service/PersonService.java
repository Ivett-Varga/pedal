package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.domain.Person;
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

    public void deleteById(Integer id) {
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
                .orElseThrow(() ->  {
                    log.error("Person not found with id {}", id);
                    return new RuntimeException("Person not found with id " + id);
                });

        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setPermanentAddress(personDetails.getPermanentAddress());
        person.setTemporaryAddress(personDetails.getTemporaryAddress());

        Person updatedPerson = personRepository.save(person);
        log.info("Updated person with id: {}", id);
        return updatedPerson;
    }

    public Optional<Person> getPersonById(Integer id) {
        return personRepository.findById(id);
    }

    public void deletePerson(Integer id) {
        personRepository.deleteById(id);
    }

    public Person savePerson(Person testPerson) {
        return personRepository.save(testPerson);
    }

    public List<Person> findByFirstName(String firstName) {
        log.info("Fetching persons by first name: {}", firstName);
        List<Person> persons = personRepository.findByFirstName(firstName);
        log.info("Found {} persons with first name: {}", persons.size(), firstName);
        return persons;
    }


    public List<Person> findByLastName(String lastName) {
        log.info("Fetching persons by last name: {}", lastName);
        List<Person> persons = personRepository.findByLastName(lastName);
        log.info("Found {} persons with last name: {}", persons.size(), lastName);
        return persons;
    }

    public List<Person> findByFirstNameAndLastName(String firstName, String lastName) {
        log.info("Fetching persons by first name: {} and last name: {}", firstName, lastName);
        List<Person> persons = personRepository.findByFirstNameAndLastName(firstName, lastName);
        log.info("Found {} persons with first name: {} and last name: {}", persons.size(), firstName, lastName);
        return persons;
    }

    public List<Person> findAllByOrderByLastNameAsc() {
        log.info("Fetching all persons ordered by last name");
        List<Person> persons = personRepository.findAllByOrderByLastNameAsc();
        log.info("Found {} persons", persons.size());
        return persons;
    }
}
