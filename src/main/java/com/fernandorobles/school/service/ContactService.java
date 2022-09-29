package com.fernandorobles.school.service;

import com.fernandorobles.school.constants.SchoolConstants;
import com.fernandorobles.school.model.Contact;
import com.fernandorobles.school.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
//@RequestScope
//@SessionScope
@ApplicationScope
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public ContactService() {
        System.out.println("Contact Service Bean initialized");
    }
    public boolean saveMessageDetails(Contact contact){

        contact.setStatus(SchoolConstants.OPEN);
        contact.setCreatedBy(SchoolConstants.ANONYMOUS);
        contact.setCreatedAt(LocalDateTime.now());
        int result = contactRepository.saveContactMsg(contact);

        return result > 0;
    }

    public List<Contact> findMessagesWithOpenStatus() {
        return contactRepository.findMessagesWithStatus(SchoolConstants.OPEN);
    }

    public boolean updateMessageStatus(int contactId, String updatedBy) {

        int result = contactRepository.updateMsgStatus(contactId, SchoolConstants.CLOSE, updatedBy);
        return result > 0;

    }
}
