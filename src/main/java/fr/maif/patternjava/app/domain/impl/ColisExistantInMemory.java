package fr.maif.patternjava.app.domain.impl;

import fr.maif.patternjava.app.domain.ColisExistants;
import fr.maif.patternjava.app.domain.models.Colis;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ColisExistantInMemory implements ColisExistants {

    private final ConcurrentHashMap<String, Colis.ColisExistant> store = new ConcurrentHashMap<>();

    @Override
    public Colis.ColisExistant chercherColisExistantParReference(String referenceColis) {
        return store.get(referenceColis);
    }

    @Override
    public Colis.ColisExistant enregistrerColis(Colis.ColisExistant colisPrisEnCharge) {
        return store.compute(colisPrisEnCharge.reference(), (id, colisExistant) -> colisPrisEnCharge);
    }

    @Override
    public Colis.ColisExistant mettreAJourColis(Colis.ColisExistant colis) {
        return store.compute(colis.reference(), (id, colisExistant) -> colis);
    }

    @Override
    public List<Colis.ColisExistant> listerColis() {
        return store.values().stream().toList();
    }
}
