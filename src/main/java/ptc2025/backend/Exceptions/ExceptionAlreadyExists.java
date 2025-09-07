package ptc2025.backend.Exceptions;

public class ExceptionAlreadyExists extends RuntimeException {
    public ExceptionAlreadyExists(String message) {
        super(message);
    }
}
