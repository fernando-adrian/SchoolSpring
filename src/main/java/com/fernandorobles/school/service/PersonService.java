package com.fernandorobles.school.service;

import com.fernandorobles.school.constants.SchoolConstants;
import com.fernandorobles.school.model.Person;
import com.fernandorobles.school.model.Roles;
import com.fernandorobles.school.repository.PersonRepository;
import com.fernandorobles.school.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean createNewPerson(Person person){
        Roles role = rolesRepository.getByRoleName(SchoolConstants.STUDENT_ROLE);
        person.setRoles(role);
        person.setPwd(passwordEncoder.encode(person.getPwd()));
        person = personRepository.save(person);
        return person.getPersonId() > 0;
    }
}
