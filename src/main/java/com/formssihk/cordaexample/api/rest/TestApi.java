package com.formssihk.cordaexample.api.rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/devOnly")
public class TestApi {

    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }

}
