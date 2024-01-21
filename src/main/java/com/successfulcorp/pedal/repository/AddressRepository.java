package com.successfulcorp.pedal.repository;

import com.successfulcorp.pedal.domain.Address;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @NotNull Optional<Address> findById(Integer Id);

}