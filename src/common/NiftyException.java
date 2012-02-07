package common;

// Essentially just a RuntimeException with a more convenient constructor
@SuppressWarnings("serial")
public class NiftyException extends RuntimeException {
    public NiftyException(String format, Object... args) {
        super(String.format(format, args));
    }

    public NiftyException(Throwable cause, String format, Object... args) {
        super(String.format(format, args), cause);
    }
}
