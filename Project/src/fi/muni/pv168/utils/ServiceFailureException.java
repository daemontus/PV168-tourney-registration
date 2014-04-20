package fi.muni.pv168.utils;

/**
 * Exception expressing some kind of service failure or database inconsistency
 *
 * @author Samuel Pastva
 * @version 29/03/2014
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

}
