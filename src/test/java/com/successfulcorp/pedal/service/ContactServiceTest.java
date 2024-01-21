package com.successfulcorp.pedal.service;

import com.successfulcorp.pedal.TestHelper;
import com.successfulcorp.pedal.domain.Contact;
import com.successfulcorp.pedal.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    private Contact testContact;

    @BeforeEach
    public void setUp() {
        TestHelper testHelper = new TestHelper(null, null, contactRepository);
        testContact = new Contact();
        testContact.setId(1);
        testContact.setContactType("phone");
        testContact.setContactDetail("+1234567890");
        when(contactRepository.save(any(Contact.class))).thenReturn(testContact);
        when(contactRepository.findById(anyInt())).thenReturn(Optional.of(testContact));
    }

    @Test
    public void whenSaveContact_thenContactShouldBeSaved() {
        // when
        Contact savedContact = contactService.save(testContact);

        // then
        assertThat(savedContact).isNotNull();
        assertThat(savedContact.getContactType()).isEqualTo("phone");
        assertThat(savedContact.getContactDetail()).isEqualTo("+1234567890");
        verify(contactRepository, times(1)).save(testContact);
    }

    @Test
    public void whenFindById_thenReturnContact() {
        // when
        Optional<Contact> foundContact = contactService.findById(testContact.getId());

        // then
        assertThat(foundContact).isPresent();
        assertThat(foundContact.get().getContactType()).isEqualTo("phone");
        assertThat(foundContact.get().getContactDetail()).isEqualTo("+1234567890");
        verify(contactRepository, times(1)).findById(testContact.getId());
    }

    @Test
    public void whenDeleteById_thenContactShouldBeDeleted() {
        // setup
        when(contactRepository.existsById(testContact.getId())).thenReturn(true);

        // when
        contactService.deleteById(testContact.getId());

        // then
        verify(contactRepository, times(1)).deleteById(testContact.getId());
    }

    @Test
    public void whenUpdateContact_thenContactShouldBeUpdated() {
        // setup
        Contact updatedDetails = new Contact();
        updatedDetails.setContactType("email");
        updatedDetails.setContactDetail("john.doe@example.com");

        when(contactRepository.findById(testContact.getId())).thenReturn(Optional.of(testContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(updatedDetails);

        // when
        Contact updatedContact = contactService.updateContact(testContact.getId(), updatedDetails);

        // then
        assertThat(updatedContact.getContactType()).isEqualTo("email");
        assertThat(updatedContact.getContactDetail()).isEqualTo("john.doe@example.com");
        verify(contactRepository, times(1)).save(testContact);
    }
}
