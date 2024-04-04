package fr.maif.patternjava.appv2.domain.impl;

import fr.maif.patternjava.appv2.domain.ColisExistants;
import fr.maif.patternjava.appv2.domain.models.ColisOuErreur.ColisExistant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ColisExistantInMemory implements ColisExistants {

    private final ConcurrentHashMap<String, ColisExistant> store = new ConcurrentHashMap<>();

    @Override
    public ColisExistant chercherColisExistantParReference(String referenceColis) {
        return store.get(referenceColis);
    }

    @Override
    public ColisExistant enregistrerColis(ColisExistant colisPrisEnCharge) {
        return store.compute(colisPrisEnCharge.reference(), (id, colisExistant) -> colisPrisEnCharge);
    }

    @Override
    public ColisExistant mettreAJourColis(ColisExistant colis) {
        return store.compute(colis.reference(), (id, colisExistant) -> colis);
    }

    @Override
    public List<ColisExistant> listerColis() {
        return store.values().stream().toList();
    }
}
