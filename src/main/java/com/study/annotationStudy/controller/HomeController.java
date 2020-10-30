package com.study.annotationStudy.controller;

import com.study.annotationStudy.dto.Guest;
import com.study.annotationStudy.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {
    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @RequestMapping("")
    public ModelAndView home(ModelAndView modelAndView) {
        List<Guest> guests = homeService.getGuests();

        modelAndView.addObject("guests", guests);
        modelAndView.setViewName("home");

        return modelAndView;
    }

    @RequestMapping("readOnly")
    public ModelAndView homeReadOnly(ModelAndView modelAndView) {
        List<Guest> guests = homeService.getGuestsReadOnly();

        modelAndView.addObject("guests", guests);
        modelAndView.setViewName("home");

        return modelAndView;
    }
}
