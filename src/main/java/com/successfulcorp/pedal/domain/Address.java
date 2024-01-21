package com.successfulcorp.pedal.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address extends BaseEntity {

    @Column(name = "street", nullable = false)
    @NotBlank(message = "Street must not be blank")
    @Size(max = 255, message = "Street name too long")
    private String street;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City must not be blank")
    @Size(max = 100, message = "City name too long")
    private String city;

    @Column(name = "state", nullable = false)
    @NotBlank(message = "State must not be blank")
    @Size(max = 100, message = "State name too long")
    private String state;

    @Column(name = "zip_code", nullable = false)
    @NotBlank(message = "Zip code must not be blank")
    @Size(max = 10, message = "Zip code too long")
    private String zipCode;

    @Column(name = "country", nullable = false)
    @NotBlank(message = "Country must not be blank")
    @Size(max = 100, message = "Country name too long")
    private String country;

}
