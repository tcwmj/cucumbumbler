package org.yiwan.cucumber.runtime;

import cucumber.api.StepDefinitionReporter;
import cucumber.runtime.*;
import cucumber.runtime.Runtime;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import gherkin.I18n;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;
import org.yiwan.cucumber.runtime.model.CucumberFeatureWrapper;
import org.yiwan.cucumbumbler.exception.ManualTestFailedException;
import org.yiwan.cucumbumbler.interaction.Bumbler;
import org.yiwan.cucumbumbler.interaction.SampleBumbler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Created by Kenny Wang on 2/18/2016.
 */
public class SweetRuntime extends Runtime {
    public final static boolean MANUAL = Boolean.parseBoolean(System.getProperty("manual", "false"));

    public SweetRuntime(ResourceLoader resourceLoader, ClassFinder classFinder, ClassLoader classLoader, RuntimeOptions runtimeOptions) {
        super(resourceLoader, classFinder, classLoader, runtimeOptions);
    }

    public SweetRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, Collection<? extends Backend> backends, RuntimeOptions runtimeOptions) {
        super(resourceLoader, classLoader, backends, runtimeOptions);
    }

    public SweetRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, Collection<? extends Backend> backends,
                        RuntimeOptions runtimeOptions, RuntimeGlue optionalGlue) {
        super(resourceLoader, classLoader, backends, runtimeOptions, optionalGlue);
    }

    public SweetRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, Collection<? extends Backend> backends, RuntimeOptions runtimeOptions, StopWatch stopWatch, RuntimeGlue optionalGlue) {
        super(resourceLoader, classLoader, backends, runtimeOptions, stopWatch, optionalGlue);
    }

    /**
     * This is the main entry point. Used from CLI, but not from JUnit.
     */
    @Override
    public void run() throws IOException {
        // Make sure all features parse before initialising any reporters/formatters
        List<CucumberFeature> features = getRuntimeOptions().cucumberFeatures(getResourceLoader());

        // TODO: This is duplicated in cucumber.api.android.CucumberInstrumentationCore - refactor or keep uptodate

        Formatter formatter = getRuntimeOptions().formatter(getClassLoader());
        Reporter reporter = getRuntimeOptions().reporter(getClassLoader());
        StepDefinitionReporter stepDefinitionReporter = getRuntimeOptions().stepDefinitionReporter(getClassLoader());

        getGlue().reportStepDefinitions(stepDefinitionReporter);

        for (CucumberFeature cucumberFeature : features) {
            new CucumberFeatureWrapper(cucumberFeature).run(formatter, reporter, this);
        }

        formatter.done();
        formatter.close();
        printSummary();
    }

    @Override
    public void runStep(String featurePath, Step step, Reporter reporter, I18n i18n) {
        if (MANUAL) {
            if (getSkipNextStep()) {
                addStepToCounterAndResult(Result.SKIPPED);
                reporter.result(Result.SKIPPED);
            } else {
                Bumbler bumbler = new SampleBumbler();
                bumbler.ask(step.getKeyword() + step.getName());
                bumbler.ask();
                getStopWatch().start();
                bumbler.answer();
                long duration = getStopWatch().stop();
                Result result;
                if (bumbler.isPassed()) {
                    result = new Result(Result.PASSED, duration, null, getDUMMY_ARG());
                } else {
                    Throwable error = new ManualTestFailedException(bumbler.getAnswer());
                    result = new Result(Result.FAILED, duration, error, getDUMMY_ARG());
                    setSkipNextStep(true);
                }
                addStepToCounterAndResult(result);
                reporter.result(result);
            }
        } else {
            super.runStep(featurePath, step, reporter, i18n);
        }
    }

    private boolean getSkipNextStep() {
        try {
            Field field = Runtime.class.getDeclaredField("skipNextStep");
            field.setAccessible(true);
            return field.getBoolean(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return false;
    }

    private void setSkipNextStep(boolean skipNextStep) {
        try {
            Field field = Runtime.class.getDeclaredField("skipNextStep");
            field.setAccessible(true);
            field.setBoolean(this, skipNextStep);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
    }

    private StopWatch getStopWatch() {
        try {
            Field field = Runtime.class.getDeclaredField("stopWatch");
            field.setAccessible(true);
            return (StopWatch) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }

    private RuntimeOptions getRuntimeOptions() {
        try {
            Field field = Runtime.class.getDeclaredField("runtimeOptions");
            field.setAccessible(true);
            return (RuntimeOptions) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }

    private ResourceLoader getResourceLoader() {
        try {
            Field field = Runtime.class.getDeclaredField("resourceLoader");
            field.setAccessible(true);
            return (ResourceLoader) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }

    private ClassLoader getClassLoader() {
        try {
            Field field = Runtime.class.getDeclaredField("classLoader");
            field.setAccessible(true);
            return (ClassLoader) field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }

    private Object getDUMMY_ARG() {
        try {
            Field field = Runtime.class.getDeclaredField("DUMMY_ARG");
            field.setAccessible(true);
            return field.get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            System.err.println(e);
        }
        return null;
    }

    private void addStepToCounterAndResult(Result result) {
        try {
            Method method = Runtime.class.getDeclaredMethod("addStepToCounterAndResult", Result.class);
            method.setAccessible(true);
            method.invoke(this, result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e);
        }
    }

}
