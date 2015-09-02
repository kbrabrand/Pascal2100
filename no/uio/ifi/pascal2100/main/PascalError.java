package no.uio.ifi.pascal2100.main;

public class PascalError extends RuntimeException {
    // Since RuntimeException extends Exception and 
    // Exception implements Serializable, it should define this constant: 
    private static final long serialVersionUID = 20150629L;

    PascalError(String message) {
	super(message);
    }
}
