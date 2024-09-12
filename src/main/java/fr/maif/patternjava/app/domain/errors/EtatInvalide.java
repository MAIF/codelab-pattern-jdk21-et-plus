package fr.maif.patternjava.app.domain.errors;

public class EtatInvalide extends ColisException {
    public EtatInvalide() {
    }

    public EtatInvalide(String message) {
        super(message);
    }
}
