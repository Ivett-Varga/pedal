package com.successfulcorp.pedal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void whenGetAllContacts_thenReturns200() throws Exception {
        List<Contact> contacts = Arrays.asList(new Contact(), new Contact());
        when(contactService.findAll()).thenReturn(contacts);

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contacts)));

        verify(contactService, times(1)).findAll();
    }

    @Test
    public void whenGetContactById_thenReturns200() throws Exception {
        Contact contact = new Contact();
        contact.setId(1);
        when(contactService.findById(contact.getId())).thenReturn(Optional.of(contact));

        mockMvc.perform(get("/api/contacts/{id}", contact.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contact)));

        verify(contactService, times(1)).findById(contact.getId());
    }

    @Test
    public void whenCreateContact_thenReturns200() throws Exception {
        Contact contact = new Contact();
        when(contactService.save(any(Contact.class))).thenReturn(contact);

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contact)));

        verify(contactService, times(1)).save(any(Contact.class));
    }

    @Test
    public void whenUpdateContact_thenReturns200() throws Exception {
        Contact existingContact = new Contact();
        existingContact.setId(1);
        Contact updatedContact = new Contact();
        updatedContact.setContactType("email");
        updatedContact.setContactDetail("john.doe@example.com");

        when(contactService.findById(existingContact.getId())).thenReturn(Optional.of(existingContact));
        when(contactService.save(any(Contact.class))).thenReturn(updatedContact);

        mockMvc.perform(put("/api/contacts/{id}", existingContact.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedContact)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedContact)));

        verify(contactService, times(1)).save(any(Contact.class));
    }

    @Test
    public void whenDeleteContact_thenReturns200() throws Exception {
        Contact contact = new Contact();
        contact.setId(1);

        when(contactService.findById(contact.getId())).thenReturn(Optional.of(contact));
        doNothing().when(contactService).deleteById(contact.getId());

        mockMvc.perform(delete("/api/contacts/{id}", contact.getId()))
                .andExpect(status().isOk());

        verify(contactService, times(1)).deleteById(contact.getId());
    }
}
