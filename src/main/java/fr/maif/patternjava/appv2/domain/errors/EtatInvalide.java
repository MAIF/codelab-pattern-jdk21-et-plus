package fr.maif.patternjava.appv2.domain.errors;

public class EtatInvalide extends ColisException {
    public EtatInvalide() {
    }

    public EtatInvalide(String message) {
        super(message);
    }
}
