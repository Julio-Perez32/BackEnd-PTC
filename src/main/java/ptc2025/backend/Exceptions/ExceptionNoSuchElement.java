package ptc2025.backend.Exceptions;

public class ExceptionNoSuchElement extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExceptionNoSuchElement() {
        super("No existen registros para la búsqueda solicitada.");
    }

    public ExceptionNoSuchElement(String message) {
        super(message);
    }

    public ExceptionNoSuchElement(String message, Throwable cause) {
        super(message, cause);
    }
}
