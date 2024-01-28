package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.domain.Person;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    // Find persons by last name
    List<Person> findByLastName(String lastName);

    // Find persons by first name and last name
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);

}