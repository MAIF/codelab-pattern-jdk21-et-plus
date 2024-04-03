package fr.maif.patternjava.appv1.domain.errors;

public class ColisException extends RuntimeException {
    public ColisException() {
    }

    public ColisException(String message) {
        super(message);
    }
}
