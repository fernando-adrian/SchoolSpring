package com.fernandorobles.school.controller;

import com.fernandorobles.school.model.Person;
import com.fernandorobles.school.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class DashboardController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session){
        Person person = personRepository.readByEmail(authentication.getName());
        model.addAttribute("username", person.getName());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        if (person.getSchoolClass() != null && person.getSchoolClass().getName() != null){
            model.addAttribute("enrolledClass", person.getSchoolClass().getName());
        }
        session.setAttribute("loggedInPerson", person);
        logMessages();
        return "dashboard.html";
    }

    private void logMessages(){
        log.error("error message from dashboard");
        log.warn("warn message from dashboard");
        log.info("info message from dashboard");
        log.debug("debug message from dashboard");
        log.trace("trace message from dashboard");

    }
}
