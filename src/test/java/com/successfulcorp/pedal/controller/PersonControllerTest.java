package com.successfulcorp.pedal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void whenGetAllPeople_thenReturns200() throws Exception {
        List<Person> people = Arrays.asList(new Person(), new Person());
        when(personService.findAll()).thenReturn(people);

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(people)));

        verify(personService, times(1)).findAll();
    }

    @Test
    public void whenFindById_thenReturns200() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(personService.findById(person.getId())).thenReturn(Optional.of(person));

        mockMvc.perform(get("/api/persons/{id}", person.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(person)));

        verify(personService, times(1)).findById(person.getId());
    }

    @Test
    public void whenCreatePerson_thenReturns200() throws Exception {
        Person person = new Person();
        when(personService.save(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(person)));

        verify(personService, times(1)).save(any(Person.class));
    }

    @Test
    public void whenUpdatePerson_thenReturns200() throws Exception {
        Person existingPerson = new Person();
        existingPerson.setId(1);
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");

        when(personService.findById(existingPerson.getId())).thenReturn(Optional.of(existingPerson));
        when(personService.save(any(Person.class))).thenReturn(updatedPerson);

        mockMvc.perform(put("/api/persons/{id}", existingPerson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPerson)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedPerson)));

        verify(personService, times(1)).save(any(Person.class));
    }

    @Test
    public void whenDeletePerson_thenReturns200() throws Exception {
        Person person = new Person();
        person.setId(1);

        when(personService.findById(person.getId())).thenReturn(Optional.of(person));
        doNothing().when(personService).delete(person.getId());

        mockMvc.perform(delete("/api/persons/{id}", person.getId()))
                .andExpect(status().isOk());

        verify(personService, times(1)).delete(person.getId());
    }

    @Test
    public void whenDeleteAllPersons_thenReturns200() throws Exception {
        doNothing().when(personService).deleteAll();

        mockMvc.perform(delete("/api/persons"))
                .andExpect(status().isOk());

        verify(personService, times(1)).deleteAll();
    }

}