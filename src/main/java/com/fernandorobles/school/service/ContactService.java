package com.fernandorobles.school.service;

import com.fernandorobles.school.constants.SchoolConstants;
import com.fernandorobles.school.model.Contact;
import com.fernandorobles.school.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Contact> findMessagesWithOpenStatus(int pageNum, String sortField,
                                                    String sortDir) {
        int pageSize = 5;
        Pageable pageable =
                PageRequest.of(pageNum - 1, pageSize,
                        sortDir.equals("asc") ?
                                Sort.by(sortField).ascending() :
                                Sort.by(sortField).descending());
        return contactRepository.findByStatus(SchoolConstants.OPEN, pageable);
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
