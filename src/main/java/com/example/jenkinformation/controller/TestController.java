package com.example.jenkinformation.controller;

import com.example.jenkinformation.service.JenkinService;


import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
@RestController

public class TestController {

    private  JenkinService jenkinService;

    public TestController(JenkinService jenkinService) {
        this.jenkinService = jenkinService;
    }

    @GetMapping("/jenkins/test")
    public JsonNode testJenkinsApi() {
        return jenkinService.callJenkinsApi();
    }
}
