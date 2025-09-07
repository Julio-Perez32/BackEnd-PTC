package ptc2025.backend.Exceptions;

public class ExceptionServerError extends RuntimeException {

    public ExceptionServerError(String message) {
        super(message);
    }

    public ExceptionServerError(String message, Throwable cause) {
        super(message, cause);
    }
}