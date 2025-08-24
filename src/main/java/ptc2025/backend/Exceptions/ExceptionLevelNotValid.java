package ptc2025.backend.Exceptions;

public class ExceptionLevelNotValid extends RuntimeException {
    private String LevelNotValid;

    public ExceptionLevelNotValid(String message) {
        super(message);
    }
}