package com.fernandorobles.school.controller;

import com.fernandorobles.school.model.Holiday;
import com.fernandorobles.school.repository.HolidaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class HolidaysController {

    @Autowired
    private HolidaysRepository holidaysRepository;

    @GetMapping("/holidays/{display}")
    public String displayHolidays(@PathVariable String display, Model model){

        if(display != null){
            if(display.equals("federal") || display.equals("all"))
                model.addAttribute("federal", true);

            if(display.equals("festival") || display.equals("all"))
                model.addAttribute("festival", true);
        }

        Iterable<Holiday> holidays = holidaysRepository.findAll();
        List<Holiday> holidayList = StreamSupport
                .stream(holidays.spliterator(), false).toList();
        Holiday.Type[] types = Holiday.Type.values();
        for(Holiday.Type type: types){
            model.addAttribute(
                    type.toString(),
                    holidayList.stream()
                            .filter(holiday -> holiday.getType().equals(type))
                            .collect(Collectors.toList())
            );
        }
        return "holidays.html";
    }
}
