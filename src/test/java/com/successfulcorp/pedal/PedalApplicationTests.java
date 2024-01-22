package com.successfulcorp.pedal;

import com.successfulcorp.pedal.domain.Address;
import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.domain.Person;
import com.successfulcorp.pedal.repository.AddressRepository;
import com.successfulcorp.pedal.repository.ContactRepository;
import com.successfulcorp.pedal.repository.PersonRepository;
import com.successfulcorp.pedal.service.AddressService;
import com.successfulcorp.pedal.service.ContactService;
import com.successfulcorp.pedal.service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PedalApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PedalApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ContactRepository contactRepository;

    @LocalServerPort
    private int port;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Configuration
    static class TestConfig {
        @Bean
        public ContactRepository contactRepository() {
            return Mockito.mock(ContactRepository.class);
        }

        @Bean
        public ContactService contactService() {
            return new ContactService(contactRepository());
        }

        @Bean
        public PersonRepository personRepository() {
            return Mockito.mock(PersonRepository.class);
        }

        @Bean
        public PersonService personService() {
            return new PersonService(personRepository());
        }

        @Bean
        public AddressRepository addressRepository() {
            return Mockito.mock(AddressRepository.class);
        }

        @Bean
        public AddressService addressService() {
            return new AddressService(addressRepository());
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        // Execute the SQL script to insert data
        // Assuming you have a data.sql file in your classpath
        Resource resource = new ClassPathResource("\\db\\data.sql");
        EncodedResource encodedResource = new EncodedResource(resource, StandardCharsets.UTF_8);
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), encodedResource);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the data from the database
        // Define your DELETE statements here to clean up the data
        jdbcTemplate.execute("DELETE FROM contact WHERE contact_detail LIKE 'test.%'");
        jdbcTemplate.execute("DELETE FROM person WHERE first_name LIKE 'Test%'");
        jdbcTemplate.execute("DELETE FROM address WHERE street LIKE 'Test%'");
        // Add more DELETE statements as needed for your test data
    }

    @Test
    public void testContactManipulation() {
        // Step 1: Query already existing contacts and list what was found
        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/api/contacts", List.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<Contact> originalContacts = response.getBody();
        assertThat(originalContacts).isNotNull();
        int originalSize = originalContacts.size();

        // Step 2: Create a new contact and search for it by ID
        Contact newContact = new Contact();
        newContact.setAddressId(1);
        newContact.setContactType("email");
        newContact.setContactDetail("new.contact@example.com");
        Contact createdContact = restTemplate.postForObject(getRootUrl() + "/api/contacts", newContact, Contact.class);
        assertThat(createdContact).isNotNull();
        assertThat(createdContact.getContactType()).isEqualTo(newContact.getContactType());

        // Retrieve the newly created contact by ID
        Contact retrievedContact = restTemplate.getForObject(getRootUrl() + "/api/contacts/" + createdContact.getId(), Contact.class);
        assertThat(retrievedContact).isNotNull();
        assertThat(retrievedContact.getId()).isEqualTo(createdContact.getId());

        // Step 3: Update the new contact and list all again
        retrievedContact.setContactDetail("updated.contact@example.com");
        restTemplate.exchange(getRootUrl() + "/api/contacts/" + retrievedContact.getId(), HttpMethod.PUT, new HttpEntity<>(retrievedContact), Contact.class);

        // Verify the update
        Contact updatedContact = restTemplate.getForObject(getRootUrl() + "/api/contacts/" + createdContact.getId(), Contact.class);
        assertThat(updatedContact).isNotNull();
        assertThat(updatedContact.getContactDetail()).isEqualTo("updated.contact@example.com");

        /// List all contacts and verify the count
        response = restTemplate.getForEntity(getRootUrl() + "/api/contacts", List.class);
        List<Contact> updatedContacts = response.getBody();
        assertThat(updatedContacts).isNotNull();
        assertThat(updatedContacts.size()).isEqualTo(originalSize + 1);

        // Step 4: Delete the created + updated contact
        restTemplate.delete(getRootUrl() + "/api/contacts/" + updatedContact.getId());

        // Verify the deletion
        ResponseEntity<Contact> deleteResponse = restTemplate.getForEntity(getRootUrl() + "/api/contacts/" + updatedContact.getId(), Contact.class);
        assertThat(deleteResponse.getStatusCode().is4xxClientError()).isTrue();

        // Verify the count after deletion
        response = restTemplate.getForEntity(getRootUrl() + "/api/contacts", List.class);
        List finalContacts = response.getBody();
        assertThat(finalContacts).isNotNull();
        assertThat(finalContacts.size()).isEqualTo(originalSize);

    }

    @Test
    public void testAddressManipulation() {
        // Get the root URL for the server
        String baseUrl = "http://localhost:" + port;

        // Step 1: Query already existing addresses
        ResponseEntity<Address[]> response = restTemplate.getForEntity(baseUrl + "/api/addresses", Address[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Address[] originalAddresses = response.getBody();
        int originalSize = originalAddresses != null ? originalAddresses.length : 0;

        // Step 2: Create a new address and verify
        Address newAddress = new Address("123 New Street", "NewCity", "NS", "12345", "NewCountry");
        Address createdAddress = restTemplate.postForObject(baseUrl + "/api/addresses", newAddress, Address.class);
        assertThat(createdAddress).isNotNull();
        assertThat(createdAddress.getStreet()).isEqualTo(newAddress.getStreet());

        // Verify new address is in the list
        response = restTemplate.getForEntity(baseUrl + "/api/addresses", Address[].class);
        assertThat(response.getBody()).hasSize(originalSize + 1);

        // Step 3: Update the address and verify
        createdAddress.setStreet("123 Updated Street");
        restTemplate.put(baseUrl + "/api/addresses/" + createdAddress.getId(), createdAddress);
        Address updatedAddress = restTemplate.getForObject(baseUrl + "/api/addresses/" + createdAddress.getId(), Address.class);
        assertThat(updatedAddress.getStreet()).isEqualTo("123 Updated Street");

        // Verify the address is updated in the list
        response = restTemplate.getForEntity(baseUrl + "/api/addresses", Address[].class);
        assertThat(response.getBody()).hasSize(originalSize + 1);

        // Step 4: Delete the created + updated address
        restTemplate.delete(baseUrl + "/api/addresses/" + createdAddress.getId());

        // Verify the address is removed from the list
        response = restTemplate.getForEntity(baseUrl + "/api/addresses", Address[].class);
        assertThat(response.getBody()).hasSize(originalSize);
    }
    @Test
    public void testPersonManipulation() {
        // Get the root URL for the server
        String baseUrl = "http://localhost:" + port;

        // Step 1: Query already existing persons
        ResponseEntity<Person[]> response = restTemplate.getForEntity(baseUrl + "/api/persons", Person[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Person[] originalPersons = response.getBody();
        int originalSize = originalPersons != null ? originalPersons.length : 0;

        // Step 2: Create a new person and verify
        Person newPerson = new Person("Test", "User");
        // set other properties of Person, if any
        Person createdPerson = restTemplate.postForObject(baseUrl + "/api/persons", newPerson, Person.class);
        assertThat(createdPerson).isNotNull();
        assertThat(createdPerson.getFirstName()).isEqualTo(newPerson.getFirstName());

        // Verify new person is in the list
        response = restTemplate.getForEntity(baseUrl + "/api/persons", Person[].class);
        assertThat(response.getBody()).hasSize(originalSize + 1);

        // Step 3: Update the person and verify
        createdPerson.setLastName("UpdatedUser");
        restTemplate.put(baseUrl + "/api/persons/" + createdPerson.getId(), createdPerson);
        Person updatedPerson = restTemplate.getForObject(baseUrl + "/api/persons/" + createdPerson.getId(), Person.class);
        assertThat(updatedPerson.getLastName()).isEqualTo("UpdatedUser");

        // Verify the person is updated in the list
        response = restTemplate.getForEntity(baseUrl + "/api/persons", Person[].class);
        assertThat(response.getBody()).hasSize(originalSize + 1);

        // Step 4: Delete the created + updated person
        restTemplate.delete(baseUrl + "/api/persons/" + createdPerson.getId());

        // Verify the person is removed from the list
        response = restTemplate.getForEntity(baseUrl + "/api/persons", Person[].class);
        assertThat(response.getBody()).hasSize(originalSize);
    }


}
