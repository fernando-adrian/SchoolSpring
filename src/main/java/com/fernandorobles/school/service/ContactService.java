package com.fernandorobles.school.service;

import com.fernandorobles.school.config.SchoolProps;
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

@Slf4j
@Service
@ApplicationScope
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private SchoolProps schoolProps;

    public ContactService() {
        System.out.println("Contact Service Bean initialized");
    }
    public boolean saveMessageDetails(Contact contact){

        contact.setStatus(SchoolConstants.OPEN);
        Contact savedContact = contactRepository.save(contact);

        return savedContact.getContactId() > 0;
    }

    public Page<Contact> findMessagesWithOpenStatus(
            int pageNum, String sortField, String sortDir) {
        int pageSize = schoolProps.getPageSize();
        if (schoolProps.getContact() != null && schoolProps.getContact().get("pageSize") != null){
            pageSize = Integer.parseInt(schoolProps.getContact().get("pageSize").trim());
        }
        Pageable pageable =
                PageRequest.of(pageNum - 1, pageSize,
                        sortDir.equals("asc") ?
                                Sort.by(sortField).ascending() :
                                Sort.by(sortField).descending());
        return contactRepository.findByStatusWithQuery(SchoolConstants.OPEN, pageable);
    }

    public boolean updateMessageStatus(int contactId) {

        return contactRepository.updateMsgStatus(SchoolConstants.CLOSE, contactId) > 0;
    }
}
