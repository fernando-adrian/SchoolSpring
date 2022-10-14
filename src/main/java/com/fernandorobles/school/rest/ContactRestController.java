package com.fernandorobles.school.rest;

import com.fernandorobles.school.constants.SchoolConstants;
import com.fernandorobles.school.model.Contact;
import com.fernandorobles.school.model.Response;
import com.fernandorobles.school.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/contact", produces = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.APPLICATION_XML_VALUE
})
@CrossOrigin(origins = "*")
public class ContactRestController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/getMessagesByStatus")
    public List<Contact> getMessagesByStatus(@RequestParam(name = "status") String status){
        return contactRepository.findByStatus(status);
    }

    @GetMapping("/getAllMessagesByStatus")
    public List<Contact> getAllMessagesByStatus(@RequestBody Contact contact){
        if(contact != null && contact.getStatus() != null)
            return contactRepository.findByStatus(contact.getStatus());
        else
            return List.of();
    }

    @PostMapping("/saveMessage")
    public ResponseEntity<Response> saveMessage(
            @RequestHeader("invocationFrom") String invocationFrom,
            @Valid @RequestBody Contact contact){
        log.info(String.format("Header invocationFrom = %s", invocationFrom));
        contactRepository.save(contact);
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMessage("Message saved successfully");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("isMessageSaved", "true")
                .body(response);
    }

    @DeleteMapping("/deleteMessage")
    public ResponseEntity<Response> deleteMessage(RequestEntity<Contact> requestEntity){
        HttpHeaders headers = requestEntity.getHeaders();
        headers.forEach((key, value)->{
            log.info(String.format(
                    "Header '%s' = %s",
                    key, value.stream().collect(Collectors.joining("|"))));
        });
        Contact contact = requestEntity.getBody();
        contactRepository.deleteById(contact.getContactId());
        Response response = new Response();
        response.setStatusCode("200");
        response.setStatusMessage("Message successfully deleted");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PatchMapping("/closeMessage")
    public ResponseEntity<Response> closeMessage(@RequestBody Contact contactReq){
        Response response = new Response();
        Optional<Contact> contact
                = contactRepository.findById(contactReq.getContactId());
        if(contact.isPresent()){
            contact.get().setStatus(SchoolConstants.CLOSE);
            contactRepository.save(contact.get());
        }else{
            response.setStatusCode("400");
            response.setStatusMessage("Invalid Contact ID received");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
        response.setStatusCode("200");
        response.setStatusMessage("Message successfully closed");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
