package org.yiwan.cucumber.api.junit;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.junit.runners.model.InitializationError;
import org.yiwan.cucumber.runtime.SweetRuntime;

import java.io.IOException;

/**
 * Created by Kenny Wang on 2/18/2016.
 */
public class SweetCucumber extends Cucumber {
    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws IOException         if there is a problem
     * @throws InitializationError if there is another problem
     */
    public SweetCucumber(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }

    /**
     * Create the Runtime. Can be overridden to customize the runtime or backend.
     *
     * @param resourceLoader used to load resources
     * @param classLoader    used to load classes
     * @param runtimeOptions configuration
     * @return a new runtime
     * @throws InitializationError if a JUnit error occurred
     * @throws IOException         if a class or resource could not be loaded
     */
    @Override
    protected SweetRuntime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
                                         RuntimeOptions runtimeOptions) throws InitializationError, IOException {
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        return new SweetRuntime(resourceLoader, classFinder, classLoader, runtimeOptions);
    }
}
