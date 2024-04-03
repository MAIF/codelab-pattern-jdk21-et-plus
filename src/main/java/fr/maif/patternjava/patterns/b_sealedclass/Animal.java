package fr.maif.patternjava.patterns.b_sealedclass;

public sealed interface Animal {

    String nom();

    record Chat(String nom) implements Animal {}
    record Chien(String nom, Integer age) implements Animal {}
}
