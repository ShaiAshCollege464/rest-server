package com.dev.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestController {



    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test () {
        return "test";
    }





}
