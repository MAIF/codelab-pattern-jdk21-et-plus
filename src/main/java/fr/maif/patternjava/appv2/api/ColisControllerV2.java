package fr.maif.patternjava.appv2.api;

import fr.maif.patternjava.appv2.domain.LivraisonDeColis;
import fr.maif.patternjava.appv2.domain.errors.ColisNonTrouve;
import fr.maif.patternjava.appv2.domain.errors.EtatInvalide;
import fr.maif.patternjava.appv2.domain.models.Colis;
import fr.maif.patternjava.appv2.domain.models.Colis.ColisExistant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/colis")
public class ColisControllerV2 {
    private final LivraisonDeColis livraisonDeColis;

    public ColisControllerV2(@Qualifier("LivraisonDeColis") LivraisonDeColis livraisonDeColis) {
        this.livraisonDeColis = livraisonDeColis;
    }


    @GetMapping
    public List<ColisExistant> listerColis() {
        return this.livraisonDeColis.listerColis();
    }


    @PostMapping
    public ResponseEntity<Colis> prendreEnChargeLeColis(@RequestBody @Valid Colis colis) {
        try {
            return ResponseEntity.ok(this.livraisonDeColis.prendreEnChargeLeColis(colis));
        } catch (ColisNonTrouve e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> gererLeColis(@PathVariable("id") String id, @RequestBody @Valid Colis colis) {
        try {
            return ResponseEntity.ok(this.livraisonDeColis.gererColis(colis));
        } catch (ColisNonTrouve e) {
            return ResponseEntity.notFound().build();
        } catch (EtatInvalide e) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatusCode.valueOf(400),
                    e.getMessage()
            );
            problemDetail.setProperty("error", e.getMessage());
            return ResponseEntity.badRequest().body(problemDetail);
        }
    }

}
