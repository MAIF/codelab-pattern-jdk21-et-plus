package fr.maif.patternjava.app.domain.errors;

public class ColisException extends RuntimeException {
    public ColisException() {
    }

    public ColisException(String message) {
        super(message);
    }
}
