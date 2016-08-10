package org.yiwan.cucumbumbler.exception;

/**
 * Created by Kenny Wang on 2/19/2016.
 */
public class ManualTestFailedException extends TestFailedException {


    public ManualTestFailedException() {
    }

    public ManualTestFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ManualTestFailedException(Throwable cause) {

        super(cause);
    }

    public ManualTestFailedException(String message, Throwable cause) {

        super(message, cause);
    }

    public ManualTestFailedException(String message) {

        super(message);
    }
}
