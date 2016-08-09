package org.yiwan.cucumber.runtime.model;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;

import java.util.List;

/**
 * Created by Kenny Wang on 3/2/2016.
 */
public class CucumberScenarioOutlineWrapper {

    private final CucumberScenarioOutline cucumberScenarioOutline;

    public CucumberScenarioOutlineWrapper(CucumberScenarioOutline cucumberScenarioOutline) {
        this.cucumberScenarioOutline = cucumberScenarioOutline;
    }

    public void run(Formatter formatter, Reporter reporter, Runtime runtime) {
        cucumberScenarioOutline.formatOutlineScenario(formatter);
        for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
            cucumberExamples.format(formatter);
            List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
            for (CucumberScenario exampleScenario : exampleScenarios) {
                new CucumberScenarioWrapper(exampleScenario).run(formatter, reporter, runtime);
            }
        }
    }
}
