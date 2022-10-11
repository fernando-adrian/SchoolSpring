package com.fernandorobles.school.controller;

import com.fernandorobles.school.model.Courses;
import com.fernandorobles.school.model.Person;
import com.fernandorobles.school.model.SchoolClass;
import com.fernandorobles.school.repository.CoursesRepository;
import com.fernandorobles.school.repository.PersonRepository;
import com.fernandorobles.school.repository.SchoolClassRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    SchoolClassRepository schoolClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model){
        List<SchoolClass> schoolClasses = schoolClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("schoolClasses", schoolClasses);
        modelAndView.addObject("schoolClass", new SchoolClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("schoolClass") SchoolClass schoolClass){
        schoolClassRepository.save(schoolClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id){
        Optional<SchoolClass> schoolClass = schoolClassRepository.findById(id);
        for (Person person : schoolClass.get().getPersons()){
           person.setSchoolClass(null);
           personRepository.save(person);
        }
        schoolClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<SchoolClass> schoolClass = schoolClassRepository.findById(classId);
        modelAndView.addObject("schoolClass", schoolClass.get());
        modelAndView.addObject("person", new Person());
        session.setAttribute("schoolClass", schoolClass.get());
        if(error != null){
            errorMessage = "Invalid email entered";
            modelAndView.addObject("errorMessage", error);
        }
        return modelAndView;
    }

    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person,
                                   HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        SchoolClass schoolClass = (SchoolClass) session.getAttribute("schoolClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId() > 0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + schoolClass.getClassId()
            + "&error=true");
            return modelAndView;
        }
        personEntity.setSchoolClass(schoolClass);
        personRepository.save(personEntity);
        schoolClass.getPersons().add(personEntity);
        schoolClassRepository.save(schoolClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + schoolClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session){
        SchoolClass schoolClass = (SchoolClass) session.getAttribute("schoolClass");
        Optional<Person> person = personRepository.findById(personId);
        person.get().setSchoolClass(null);
        schoolClass.getPersons().remove(person.get());
        SchoolClass schoolClassSaved = schoolClassRepository.save(schoolClass);
        session.setAttribute("schoolClass", schoolClassSaved);
        ModelAndView modelAndView =
                new ModelAndView("redirect:/admin/displayStudents?classId="
                        + schoolClass.getClassId());
        return  modelAndView;
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model){
//        List<Courses> courses = coursesRepository.findByOrderByNameDesc();
        List<Courses> courses = coursesRepository.findAll(Sort.by("name").ascending());
        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses", courses);
        model.addAttribute("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course){
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam int id,
                                     HttpSession session,
                                     @RequestParam(required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        Optional<Courses> course = coursesRepository.findById(id);
        modelAndView.addObject("courses", course.get());
        modelAndView.addObject("person", new Person());
        session.setAttribute("courses", course.get());
        if(error != null){
            errorMessage = "Invalid email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model,
                                           @ModelAttribute("person") Person person,
                                           HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) session.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity == null || !(personEntity.getPersonId() > 0)){
            modelAndView.setViewName(
                    "redirect:/admin/viewStudents?id=" + courses.getCourseId() +"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        session.setAttribute("courses", courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id=" + courses.getCourseId());
        return modelAndView;
    }

    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId,
                                                HttpSession session){
        Courses courses = (Courses) session.getAttribute("courses");
        Optional<Person> person = personRepository.findById(personId);
        person.get().getCourses().remove(courses);
        courses.getPersons().remove(person);
        personRepository.save(person.get());
        session.setAttribute("courses", courses);
        ModelAndView modelAndView =
                new ModelAndView("redirect:/admin/viewStudents?id=" + courses.getCourseId());
        return modelAndView;

    }
}
