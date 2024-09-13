package fr.maif.patternjava.app.api;

import fr.maif.patternjava.app.domain.LivraisonDeColis;
import fr.maif.patternjava.app.domain.models.ColisOuErreur;
import fr.maif.patternjava.app.domain.models.ColisOuErreur.ColisExistant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colis")
public class ColisController {
    private final LivraisonDeColis livraisonDeColis;

    public ColisController(@Qualifier("LivraisonDeColis") LivraisonDeColis livraisonDeColis) {
        this.livraisonDeColis = livraisonDeColis;
    }


    @GetMapping
    public List<ColisExistant> listerColis() {
        return this.livraisonDeColis.listerColis();
    }


    @PostMapping
    public ResponseEntity<ColisOuErreur.Colis> prendreEnChargeLeColis(@RequestBody @Valid ColisOuErreur.Colis colis) {
        return switch (this.livraisonDeColis.prendreEnChargeLeColis(colis)) {
            case ColisOuErreur.Colis colisPrisEnCharge -> ResponseEntity.ok(colisPrisEnCharge);
            default -> ResponseEntity.internalServerError().build();
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> gererLeColis(@PathVariable("id") String id, @RequestBody @Valid ColisOuErreur.Colis colis) {
        return switch (this.livraisonDeColis.gererColis(colis)) {
            case ColisOuErreur.Colis colisGere -> ResponseEntity.ok(colisGere);
            case ColisOuErreur.ColisNonTrouve __ -> ResponseEntity.notFound().build();
            case ColisOuErreur.ColisInvalide colisInvalide -> {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatusCode.valueOf(400),
                        colisInvalide.message()
                );
                problemDetail.setProperty("error", colisInvalide.message());
                yield ResponseEntity.badRequest().body(problemDetail);
            }
        };
    }

}