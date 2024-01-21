package com.successfulcorp.pedal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void whenGetAllAddresses_thenReturns200() throws Exception {
        List<Address> addresses = Arrays.asList(new Address(), new Address());
        when(addressService.findAll()).thenReturn(addresses);

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(addresses)));

        verify(addressService, times(1)).findAll();
    }

    @Test
    public void whenGetAddressById_thenReturns200() throws Exception {
        Address address = new Address();
        address.setId(1);
        when(addressService.findById(address.getId())).thenReturn(Optional.of(address));

        mockMvc.perform(get("/api/addresses/{id}", address.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(address)));

        verify(addressService, times(1)).findById(address.getId());
    }

    @Test
    public void whenCreateAddress_thenReturns200() throws Exception {
        Address address = new Address();
        when(addressService.save(any(Address.class))).thenReturn(address);

        mockMvc.perform(post("/api/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(address)));

        verify(addressService, times(1)).save(any(Address.class));
    }

    @Test
    public void whenUpdateAddress_thenReturns200() throws Exception {
        Address existingAddress = new Address();
        existingAddress.setId(1);
        Address updatedAddress = new Address();
        updatedAddress.setStreet("Updated Street");

        when(addressService.findById(existingAddress.getId())).thenReturn(Optional.of(existingAddress));
        when(addressService.save(any(Address.class))).thenReturn(updatedAddress);

        mockMvc.perform(put("/api/addresses/{id}", existingAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedAddress)));

        verify(addressService, times(1)).save(any(Address.class));
    }

    @Test
    public void whenDeleteAddress_thenReturns200() throws Exception {
        Address address = new Address();
        address.setId(1);

        when(addressService.findById(address.getId())).thenReturn(Optional.of(address));
        doNothing().when(addressService).deleteById(address.getId());

        mockMvc.perform(delete("/api/addresses/{id}", address.getId()))
                .andExpect(status().isOk());

        verify(addressService, times(1)).deleteById(address.getId());
    }

    @Test
    public void whenDeleteAllAddresses_thenReturns200() throws Exception {
        doNothing().when(addressService).deleteAll();

        mockMvc.perform(delete("/api/addresses"))
                .andExpect(status().isOk());

        verify(addressService, times(1)).deleteAll();
    }
}
