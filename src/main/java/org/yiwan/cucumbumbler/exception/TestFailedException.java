package org.yiwan.cucumbumbler.exception;

/**
 * Created by Kenny Wang on 2/19/2016.
 */
public class TestFailedException extends RuntimeException {


    public TestFailedException() {
    }

    public TestFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TestFailedException(Throwable cause) {

        super(cause);
    }

    public TestFailedException(String message, Throwable cause) {

        super(message, cause);
    }

    public TestFailedException(String message) {

        super(message);
    }
}
