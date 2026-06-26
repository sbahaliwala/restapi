package com.example.restapi.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberSpringConfiguration.TestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CucumberSpringConfiguration {

    @Configuration
    @ComponentScan(basePackages = {
        "com.example.restapi",
        "com.example.restapi.context"
    })
    public static class TestConfig {
    }
}
