package fr.maif.patternjava.patterns.c_patternmatching;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PatternMatchingMain {

    public sealed interface Animal {

        String nom();

        record Chat(String nom) implements Animal {}
        record Chien(String nom, Integer age) implements Animal {
            public Chien(String nom) {
                this(nom, 42);
            }
        }
    }

    public static void main(String[] args) {

        // If instanceof
        Object test = "";
        if (test instanceof String string) {
            System.out.println("C'est un string "+string);
        }


        var chien = new Animal.Chien("medor");

        // EN jdk 22 et en activant les preview
//        if (chien instanceof Animal.Chien(var nom, _)) {
//            System.out.println(nom);
//        }

        // Switch avec pattern matching
        Animal animal = new Animal.Chat("Miaou");
        String age = switch (animal) {
            case Animal.Chat chat -> chat.nom() + "n'a pas d'age";
            case Animal.Chien(var leNom, var ageDuChien) when ageDuChien > 18 -> leNom + " est majeur " + ageDuChien;
            case Animal.Chien(var leNom, var ageDuChien) -> {
                String nom = leNom + " est mineur " + ageDuChien;
                yield nom;
            }
        };


        // Pattern matching avancé
        // Définition locale d'un record
        record Jai2Animaux(Animal animal1, Animal animal2) {}

        Animal animal2 = new Animal.Chien("Medor", 5);

        String cas = switch (new Jai2Animaux(animal, animal2)) {
            case Jai2Animaux(Animal.Chien chien1, Animal.Chien chien2) -> "J'ai 2 chiens";
            case Jai2Animaux(Animal.Chat chat1, Animal.Chat chat2) -> "J'ai 2 chat";
            case Jai2Animaux(Animal.Chien chien1, Animal.Chat chat2) -> "J'ai 1 chien et 1 chat";
            case Jai2Animaux(Animal.Chat chat1, Animal.Chien chien2) -> "J'ai 1 chien et 1 chat";
        };

        // Collecter un type donné dans une list == filter + map
        List<Animal> animals = List.of();
        List<Animal.Chien> chiens = animals
            .stream()
            .flatMap(a -> switch (a) {
                case Animal.Chien c -> Stream.of(c);
                default -> Stream.of();
            })
            .toList();

        // Switch sur un optional
        Optional<String> stringOrVide = Optional.of("C'est pas vide");

        String resultat = switch (opt(stringOrVide)) {
            case Opt.Present(var value) -> value;
            case Opt.Empty() -> "c'est vide";
        };

    }

    public static <T> Opt<T> opt(Optional<T> optional) {
        return optional.<Opt<T>>map(Opt.Present::new).orElse(new Opt.Empty<>());
    }
    public sealed interface Opt<T> {
        record Present<T>(T value) implements Opt<T> {}
        record Empty<T>() implements Opt<T> {}
    }
}
