package fr.maif.patternjava.patterns.a_records;

import java.util.Objects;

public record MonRecord(String attribut1) implements MonInterface {
    public MonRecord {
        attribut1 = Objects.requireNonNullElse(attribut1, "default");
    }
}
