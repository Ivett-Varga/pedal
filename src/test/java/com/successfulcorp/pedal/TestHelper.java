package com.successfulcorp.pedal;

import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.repository.AddressRepository;
import com.successfulcorp.pedal.repository.ContactRepository;
import com.successfulcorp.pedal.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TestHelper {

    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final ContactRepository contactRepository;

    // Predefined lists of entities
    private static final List<Contact> PREDEFINED_CONTACTS = Arrays.asList(
            new Contact("phone","+44 1 111 111"),
            new Contact("mobile", "+44 30 111 1111"),
            new Contact("email", "sherlock.holmes@conandoyle.com")
    );

    private static final List<Address> PREDEFINED_ADDRESSES = Arrays.asList(
            new Address("Downing Street 10","London", "England", "SW1A 2AB", "UK"),
            new Address("Baker Street 221B","London", "England", "NW1 6XE", "UK"),
            new Address("Baker Street 221C","London", "England", "NW1 6XE", "UK")
    );

    private static final List<Person> PREDEFINED_PEOPLE = Arrays.asList(
            new Person("John", "Doe"),
            new Person("Jane","Eyre"),
            new Person("Sherlock", "Holmes")
    );

    public TestHelper(AddressRepository addressRepository, PersonRepository personRepository, ContactRepository contactRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.contactRepository = contactRepository;
    }

    public Address createAndPersistAddress(String street, String city, String state, String zipCode, String country) {
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZipCode(zipCode);
        address.setCountry(country);
        return addressRepository.save(address);
    }

    public Person createAndPersistPerson(String firstName, String lastName, Address permanentAddress, Address temporaryAddress) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setPermanentAddress(permanentAddress);
        person.setTemporaryAddress(temporaryAddress);
        return personRepository.save(person);
    }

    public Contact createAndPersistContact(Integer addressId, String contactType, String contactDetail) {
        Contact contact = new Contact();
        contact.setAddressId(addressId);
        contact.setContactType(contactType);
        contact.setContactDetail(contactDetail);
        return contactRepository.save(contact);
    }


    public List<Contact> createAndPersistContacts(int numberOfContacts) {
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < numberOfContacts; i++) {
            Contact contactTemplate = PREDEFINED_CONTACTS.get(i % PREDEFINED_CONTACTS.size());
            Contact contact = new Contact(contactTemplate.getAddressId(), contactTemplate.getContactType(), contactTemplate.getContactDetail());
            contacts.add(contactRepository.save(contact));// Persist to generate ID
        }
        return contacts;
    }

    public List<Address> createAndPersistAddresses(int numberOfAddresses, List<Contact> contacts) {
        List<Address> addresses = new ArrayList<>();
        for (int i = 0; i < numberOfAddresses; i++) {
            Address addressTemplate = PREDEFINED_ADDRESSES.get(i % PREDEFINED_ADDRESSES.size());
            Address address = new Address(addressTemplate.getStreet(), addressTemplate.getCity(), addressTemplate.getState(), addressTemplate.getZipCode(), addressTemplate.getCountry());
            address = addressRepository.save(address); // Persist to generate ID

            // Associate contacts with this address
            for (Contact contact : contacts) {
                contact.setAddressId(address.getId());
                contactRepository.save(contact); // Update contacts with address ID
            }

            addresses.add(address);
        }
        return addresses;
    }

    public List<Person> createAndPersistPeopleWithAddresses(int numberOfPeople, List<Address> addresses) {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < numberOfPeople; i++) {
            Person personTemplate = PREDEFINED_PEOPLE.get(i % PREDEFINED_PEOPLE.size());
            Person person = new Person(personTemplate.getFirstName(), personTemplate.getLastName(), addresses.get(i % addresses.size()), null);
            people.add(personRepository.save(person));// Persist to generate ID
        }
        return people;
    }
}
