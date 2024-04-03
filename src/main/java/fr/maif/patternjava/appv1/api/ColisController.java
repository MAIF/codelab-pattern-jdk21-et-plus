package fr.maif.patternjava.appv1.api;

import fr.maif.patternjava.appv1.domain.LivraisonDeColis;
import fr.maif.patternjava.appv1.domain.errors.ColisNonTrouve;
import fr.maif.patternjava.appv1.domain.errors.EtatInvalide;
import fr.maif.patternjava.appv1.domain.models.Colis;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colis")
public class ColisController {
    private final LivraisonDeColis livraisonDeColis;

    public ColisController(LivraisonDeColis livraisonDeColis) {
        this.livraisonDeColis = livraisonDeColis;
    }


    @GetMapping
    public List<Colis> listerColis() {
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
