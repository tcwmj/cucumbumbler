package org.yiwan.cucumber.runtime.model;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberBackground;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberTagStatement;
import cucumber.runtime.model.StepContainer;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Tag;
import org.yiwan.cucumber.runtime.SweetRuntime;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static org.yiwan.cucumber.runtime.model.CucumberFeatureWrapper.*;

/**
 * Created by Kenny Wang on 3/2/2016.
 */
public class CucumberScenarioWrapper {
    private final CucumberScenario cucumberScenario;

    public CucumberScenarioWrapper(CucumberScenario cucumberScenario) {
        this.cucumberScenario = cucumberScenario;
    }

    /**
     * This method is called when Cucumber is run from the CLI or JUnit
     */
    public void run(Formatter formatter, Reporter reporter, Runtime runtime) {
        Set<Tag> tags = tagsAndInheritedTags();
        runtime.buildBackendWorlds(reporter, tags, getScenario());
        formatter.startOfScenarioLifeCycle((Scenario) cucumberScenario.getGherkinModel());
        runtime.runBeforeHooks(reporter, tags);

        runBackground(formatter, reporter, runtime);
        format(cucumberScenario, formatter);

        if (SweetRuntime.MANUAL) {
            printTags(cucumberScenario.getGherkinModel().getTags());
            printBasicStatement(cucumberScenario.getGherkinModel());
            printDescription(cucumberScenario.getGherkinModel().getDescription());
            printComments(cucumberScenario.getGherkinModel().getComments());
        }
        runSteps(cucumberScenario, reporter, runtime);

        runtime.runAfterHooks(reporter, tags);
        formatter.endOfScenarioLifeCycle((Scenario) cucumberScenario.getGherkinModel());
        runtime.disposeBackendWorlds(createScenarioDesignation());
    }

    private void runBackground(Formatter formatter, Reporter reporter, Runtime runtime) {
        if (cucumberScenario.getCucumberBackground() != null) {
            if (SweetRuntime.MANUAL) {
                Background background = getBackground(cucumberScenario.getCucumberBackground());
                printBasicStatement(background);
                printDescription(background.getDescription());
                printComments(background.getComments());
            }
            format(cucumberScenario.getCucumberBackground(), formatter);
            runSteps(cucumberScenario.getCucumberBackground(), reporter, runtime);
        }
    }

    private Background getBackground(CucumberBackground cucumberBackground) {
        try {
            Field field = StepContainer.class.getDeclaredField("statement");
            field.setAccessible(true);
            return (Background) field.get(cucumberBackground);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }

    private String createScenarioDesignation() {
        try {
            Method method = CucumberScenario.class.getDeclaredMethod("createScenarioDesignation");
            method.setAccessible(true);
            return (String) method.invoke(cucumberScenario);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e);
        }
        return null;
    }

    private Set<Tag> tagsAndInheritedTags() {
        try {
            Method method = CucumberTagStatement.class.getDeclaredMethod("tagsAndInheritedTags");
            method.setAccessible(true);
            return (Set<Tag>) method.invoke(cucumberScenario);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e);
        }
        return null;
    }

    private void format(StepContainer stepContainer, Formatter formatter) {
        try {
            Method method = StepContainer.class.getDeclaredMethod("format", Formatter.class);
            method.setAccessible(true);
            method.invoke(stepContainer, formatter);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e);
        }
    }

    private void runSteps(StepContainer stepContainer, Reporter reporter, Runtime runtime) {
        try {
            Method method = StepContainer.class.getDeclaredMethod("runSteps", Reporter.class, Runtime.class);
            method.setAccessible(true);
            method.invoke(stepContainer, reporter, runtime);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e);
        }
    }

    private Scenario getScenario() {
        try {
            Field field = CucumberScenario.class.getDeclaredField("scenario");
            field.setAccessible(true);
            return (Scenario) field.get(cucumberScenario);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }
}
