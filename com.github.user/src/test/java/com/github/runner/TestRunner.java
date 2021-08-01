package com.github.runner;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(

		features="/src/test/resources/featurefile",
		glue= {"/src/test/java/com/github/step_definition"},
		format = {"pretty","html:test-output"},
		tags = {"@GitHubUserInfo"}
		)


public class TestRunner {

}
