package com.successfulcorp.pedal.domain;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact")
public class Contact extends BaseEntity{

    @Column(name = "address_id", nullable = false)
    private Integer addressId; // This is just an integer referencing the address ID


    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "contact_detail", nullable = false)
    private String contactDetail;

    // Constructor without addressId, for predefined entities
    public Contact(String contactType, String contactDetail) {
        this.contactType = contactType;
        this.contactDetail = contactDetail;
    }

}