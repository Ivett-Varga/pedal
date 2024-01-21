package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address testAddress;

    @BeforeEach
    public void setUp() {
        TestHelper testHelper = new TestHelper(null, null, null);
        testAddress = new Address();
        testAddress.setId(1);
        testAddress.setStreet("Test Street");
        testAddress.setCity("Test City");
        testAddress.setState("Test State");
        testAddress.setZipCode("Test ZIP");
        testAddress.setCountry("Test Country");
        when(addressRepository.save(any(Address.class))).thenReturn(testAddress);
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(testAddress));
    }

    @Test
    public void whenSaveAddress_thenAddressShouldBeSaved() {
        // when
        Address savedAddress = addressService.save(testAddress);

        // then
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getStreet()).isEqualTo("Test Street");
        verify(addressRepository, times(1)).save(testAddress);
    }

    @Test
    public void whenFindById_thenReturnAddress() {
        // when
        Optional<Address> foundAddress = addressService.findById(testAddress.getId());

        // then
        assertThat(foundAddress).isPresent();
        assertThat(foundAddress.get().getStreet()).isEqualTo("Test Street");
        verify(addressRepository, times(1)).findById(testAddress.getId());
    }

    @Test
    public void whenDeleteById_thenAddressShouldBeDeleted() {
        // setup
        when(addressRepository.existsById(testAddress.getId())).thenReturn(true);

        // when
        addressService.deleteById(testAddress.getId());

        // then
        verify(addressRepository, times(1)).deleteById(testAddress.getId());
    }

    @Test
    public void whenUpdateAddress_thenAddressShouldBeUpdated() {
        // setup
        Address updatedDetails = new Address();
        updatedDetails.setStreet("Updated Street");
        updatedDetails.setCity("Updated City");
        updatedDetails.setState("Updated State");
        updatedDetails.setZipCode("Updated ZIP");
        updatedDetails.setCountry("Updated Country");

        when(addressRepository.findById(testAddress.getId())).thenReturn(Optional.of(testAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedDetails);

        // when
        Address updatedAddress = addressService.updateAddress(testAddress.getId(), updatedDetails);

        // then
        assertThat(updatedAddress.getStreet()).isEqualTo("Updated Street");
        verify(addressRepository, times(1)).save(testAddress);
    }
}
