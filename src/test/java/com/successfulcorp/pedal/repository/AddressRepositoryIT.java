package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.domain.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AddressRepositoryIT {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void whenFindById_thenReturnAddress() {
        // given
        Address address = new Address(/* set properties */);
        address = addressRepository.save(address);

        // when
        Optional<Address> foundAddress = addressRepository.findById(address.getId());

        // then
        assertTrue(foundAddress.isPresent(), "Address should be present");
        assertThat(foundAddress.get().getId()).isEqualTo(address.getId());
    }

}
