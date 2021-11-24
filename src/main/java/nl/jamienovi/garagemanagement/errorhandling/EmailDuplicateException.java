package nl.jamienovi.garagemanagement.errorhandling;

/**
 * Throws exception when email is posted that already existst in the database
 *
 * @author J.Spekman
 */
public class EmailDuplicateException extends RuntimeException{

    public EmailDuplicateException(String message) {
        super(message);
    }

    public EmailDuplicateException(String message, Throwable cause) {
        super(message,cause);
    }
}
