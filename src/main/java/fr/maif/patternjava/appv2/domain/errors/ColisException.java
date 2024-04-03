package fr.maif.patternjava.appv2.domain.errors;

public class ColisException extends RuntimeException {
    public ColisException() {
    }

    public ColisException(String message) {
        super(message);
    }
}
