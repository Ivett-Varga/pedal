package com.successfulcorp.pedal.controller;

import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<Person> getAllPeople() {
        log.info("Received request to get all persons");
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Integer id) {
        log.info("Received request to get person by ID: {}", id);
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        log.info("Received request to save person: {}", person);
        return personService.save(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Integer id, @RequestBody Person personDetails) {
        log.info("Received request to update person: {} to new values: {}", id, personDetails);
        ResponseEntity<Person> personToUpdate = personService.findById(id).map(person -> {
                    person.setFirstName(personDetails.getFirstName());
                    person.setLastName(personDetails.getLastName());
                    // Handle updating persones if necessary
                    Person updatedPerson = personService.save(person);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElse(ResponseEntity.notFound().build());
        return personToUpdate;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Integer id) {
        log.info("Received request to delete person: {}", id);
        return personService.findById(id)
                .map(person -> {
                    personService.delete(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPersons() {
        log.info("Received request to delete all persons");
        personService.deleteAll();
        return ResponseEntity.ok().build();
    }


}
