package ptc2025.backend.Exceptions;

public class ExceptionValidationError extends RuntimeException {

    public ExceptionValidationError(String message) {
        super(message);
    }
}
