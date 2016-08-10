package org.yiwan.cucumbumbler.interaction;

/**
 * Created by Kenny Wang on 2/19/2016.
 */
public interface Bumbler {

    public void ask();

    public void ask(String question);

    public void answer();

    public String getAnswer();

    public boolean isPassed();

}