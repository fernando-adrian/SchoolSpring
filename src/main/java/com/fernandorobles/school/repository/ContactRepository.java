package com.fernandorobles.school.repository;

import com.fernandorobles.school.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    List<Contact> findByStatus(String status);

    Page<Contact> findByStatus(String status, Pageable pageable);
}
