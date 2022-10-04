package com.fernandorobles.school.service;

import com.fernandorobles.school.constants.SchoolConstants;
import com.fernandorobles.school.model.Contact;
import com.fernandorobles.school.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@ApplicationScope
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public ContactService() {
        System.out.println("Contact Service Bean initialized");
    }
    public boolean saveMessageDetails(Contact contact){

        contact.setStatus(SchoolConstants.OPEN);
        Contact savedContact = contactRepository.save(contact);

        return savedContact.getContactId() > 0;
    }

    public List<Contact> findMessagesWithOpenStatus() {

        return contactRepository.findByStatus(SchoolConstants.OPEN);
    }

    public boolean updateMessageStatus(int contactId) {

        Optional<Contact> contact = contactRepository.findById(contactId);
        contact.ifPresent( contactFound -> {
            contactFound.setStatus(SchoolConstants.CLOSE);
        });
        Contact updatedContact = contactRepository.save(contact.get());

        return updatedContact.getUpdatedBy() != null;

    }
}
