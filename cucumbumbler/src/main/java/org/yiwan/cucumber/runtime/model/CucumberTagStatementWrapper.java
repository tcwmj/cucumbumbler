package org.yiwan.cucumber.runtime.model;

import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import org.yiwan.cucumber.runtime.SweetRuntime;

/**
 * Created by Kenny Wang on 3/2/2016.
 */
public class CucumberTagStatementWrapper {

    private final CucumberTagStatement cucumberTagStatement;


    public CucumberTagStatementWrapper(CucumberTagStatement cucumberTagStatement) {
        this.cucumberTagStatement = cucumberTagStatement;
    }

    /**
     * This method is called when Cucumber is run from the CLI or JUnit
     */
    public void run(Formatter formatter, Reporter reporter, SweetRuntime runtime) {
        if (SweetRuntime.MANUAL) {
            if (cucumberTagStatement instanceof CucumberScenario) {
                CucumberScenario cucumberScenario = (CucumberScenario) cucumberTagStatement;
                new CucumberScenarioWrapper(cucumberScenario).run(formatter, reporter, runtime);
            } else if (cucumberTagStatement instanceof CucumberScenarioOutline) {
                CucumberScenarioOutline cucumberScenarioOutline = (CucumberScenarioOutline) cucumberTagStatement;
                new CucumberScenarioOutlineWrapper(cucumberScenarioOutline).run(formatter, reporter, runtime);
            }
        } else {
            cucumberTagStatement.run(formatter, reporter, runtime);
        }
    }
}
