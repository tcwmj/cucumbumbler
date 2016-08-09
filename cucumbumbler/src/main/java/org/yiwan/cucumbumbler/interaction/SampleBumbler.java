package org.yiwan.cucumbumbler.interaction;

import java.util.Scanner;

/**
 * Created by Kenny Wang on 2/19/2016.
 */
public class SampleBumbler implements Bumbler {

    private String question = "Pass? [Y, N]";
    private String answer;
    private String pass = "Y";
    private boolean passed = false;

    public SampleBumbler() {
    }

    public SampleBumbler(String question) {
        this.question = question;
    }

    public SampleBumbler(String question, String pass) {
        this.question = question;
        this.pass = pass;
    }

    @Override
    public void ask() {
        ask(question);
    }

    @Override
    public void ask(String question) {
        System.out.print(question);
    }

    @Override
    public void answer() {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String response = in.nextLine();
            if (response.length() != 0) {
                if (response.toUpperCase().equals(pass)) {
                    passed = true;
                }
                answer = response;
                return;
            }
        }
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean isPassed() {
        return passed;
    }

    public static void main(String[] args) {
        Bumbler bumbler = new SampleBumbler();
        bumbler.ask();
        bumbler.answer();
        System.out.println(bumbler.isPassed());
        System.out.println(bumbler.getAnswer());
    }

}
