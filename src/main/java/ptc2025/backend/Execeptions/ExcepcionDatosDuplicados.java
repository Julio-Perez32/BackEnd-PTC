package ptc2025.backend.Execeptions;

import lombok.Getter;

public class ExcepcionDatosDuplicados extends RuntimeException {
    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicados(String message, String campoDuplicado)
    {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }
}
