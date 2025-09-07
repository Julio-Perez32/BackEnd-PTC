package ptc2025.backend.Exceptions;

public class ExceptionBadRequest extends RuntimeException {
    public ExceptionBadRequest(String message) {
        super(message);
    }
}
