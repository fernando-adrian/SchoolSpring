package com.fernandorobles.school.service;

import com.fernandorobles.school.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Slf4j
@Service
//@RequestScope
//@SessionScope
@ApplicationScope
public class ContactService {



    private int counter = 0;
    public ContactService() {
        System.out.println("Contact Service Bean initialized");
    }
    public boolean saveMessageDetails(Contact contact){
        boolean isSaved = true;
        // TODO: 9/21/2022 - Need to persist the data into the DB
        log.info(contact.toString());
        return isSaved;
    }
    public int incrementCounter(){
        return counter++;
    }
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
