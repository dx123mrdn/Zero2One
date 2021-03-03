package com.zero2one.rosefinch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping(value = "halo", method = RequestMethod.GET)
    public String halo() {
        return "Halo, Rosefinch ~";
    }
}
