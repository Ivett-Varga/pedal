package com.successfulcorp.pedal.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class Person extends BaseEntity{

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "First name must not be blank")
    @Size(max = 100, message = "First name too long")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name must not be blank")
    @Size(max = 100, message = "Last name too long")
    private String lastName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permanent_address_id", referencedColumnName = "id", nullable = false)
    private Address permanentAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "temporary_address_id", referencedColumnName = "id")
    private Address temporaryAddress;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
