package fr.maif.patternjava.patterns.a_records;

public class RecordsMain {

    public static void main(String[] args) {
        MonRecord monRecord = new MonRecord("Test");

        String attribut1 = monRecord.attribut1();
        System.out.println(attribut1);
    }
}
