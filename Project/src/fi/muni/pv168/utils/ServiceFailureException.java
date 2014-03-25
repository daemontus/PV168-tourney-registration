package fi.muni.pv168.utils;

/**
 * Created by daemontus on 24/03/14.
 */
public class ServiceFailureException extends RuntimeException {

    public ServiceFailureException() {
    }

    public ServiceFailureException(String message) {
        super(message);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
