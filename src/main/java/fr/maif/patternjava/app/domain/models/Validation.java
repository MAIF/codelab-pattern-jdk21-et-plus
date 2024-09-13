package fr.maif.patternjava.app.domain.models;

import fr.maif.validations.Rule;
import io.vavr.collection.Seq;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Validation {


    @Value
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    public static class ValidationException extends RuntimeException {
        public List<String> errors;
    }

    static <T, E extends RuntimeException> void throwInvalid(Rule<T> rule, Function<Seq<T>, E> raise) {
        if(rule.isInvalid()) {
            throw raise.apply(rule.getErrors());
        }
    }

    static void throwInvalid(Rule<String> rule) {
        throwInvalid(rule, l -> new Validation.ValidationException(l.toJavaList()));
    }


    public static Rule<String> datePassee(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now())) {
            return Rule.valid();
        } else {
            return Rule.invalid("La date devrait être antérieure à aujourd'hui");
        }
    }

    public static Rule<String> doitEtreAvant(LocalDateTime before, LocalDateTime after, String message) {
        if (before.isBefore(after)) {
            return Rule.valid();
        } else {
            return Rule.invalid(message);
        }
    }

    public static String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static Rule<String> emailValid(String elt) {
        return pattern(elt, EMAIL_PATTERN, "Le format de l'email n'est pas valide");
    }

    public static <E> Rule<String> nonNull(E elt, String field) {
        if (elt == null) {
            return Rule.invalid("La valeur devrait être renseignée  pour "+field);
        } else {
            return Rule.valid();
        }
    }

    public static <E> Rule<String> nonNull(E elt) {
        if (elt == null) {
            return Rule.invalid("La valeur devrait être renseignée ");
        } else {
            return Rule.valid();
        }
    }

    public static <E> Rule<String> tailleMax(String elt, int max) {
        if (elt.length() > max) {
            return Rule.invalid("Le texte ne doit pas faire plus de %s".formatted(max));
        } else {
            return Rule.valid();
        }
    }

    public static <E> Rule<E> pattern(String elt, String pattern, E error) {
        Pattern compiled = Pattern.compile(pattern);
        if (compiled.matcher(elt).matches()) {
            return Rule.valid();
        } else {
            return Rule.invalid(error);
        }
    }

}
