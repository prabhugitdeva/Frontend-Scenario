package com.example.tests;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "json:target/cucumber.json"},
    features = "src/test/resources/features",
    glue = {"com.example.tests.steps"},
    monochrome = true
)
public class RunCucumberTest { }
