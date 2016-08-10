package org.yiwan.cucumber.runtime.model;

import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.BasicStatement;
import gherkin.formatter.model.Comment;
import gherkin.formatter.model.Tag;
import org.yiwan.cucumber.runtime.SweetRuntime;
import org.yiwan.cucumbumbler.interaction.Bumbler;
import org.yiwan.cucumbumbler.interaction.SampleBumbler;

import java.util.List;

/**
 * Created by Kenny Wang on 3/2/2016.
 */
public class CucumberFeatureWrapper {
    private final CucumberFeature cucumberFeature;

    public CucumberFeatureWrapper(CucumberFeature cucumberFeature) {
        this.cucumberFeature = cucumberFeature;
    }

    public void run(Formatter formatter, Reporter reporter, SweetRuntime runtime) {
        if (runtime.MANUAL) {
            printTags(cucumberFeature.getGherkinFeature().getTags());
            printBasicStatement(cucumberFeature.getGherkinFeature());
            printDescription(cucumberFeature.getGherkinFeature().getDescription());
            printComments(cucumberFeature.getGherkinFeature().getComments());
        }

        formatter.uri(cucumberFeature.getPath());
        formatter.feature(cucumberFeature.getGherkinFeature());

        for (CucumberTagStatement cucumberTagStatement : cucumberFeature.getFeatureElements()) {
            //Run the scenario, it should handle before and after hooks
            new CucumberTagStatementWrapper(cucumberTagStatement).run(formatter, reporter, runtime);
        }
        formatter.eof();

    }

    public static void printBasicStatement(BasicStatement basicStatement) {
        Bumbler bumbler = new SampleBumbler();
        bumbler.ask(basicStatement.getKeyword() + " " + basicStatement.getName() + "\n");
    }

    public static void printTags(List<Tag> tags) {
        Bumbler bumbler = new SampleBumbler();
        bumbler.ask(getTagsString(tags) + "\n");
    }

    public static void printDescription(String description) {
        Bumbler bumbler = new SampleBumbler();
        bumbler.ask(description + "\n\n");
    }

    public static void printComments(List<Comment> comments) {
        Bumbler bumbler = new SampleBumbler();
        bumbler.ask(getCommentsString(comments) + (comments.size() > 0 ? "\n\n" : ""));
    }

    private static String getTagsString(List<Tag> tags) {
        StringBuffer tagsStringBuffer = new StringBuffer();
        for (Tag tag : tags) {
            tagsStringBuffer.append(tag.getName());
        }
        return tagsStringBuffer.toString();
    }

    private static String getCommentsString(List<Comment> comments) {
        StringBuffer coomentsStringBuffer = new StringBuffer();
        for (Comment comment : comments) {
            coomentsStringBuffer.append(comment.getValue());
        }
        return coomentsStringBuffer.toString();
    }
}
