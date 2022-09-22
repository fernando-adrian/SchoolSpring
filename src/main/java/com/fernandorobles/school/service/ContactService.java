package com.fernandorobles.school.service;

import com.fernandorobles.school.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ContactService {

    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = true;
        // TODO: 9/21/2022 - Need to persist the data into the DB
        log.info(contact.toString());
        return isSaved;
    }
}
